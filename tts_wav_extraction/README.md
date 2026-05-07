# Android TTS 转 WAV 音频流程详解

## 一、概述

本文档详细说明了如何从 Android 系统 TTS（Text-to-Speech）获取音频并保存为 WAV 文件的完整流程。

### 1.1 两种播放模式

| 模式 | `isDirectPlayMode` | 行为 | 用途 |
|------|-------------------|------|------|
| **直接播放模式** | `true` | 调用 `speak()` 直接播放，不保存文件 | 低延迟，兼容性差 |
| **中转模式** | `false` | 调用 `synthesizeToFile()` 保存WAV，再通过管道读取 | 兼容性好，有性能损耗 |

### 1.2 核心文件列表

```
src/
├── com/github/jing332/tts/speech/local/
│   ├── AndroidTtsEngine.kt      # 核心引擎封装类
│   ├── LocalTtsProvider.kt       # TTS提供者（决定使用哪种模式）
│   └── TtsEngineError.kt        # 错误类型定义
├── com/github/jing332/tts/speech/
│   ├── TextToSpeechProvider.kt  # TTS提供者基类
│   ├── EngineState.kt           # 引擎状态定义
│   └── ILifeState.kt            # 生命周期接口
├── com/github/jing332/database/entities/systts/
│   └── AudioParams.kt           # 音频参数（语速、音高、音量）
└── com/github/jing332/database/entities/systts/source/
    ├── LocalTtsSource.kt         # 本地TTS配置
    ├── LocalTtsParameter.kt      # 额外参数
    └── TextToSpeechSource.kt    # TTS配置基类
```

---

## 二、详细流程分析

### 2.1 整体架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                        调用入口                                  │
│                    (LocalTtsProvider)                            │
└─────────────────────────────┬───────────────────────────────────┘
                              │
                    ┌─────────▼─────────┐
                    │  isDirectPlayMode │
                    │      检查          │
                    └─────────┬─────────┘
                              │
           ┌──────────────────┴──────────────────┐
           │                                         │
           ▼                                         ▼
┌─────────────────────┐                 ┌─────────────────────┐
│   true (直接播放)     │                 │   false (中转模式)   │
│   调用 syncPlay()     │                 │   调用 getStream()   │
└─────────┬───────────┘                 └─────────┬───────────┘
          │                                           │
          ▼                                           ▼
┌─────────────────────┐                 ┌─────────────────────────┐
│  AndroidTtsEngine   │                 │   AndroidTtsEngine      │
│      .play()        │                 │       .getStream()       │
│                     │                 │                         │
│  tts.speak()        │                 │  1. 创建临时WAV文件      │
│  直接播放，无文件输出 │                 │  2. synthesizeToFile() │
└─────────────────────┘                 │  3. 管道返回InputStream  │
                                          └────────────┬────────────┘
                                                       │
                                                       ▼
                                          ┌─────────────────────────┐
                                          │    生成 WAV 文件        │
                                          │  /cache/AndroidTTS/     │
                                          │    xxx.wav              │
                                          └─────────────────────────┘
```

---

### 2.2 核心类详解

#### 2.2.1 AndroidTtsEngine - 核心引擎封装

这是最核心的类，负责与 Android 原生 `TextToSpeech` API 交互。

**关键方法对比：**

| 方法 | 用途 | 输出 | 等待方式 |
|------|------|------|----------|
| `getFile()` | 保存WAV文件 | File | `onDone()` 回调 |
| `getStream()` | 获取音频流 | InputStream | `onAudioAvailable()` 回调 |
| `getAudio()` | 音频回调 | Listener 回调 | 多个回调 |
| `play()` | 直接播放 | 无 | `onDone()` 回调 |

#### 2.2.2 LocalTtsProvider - TTS提供者

决定使用哪种模式的中枢：

```kotlin
// 判断是否为直接播放模式
override fun isSyncPlay(source: LocalTtsSource): Boolean {
    return source.isDirectPlayMode
}

// 中转模式
override suspend fun getStream(params: SystemParams, source: LocalTtsSource): InputStream {
    return tts.getStream(...)  // 最终调用 synthesizeToFile
}

// 直接播放模式
override suspend fun syncPlay(params: SystemParams, source: LocalTtsSource) {
    tts.play(...)  // 最终调用 speak
}
```

---

## 三、详细代码流程（getStream 方法）

### 3.1 方法签名

```kotlin
suspend fun getStream(
    text: String,                      // 要转换的文本
    locale: String = "",               // 语言标签（如 "zh-CN"）
    voice: String = "",               // 声音名称
    extraParams: List<LocalTtsParameter> = emptyList(),  // 额外参数
    params: AudioParams = AudioParams(),  // 音频参数
): Result<InputStream, TtsEngineError>
```

### 3.2 完整流程

```
┌─────────────────────────────────────────────────────────────────┐
│                     getStream() 完整流程                         │
└─────────────────────────────────────────────────────────────────┘

第1步：互斥锁
    │
    ▼
    mutex.withLock {
        │
        ▼
第2步：检查引擎状态
    │
    ├─── 失败 ──► 返回 Err(TtsEngineError.Initialization)
    │
    ▼
第3步：创建协程作用域
    │
    coroutineScope {
        │
        ▼
第4步：创建临时文件
        │
        ├── filename = System.currentTimeMillis() + ".wav"
        ├── file = File(cacheDir, filename)
        │
        ▼
第5步：确保目录存在
        │
        ├─── 失败 ──► 返回 Err(TtsEngineError.File)
        │
        ▼
第6步：定义删除辅助函数
        │
        fun delete() { file.delete() }
        │
        ▼
第7步：设置引擎参数
        │
        ├── tts.language = Locale.forLanguageTag(locale)
        ├── tts.voice = ...
        ├── tts.setSpeechRate(params.speed)
        ├── tts.setPitch(params.pitch)
        │
        ▼
第8步：合成到文件
        │
        tts.synthesizeToFile(text, bundle, file, filename)
        │
        ├─── 失败 ──► delete() + 返回 Err(TtsEngineError.Engine)
        │
        ▼
第9步：创建管道用于流式读取
        │
        ├── pos = PipedOutputStream()
        ├── pis = PipedInputStream(pos)
        │
        ▼
第10步：设置回调监听器
        │
        tts.setOnUtteranceProgressListener(object : ... {
            │
            ├── onStart() ───► continuation.resume(Ok(pis))  返回管道
            │
            ├── onAudioAvailable(audio) ──► pos.write(audio)  写入管道
            │
            ├── onDone() ───► pos.close() + delete()
            │
            └── onError() ──► pos.close() + delete()
        })
        │
        ▼
第11步：处理取消
        │
        continuation.invokeOnCancellation {
            delete()
            pos.close()
            mTts?.stop()
        }
    }
```

---

## 四、关键 API 说明

### 4.1 TextToSpeech.synthesizeToFile()

```kotlin
// 原型
public int synthesizeToFile(
    CharSequence text,      // 要合成的文本
    Bundle params,          // 参数 Bundle
    File file,              // 输出文件
    String utteranceId      // utterance ID（不能为 null！）
): Int

// 返回值
// - TextToSpeech.SUCCESS: 成功
// - TextToSpeech.ERROR: 失败
```

**重要**：Android 8.0+ 之前，`synthesizeToFile()` 合成的是 pcm 格式，Android 8.0+ 合成的是 wav 格式。

### 4.2 UtteranceProgressListener 回调

```kotlin
tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
    
    // 合成开始时调用
    override fun onStart(utteranceId: String?) {}
    
    // 合成完成时调用
    override fun onDone(utteranceId: String?) {}
    
    // 合成出错时调用
    override fun onError(utteranceId: String?) {}
    
    // Android 21+ 可用：音频数据可用时调用
    // 用于流式读取音频数据
    override fun onAudioAvailable(utteranceId: String?, audio: ByteArray?) {
        // audio 是 PCM 格式的音频数据
    }
})
```

### 4.3 PipedInputStream / PipedOutputStream 管道

管道用于实现流式读取：

```
生产者（PipedOutputStream）          消费者（PipedInputStream）
       │                                    ▲
       │  write(audio)                     │  read()
       └────────────────────────────────────┘
       
一边写入音频数据，一边可以读取
```

---

## 五、完整使用示例

### 5.1 依赖配置（build.gradle）

```groovy
dependencies {
    // Kotlin 协程
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    
    // Result 类型（可选，也可以自己实现）
    implementation 'com.github.michaelbull:result:1.1.0'
}
```

### 5.2 完整示例代码

```kotlin
package com.example.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.*

/**
 * Android TTS 转 WAV 完整示例
 */
class TtsToWavConverter(private val context: Context) {

    // 缓存目录
    private val cacheDir = context.externalCacheDir!!.absolutePath + "/AndroidTTS"

    // TTS 引擎
    private var tts: TextToSpeech? = null

    // 互斥锁
    private val mutex = Mutex()

    /**
     * 初始化 TTS 引擎
     */
    suspend fun init(enginePackageName: String = ""): Boolean = suspendCancellableCoroutine { cont ->
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // 如果指定了引擎，切换引擎
                if (enginePackageName.isNotEmpty()) {
                    val result = tts?.setEngineByPackageName(enginePackageName)
                    cont.resume(result == TextToSpeech.SUCCESS)
                } else {
                    cont.resume(true)
                }
            } else {
                cont.resume(false)
            }
        }
        cont.invokeOnCancellation { tts?.shutdown() }
    }

    /**
     * 设置语言和音频参数
     */
    private fun setParams(
        locale: String,
        speechRate: Float,
        pitch: Float,
        volume: Float?
    ): Bundle {
        tts?.apply {
            language = Locale.forLanguageTag(locale)
            setSpeechRate(speechRate)
            setPitch(pitch)
        }

        return Bundle().apply {
            volume?.let {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, it)
            }
        }
    }

    /**
     * 将文本保存为 WAV 文件
     *
     * @param text 要转换的文本
     * @param locale 语言标签（如 "zh-CN"）
     * @param speechRate 语速（1.0 正常）
     * @param pitch 音高（1.0 正常）
     * @param volume 音量（0.0-1.0，可为 null 表示跟随系统）
     * @return Result 包含文件对象或错误
     */
    suspend fun saveToWavFile(
        text: String,
        locale: String = "zh-CN",
        speechRate: Float = 1.0f,
        pitch: Float = 1.0f,
        volume: Float? = null
    ): Result<File, Error> = mutex.withLock {
        val engine = tts ?: return@withLock Result.failure(Error("引擎未初始化"))

        // 创建目录
        val dir = File(cacheDir)
        if (!dir.exists() && !dir.mkdirs()) {
            return@withLock Result.failure(Error("无法创建目录"))
        }

        // 创建文件
        val filename = "${System.currentTimeMillis()}.wav"
        val file = File(dir, filename)

        // 设置参数
        val params = setParams(locale, speechRate, pitch, volume)

        // 合成到文件
        val result = engine.synthesizeToFile(text, params, file, filename)
        if (result != TextToSpeech.SUCCESS) {
            file.delete()
            return@withLock Result.failure(Error("合成失败"))
        }

        // 等待合成完成
        suspendCancellableCoroutine { cont ->
            engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    cont.resume(Result.success(file))
                }
                override fun onError(utteranceId: String?) {
                    file.delete()
                    cont.resume(Result.failure(Error("合成错误")))
                }
            })
            cont.invokeOnCancellation {
                engine.stop()
                file.delete()
            }
        }
    }

    /**
     * 获取音频流（用于中转播放）
     *
     * @return Result 包含 InputStream 或错误
     */
    suspend fun getAudioStream(
        text: String,
        locale: String = "zh-CN",
        speechRate: Float = 1.0f,
        pitch: Float = 1.0f
    ): Result<PipedInputStream, Error> = mutex.withLock {
        val engine = tts ?: return@withLock Result.failure(Error("引擎未初始化"))

        // 创建目录
        val dir = File(cacheDir)
        if (!dir.exists() && !dir.mkdirs()) {
            return@withLock Result.failure(Error("无法创建目录"))
        }

        // 创建临时文件（用于缓存）
        val filename = "${System.currentTimeMillis()}.wav"
        val file = File(dir, filename)

        // 设置参数
        val params = setParams(locale, speechRate, pitch, null)

        // 合成到文件
        val result = engine.synthesizeToFile(text, params, file, filename)
        if (result != TextToSpeech.SUCCESS) {
            file.delete()
            return@withLock Result.failure(Error("合成失败"))
        }

        // 创建管道
        val pos = PipedOutputStream()
        val pis = PipedInputStream(pos)

        // 设置监听器
        suspendCancellableCoroutine { cont ->
            engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    cont.resume(Result.success(pis))
                }

                override fun onAudioAvailable(utteranceId: String?, audio: ByteArray?) {
                    audio?.let {
                        try {
                            pos.write(it)
                        } catch (e: Exception) {
                            // ignore
                        }
                    }
                }

                override fun onDone(utteranceId: String?) {
                    try {
                        pos.close()
                    } catch (e: Exception) {
                        // ignore
                    }
                    file.delete()
                }

                override fun onError(utteranceId: String?) {
                    try {
                        pos.close()
                    } catch (e: Exception) {
                        // ignore
                    }
                    file.delete()
                }
            })

            cont.invokeOnCancellation {
                try {
                    pos.close()
                } catch (e: Exception) {
                    // ignore
                }
                file.delete()
                engine.stop()
            }
        }
    }

    /**
     * 直接播放（不保存文件）
     */
    suspend fun speak(
        text: String,
        locale: String = "zh-CN",
        speechRate: Float = 1.0f,
        pitch: Float = 1.0f
    ): Result<Unit, Error> = mutex.withLock {
        val engine = tts ?: return@withLock Result.failure(Error("引擎未初始化"))

        val params = setParams(locale, speechRate, pitch, null)

        val result = engine.speak(text, TextToSpeech.QUEUE_FLUSH, params, "utterance_${System.currentTimeMillis()}")
        if (result != TextToSpeech.SUCCESS) {
            return@withLock Result.failure(Error("播放失败"))
        }

        suspendCancellableCoroutine { cont ->
            engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    cont.resume(Result.success(Unit))
                }
                override fun onError(utteranceId: String?) {
                    cont.resume(Result.failure(Error("播放错误")))
                }
            })
            cont.invokeOnCancellation {
                engine.stop()
            }
        }
    }

    /**
     * 获取可用的语言列表
     */
    fun getAvailableLocales(): List<Locale> {
        return tts?.availableLanguages?.toList()?.sortedBy { it.toString() } ?: emptyList()
    }

    /**
     * 获取可用的声音列表
     */
    fun getAvailableVoices(): List<Voice> {
        return tts?.voices?.toList() ?: emptyList()
    }

    /**
     * 释放资源
     */
    fun shutdown() {
        tts?.shutdown()
        tts = null
    }
}
```

### 5.3 使用示例

```kotlin
class MainActivity : AppCompatActivity() {
    
    private lateinit var converter: TtsToWavConverter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        converter = TtsToWavConverter(this)
        
        // 初始化
        CoroutineScope(Dispatchers.Main).launch {
            val success = converter.init()  // 使用默认引擎
            if (!success) {
                Toast.makeText(this@MainActivity, "TTS 初始化失败", Toast.LENGTH_SHORT).show()
                return@launch
            }
            
            // 示例1：保存为WAV文件
            val fileResult = converter.saveToWavFile(
                text = "你好，这是一段测试文本。",
                locale = "zh-CN",
                speechRate = 1.0f,
                pitch = 1.0f
            )
            
            when (fileResult) {
                is Ok -> {
                    val file = fileResult.value
                    Log.d("TTS", "WAV文件已保存: ${file.absolutePath}")
                    // 可以播放这个文件
                    MediaPlayer().apply {
                        setDataSource(file.absolutePath)
                        prepare()
                        start()
                    }
                }
                is Err -> {
                    Log.e("TTS", "保存失败: ${fileResult.value.message}")
                }
            }
            
            // 示例2：直接播放
            converter.speak("直接播放测试")
            
            // 示例3：获取流（用于网络传输等）
            val streamResult = converter.getAudioStream(
                text = "流式测试文本"
            )
            // streamResult.value 可以用于网络传输等场景
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        converter.shutdown()
    }
}
```

---

## 六、注意事项

### 6.1 utteranceId 不能为 null

调用 `synthesizeToFile()` 和 `speak()` 时，utteranceId 参数不能为 null，否则 `OnUtteranceProgressListener` 不会触发。

### 6.2 文件清理

临时文件需要在合适的时机清理，否则会占用大量存储空间。

### 6.3 权限

如果需要保存到外部存储，需要添加权限：

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" 
    android:maxSdkVersion="28" />
```

### 6.4 线程

所有 TTS 相关操作需要在主线程或使用协程，所有监听器回调都在主线程。

### 6.5 Android 版本

- `synthesizeToFile()` 在 Android 8.0 (API 26) 之前输出 pcm 格式，8.0+ 输出 wav 格式
- `onAudioAvailable()` 需要 Android 5.0 (API 21)+
- 语言和声音的可用性因设备和引擎而异

---

## 七、WAV 文件格式说明

Android TTS 的 `synthesizeToFile()` 生成的是 16-bit PCM 音频文件。

**WAV 文件头结构（44字节）：**

```
Offset  Size  Description
------  ----  -----------
0       4     "RIFF" 标志
4       4     文件长度 - 8
8       4     "WAVE" 标志
12      4     "fmt " 标志
16      4     格式数据长度 (通常 16)
20      2     格式类型 (1 = PCM)
22      2     声道数 (1 = 单声道, 2 = 立体声)
24      4     采样率 (如 16000)
28      4     字节速率 = 采样率 * 声道数 * 每样本字节数
30      2     块对齐 = 声道数 * 每样本字节数
32      2     每样本位数 (16)
36      4     "data" 标志
40      4     音频数据长度
44      N     音频数据
```

---

## 八、错误处理

本项目定义了三种错误类型：

```kotlin
sealed interface TtsEngineError {
    object Initialization : TtsEngineError  // 引擎未初始化
    object Engine : TtsEngineError          // 引擎执行错误
    object File : TtsEngineError            // 文件操作错误
}
```

建议在生产环境中添加更详细的错误信息，如错误码、错误描述等。
