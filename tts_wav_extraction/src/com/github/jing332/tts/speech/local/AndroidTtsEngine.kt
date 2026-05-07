package com.github.jing332.tts.speech.local

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import com.github.jing332.database.entities.systts.AudioParams
import com.github.jing332.database.entities.systts.source.LocalTtsParameter
import com.github.jing332.database.entities.systts.source.LocalTtsSource
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.*
import kotlin.collections.forEach
import kotlin.coroutines.resume


/**
 * `AndroidTtsEngine` 是 Android 文本转语音(TTS)引擎的封装类。
 * 它提供了简化的接口用于初始化、管理和使用TTS引擎进行语音合成，
 * 包括生成音频流和直接播放语音。
 *
 * @param context 应用程序上下文，用于访问系统资源和初始化TTS引擎
 * @param cacheDir 临时音频文件存储目录，默认为外部缓存目录下的 "AndroidTTS" 文件夹
 */
class AndroidTtsEngine(
    val context: Context,
    val cacheDir: String = context.externalCacheDir!!.absolutePath + "${File.separator}AndroidTTS",
) {
    companion object {
    }

    // Android 原生 TextToSpeech 对象
    private var mTts: TextToSpeech? = null

    // 当前引擎名称
    private var mEngineName: String = ""

    /**
     * 初始化 TTS 引擎
     * @param engineName 引擎名称（如空字符串使用默认引擎）
     * @return true 如果引擎初始化成功
     */
    suspend fun init(engineName: String): Boolean = coroutineScope {
        // 如果引擎名称改变，先释放旧引擎
        if (mEngineName != engineName) {
            mEngineName = engineName
            release()
        }

        // 使用 suspendCancellableCoroutine 实现异步转同步
        suspendCancellableCoroutine<Boolean> { continuation ->
            // 创建 TextToSpeech 实例，engineName 为空使用默认引擎
            mTts = TextToSpeech(context, { status ->
                when (status) {
                    TextToSpeech.SUCCESS -> {
                        continuation.resume(true)
                    }
                    TextToSpeech.ERROR -> {
                        continuation.resume(false)
                    }
                }
            }, engineName)

            continuation.invokeOnCancellation {
                release()
            }
        }
    }

    /**
     * 获取所有可用的声音列表
     */
    val voices: List<Voice>
        get() = mTts?.voices?.toList() ?: emptyList()

    /**
     * 获取所有支持的语言
     */
    val locales: List<Locale>
        get() = mTts?.availableLanguages?.toList()?.sortedBy { it.toString() } ?: emptyList()

    /**
     * 获取当前选中的声音
     */
    val voice: Voice?
        get() = mTts?.voice

    /**
     * 设置声音
     * @param voice 声音对象
     * @return true 如果设置成功
     */
    fun setVoice(voice: Voice): Boolean {
        return mTts?.setVoice(voice) == TextToSpeech.SUCCESS
    }

    /**
     * 设置引擎播放参数
     * @param engine TextToSpeech 实例
     * @param locale 语言标签（如 "zh-CN"）
     * @param voice 声音名称
     * @param extraParams 额外参数列表
     * @param params 音频参数（语速、音高、音量）
     * @return 包含参数的 Bundle
     */
    private fun setEnginePlayParams(
        engine: TextToSpeech,
        locale: String,
        voice: String,
        extraParams: List<LocalTtsParameter>?,
        params: AudioParams,
    ): Bundle {
        engine.apply {
            // 设置语言
            if (locale.isNotEmpty())
                language = Locale.forLanguageTag(locale)

            // 设置声音
            if (voice.isNotEmpty())
                voices?.forEach {
                    if (it.name == voice) this.voice = it
                }

            // 设置语速和音高
            setSpeechRate(params.speed)
            setPitch(params.pitch)

            // 构建参数 Bundle
            return Bundle().apply {
                // 如果音量不是跟随系统，则设置音量
                if (params.volume != LocalTtsSource.VOLUME_FOLLOW) {
                    putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, params.volume)
                }
                // 添加额外参数
                extraParams?.forEach { it.putValueFromBundle(this) }
            }
        }
    }

    // 互斥锁，防止并发操作
    private val mutex = Mutex()

    /**
     * 生成 WAV 音频文件
     *
     * 核心流程：
     * 1. 创建临时文件
     * 2. 调用 synthesizeToFile() 合成到文件
     * 3. 等待合成完成（通过 UtteranceProgressListener）
     * 4. 返回文件对象
     *
     * @param text 要转换的文本
     * @param locale 语言标签
     * @param voice 声音名称
     * @param extraParams 额外参数
     * @param params 音频参数
     * @return Result 包含生成的 File 或错误
     */
    suspend fun getFile(
        text: String,
        locale: String = "",
        voice: String = "",
        extraParams: List<LocalTtsParameter> = emptyList(),
        params: AudioParams = AudioParams(),
    ): Result<File, TtsEngineError> = mutex.withLock {
        val tts = mTts ?: return@withLock Err(TtsEngineError.Initialization)

        coroutineScope {
            // 1. 创建临时文件
            val filename = System.currentTimeMillis().toString() + ".wav"
            val file = File(cacheDir, filename)

            // 确保目录存在
            if (file.parentFile?.exists() != true && file.parentFile?.mkdirs() != true)
                return@coroutineScope Err(TtsEngineError.File)

            // 删除文件的辅助函数
            fun delete() {
                try {
                    file.delete()
                } catch (_: Exception) {
                }
            }

            // 2. 设置引擎参数
            val bundle = setEnginePlayParams(tts, locale, voice, extraParams, params)

            // 3. 调用 synthesizeToFile 合成到文件
            // 这个方法会异步执行，我们需要等待它完成
            val ret = tts.synthesizeToFile(text, bundle, file, filename)
            if (ret != TextToSpeech.SUCCESS) {
                delete()
                return@coroutineScope Err(TtsEngineError.Engine)
            }

            // 4. 使用 suspendCancellableCoroutine 等待合成完成
            suspendCancellableCoroutine<Result<File, TtsEngineError>> { continuation ->
                tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        // 合成开始时调用
                    }

                    override fun onDone(utteranceId: String?) {
                        // 合成完成，返回文件
                        continuation.resume(Ok(file))
                    }

                    override fun onError(utteranceId: String?) {
                        // 合成出错，删除文件并返回错误
                        delete()
                        continuation.resume(Err(TtsEngineError.Engine))
                    }
                })

                continuation.invokeOnCancellation {
                    delete()
                    mTts?.stop()
                }
            }
        }
    }


    /**
     * 生成音频流
     *
     * 核心流程：
     * 1. 创建临时文件
     * 2. 调用 synthesizeToFile() 合成到文件
     * 3. 创建管道（PipedInputStream/PipedOutputStream）
     * 4. 通过 onAudioAvailable 回调将音频数据写入管道
     * 5. 返回管道输入流
     *
     * @param text 要转换的文本
     * @param locale 语言标签
     * @param voice 声音名称
     * @param extraParams 额外参数
     * @param params 音频参数
     * @return Result 包含 InputStream 或错误
     */
    suspend fun getStream(
        text: String,
        locale: String = "",
        voice: String = "",
        extraParams: List<LocalTtsParameter> = emptyList(),
        params: AudioParams = AudioParams(),
    ): Result<InputStream, TtsEngineError> = mutex.withLock {
        val tts = mTts ?: return@withLock Err(TtsEngineError.Initialization)

        coroutineScope {
            // 1. 创建临时文件
            val filename = System.currentTimeMillis().toString() + ".wav"
            val file = File(cacheDir, filename)

            // 确保目录存在
            if (file.parentFile?.exists() != true && file.parentFile?.mkdirs() != true)
                return@coroutineScope Err(TtsEngineError.File)

            // 删除文件的辅助函数
            fun delete() {
                try {
                    file.delete()
                } catch (_: Exception) {
                }
            }

            // 2. 设置引擎参数
            val bundle = setEnginePlayParams(tts, locale, voice, extraParams, params)

            // 3. 调用 synthesizeToFile 合成到文件
            val ret = tts.synthesizeToFile(text, bundle, file, filename)
            if (ret != TextToSpeech.SUCCESS)
                return@coroutineScope Err(TtsEngineError.Engine)

            // 4. 使用管道流返回数据
            suspendCancellableCoroutine<Result<InputStream, TtsEngineError>> { continuation ->
                // 创建管道
                val pos = PipedOutputStream()
                val pis = PipedInputStream(pos)

                tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String) {
                        // 开始时返回管道输入流
                        continuation.resume(Ok(pis))
                    }

                    override fun onDone(utteranceId: String) {
                        runCatching {
                            pos.close()
                        }
                        // 完成时删除临时文件
                        delete()
                    }

                    override fun onError(utteranceId: String) {
                        runCatching {
                            pos.close()
                        }
                        // 错误时删除临时文件
                        delete()
                    }

                    // 音频数据可用时写入管道
                    override fun onAudioAvailable(utteranceId: String, audio: ByteArray) {
                        super.onAudioAvailable(utteranceId, audio)
                        pos.write(audio)
                    }
                })

                continuation.invokeOnCancellation {
                    delete()
                    pos.close()
                    mTts?.stop()
                }
            }
        }
    }

    /**
     * 生成音频并通过监听器回调
     *
     * @param text 要转换的文本
     * @param locale 语言标签
     * @param voice 声音名称
     * @param extraParams 额外参数
     * @param params 音频参数
     * @param listener 音频监听器
     * @return Result
     */
    suspend fun getAudio(
        text: String,
        locale: String = "",
        voice: String = "",
        extraParams: List<LocalTtsParameter> = emptyList(),
        params: AudioParams = AudioParams(),
        listener: Listener,
    ): Result<Unit, TtsEngineError> = mutex.withLock {
        val tts = mTts ?: return@withLock Err(TtsEngineError.Initialization)

        coroutineScope {
            // 创建临时文件
            val filename = System.currentTimeMillis().toString() + ".wav"
            val file = File(cacheDir, filename)
            if (file.parentFile?.exists() != true && file.parentFile?.mkdirs() != true)
                return@coroutineScope Err(TtsEngineError.File)

            fun delete() {
                try {
                    file.delete()
                } catch (_: Exception) {
                }
            }

            // 设置参数并合成
            val bundle = setEnginePlayParams(tts, locale, voice, extraParams, params)
            val ret = tts.synthesizeToFile(text, bundle, file, filename)
            if (ret != TextToSpeech.SUCCESS)
                return@coroutineScope Err(TtsEngineError.Engine)

            // 使用监听器回调
            suspendCancellableCoroutine<Result<Unit, TtsEngineError>> { continuation ->
                tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String) {
                        listener.start()
                    }

                    override fun onDone(utteranceId: String) {
                        listener.done()
                        continuation.resume(Ok(Unit))
                        delete()
                    }

                    override fun onError(utteranceId: String) {
                        continuation.resume(Err(TtsEngineError.Engine))
                        delete()
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        super.onError(utteranceId, errorCode)
                    }

                    override fun onAudioAvailable(utteranceId: String, audio: ByteArray) {
                        super.onAudioAvailable(utteranceId, audio)
                        listener.available(audio)
                    }
                })

                continuation.invokeOnCancellation {
                    delete()
                    mTts?.stop()
                }
            }
        }
    }

    /**
     * 直接播放语音（不保存文件）
     *
     * @param text 要转换的文本
     * @param locale 语言标签
     * @param voice 声音名称
     * @param extraParams 额外参数
     * @param params 音频参数
     * @param queueMode 队列模式，QUEUE_FLUSH 清空队列立即播放
     * @return Result
     */
    suspend fun play(
        text: String,
        locale: String = "",
        voice: String = "",
        extraParams: List<LocalTtsParameter> = emptyList(),
        params: AudioParams = AudioParams(),
        queueMode: Int = TextToSpeech.QUEUE_FLUSH,
    ): Result<Unit, TtsEngineError> = mutex.withLock {
        val tts = mTts ?: return@withLock Err(TtsEngineError.Initialization)

        val bundle = setEnginePlayParams(tts, locale, voice, extraParams, params)
        // 注意：utteranceId 不能为 null，否则 OnUtteranceProgressListener 不会触发
        val ret = tts.speak(text, queueMode, bundle, "")
        if (ret != TextToSpeech.SUCCESS) return@withLock Err(TtsEngineError.Engine)

        suspendCancellableCoroutine<Result<Unit, TtsEngineError>> { continuation ->
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                }

                override fun onDone(utteranceId: String?) {
                    continuation.resume(Ok(Unit))
                }

                override fun onError(utteranceId: String?) {
                    continuation.resume(Err(TtsEngineError.Engine))
                }
            })

            continuation.invokeOnCancellation {
                tts.stop()
            }
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        mTts?.shutdown()
        mTts = null
    }

    /**
     * 音频监听器接口
     */
    interface Listener {
        fun start()
        fun available(audio: ByteArray)
        fun done()
    }
}
