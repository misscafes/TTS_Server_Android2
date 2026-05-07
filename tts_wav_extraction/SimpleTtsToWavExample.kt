package com.example.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.github.jing332.database.entities.systts.AudioParams
import com.github.jing332.database.entities.systts.source.LocalTtsParameter
import com.github.jing332.database.entities.systts.source.LocalTtsSource
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.*
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.*
import kotlin.coroutines.resume

/**
 * 简化版 Android TTS 转 WAV 示例
 *
 * 这个文件展示了一个简化的独立实现，不依赖外部库
 * 演示了如何将系统TTS转换为WAV文件的核心流程
 */
object SimpleTtsToWavExample {

    /**
     * TTS 错误类型
     */
    sealed interface TtsError {
        object InitError : TtsError
        object SynthError : TtsError
        object FileError : TtsError
    }

    /**
     * TTS 引擎封装类
     *
     * 简化版本，展示了核心逻辑
     */
    class SimpleTtsEngine(private val context: Context) {

        private var tts: TextToSpeech? = null
        private var isInitialized = false

        /**
         * 初始化 TTS 引擎
         *
         * @param engineName 引擎名称，为空使用默认引擎
         * @param onComplete 初始化完成的回调
         */
        fun init(engineName: String = "", onComplete: (Boolean) -> Unit) {
            tts = TextToSpeech(context) { status ->
                isInitialized = status == TextToSpeech.SUCCESS
                if (isInitialized && engineName.isNotEmpty()) {
                    // 使用指定引擎
                    tts?.let { engine ->
                        val result = engine.setEngineByPackageName(engineName)
                        isInitialized = result == TextToSpeech.SUCCESS
                    }
                }
                onComplete(isInitialized)
            }
        }

        /**
         * 将文本转换为 WAV 文件
         *
         * 核心流程：
         * 1. 创建临时文件
         * 2. 调用 synthesizeToFile 合成到文件
         * 3. 等待合成完成
         *
         * @param text 要转换的文本
         * @param outputDir 输出目录
         * @param locale 语言（如 "zh-CN"）
         * @param speechRate 语速（1.0 正常）
         * @param pitch 音高（1.0 正常）
         * @return Result 包含文件路径或错误
         */
        suspend fun synthesizeToFile(
            text: String,
            outputDir: String,
            locale: String = "zh-CN",
            speechRate: Float = 1.0f,
            pitch: Float = 1.0f
        ): Result<String, TtsError> = suspendCancellableCoroutine { continuation ->

            if (!isInitialized) {
                continuation.resume(Err(TtsError.InitError))
                return@suspendCancellableCoroutine
            }

            val tts = this.tts ?: run {
                continuation.resume(Err(TtsError.InitError))
                return@suspendCancellableCoroutine
            }

            // 1. 设置语言和参数
            tts.language = Locale.forLanguageTag(locale)
            tts.setSpeechRate(speechRate)
            tts.setPitch(pitch)

            // 2. 创建输出文件
            val filename = "tts_${System.currentTimeMillis()}.wav"
            val outputFile = File(outputDir, filename)

            // 确保目录存在
            if (!outputFile.parentFile?.exists()!! && !outputFile.parentFile?.mkdirs()!!) {
                continuation.resume(Err(TtsError.FileError))
                return@suspendCancellableCoroutine
            }

            // 3. 设置合成完成监听器
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    // 合成开始
                }

                override fun onDone(utteranceId: String?) {
                    // 合成完成，返回文件路径
                    continuation.resume(Ok(outputFile.absolutePath))
                }

                override fun onError(utteranceId: String?) {
                    // 合成出错，删除文件
                    outputFile.delete()
                    continuation.resume(Err(TtsError.SynthError))
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                    outputFile.delete()
                    continuation.resume(Err(TtsError.SynthError))
                }
            })

            // 4. 调用 synthesizeToFile 开始合成
            val utteranceId = filename
            val result = tts.synthesizeToFile(text, null, outputFile, utteranceId)

            if (result != TextToSpeech.SUCCESS) {
                outputFile.delete()
                continuation.resume(Err(TtsError.SynthError))
            }

            // 5. 处理取消
            continuation.invokeOnCancellation {
                tts.stop()
                outputFile.delete()
            }
        }

        /**
         * 将文本转换为音频流
         *
         * 核心流程：
         * 1. 创建临时文件
         * 2. 合成到文件
         * 3. 同时读取文件数据通过管道返回
         *
         * @param text 要转换的文本
         * @param locale 语言
         * @param speechRate 语速
         * @param pitch 音高
         * @return Result 包含 InputStream 或错误
         */
        suspend fun synthesizeToStream(
            text: String,
            locale: String = "zh-CN",
            speechRate: Float = 1.0f,
            pitch: Float = 1.0f
        ): Result<PipedInputStream, TtsError> = suspendCancellableCoroutine { continuation ->

            if (!isInitialized) {
                continuation.resume(Err(TtsError.InitError))
                return@suspendCancellableCoroutine
            }

            val tts = this.tts ?: run {
                continuation.resume(Err(TtsError.InitError))
                return@suspendCancellableCoroutine
            }

            // 设置语言和参数
            tts.language = Locale.forLanguageTag(locale)
            tts.setSpeechRate(speechRate)
            tts.setPitch(pitch)

            // 创建管道
            val pipedOut = PipedOutputStream()
            val pipedIn = PipedInputStream(pipedOut)

            // 监听合成状态
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    // 开始时返回管道
                    continuation.resume(Ok(pipedIn))
                }

                override fun onDone(utteranceId: String?) {
                    try {
                        pipedOut.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(utteranceId: String?) {
                    try {
                        pipedOut.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                // 音频数据可用时写入管道
                override fun onAudioAvailable(utteranceId: String?, audio: ByteArray?) {
                    audio?.let {
                        try {
                            pipedOut.write(it)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            })

            // 开始合成（使用 speak 方法可以实现流式输出）
            val result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "stream_${System.currentTimeMillis()}")

            if (result != TextToSpeech.SUCCESS) {
                try {
                    pipedOut.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                continuation.resume(Err(TtsError.SynthError))
            }
        }

        /**
         * 直接播放
         */
        suspend fun speak(
            text: String,
            locale: String = "zh-CN",
            speechRate: Float = 1.0f,
            pitch: Float = 1.0f
        ): Result<Unit, TtsError> = suspendCancellableCoroutine { continuation ->

            if (!isInitialized) {
                continuation.resume(Err(TtsError.InitError))
                return@suspendCancellableCoroutine
            }

            val tts = this.tts ?: run {
                continuation.resume(Err(TtsError.InitError))
                return@suspendCancellableCoroutine
            }

            tts.language = Locale.forLanguageTag(locale)
            tts.setSpeechRate(speechRate)
            tts.setPitch(pitch)

            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    continuation.resume(Ok(Unit))
                }
                override fun onError(utteranceId: String?) {
                    continuation.resume(Err(TtsError.SynthError))
                }
            })

            val result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "speak_${System.currentTimeMillis()}")
            if (result != TextToSpeech.SUCCESS) {
                continuation.resume(Err(TtsError.SynthError))
            }
        }

        /**
         * 释放资源
         */
        fun shutdown() {
            tts?.shutdown()
            tts = null
            isInitialized = false
        }
    }

    /**
     * 使用示例
     */
    fun example(context: Context) {
        val engine = SimpleTtsEngine(context)

        engine.init("") { success ->
            if (success) {
                println("TTS 引擎初始化成功")

                // 在协程中使用
                CoroutineScope(Dispatchers.Main).launch {
                    // 示例1：保存为WAV文件
                    val fileResult = engine.synthesizeToFile(
                        text = "你好，这是一段测试文本",
                        outputDir = context.cacheDir.absolutePath,
                        locale = "zh-CN",
                        speechRate = 1.0f,
                        pitch = 1.0f
                    )

                    when (fileResult) {
                        is Ok -> println("WAV文件已保存: ${fileResult.value}")
                        is Err -> println("合成失败: ${fileResult.value}")
                    }

                    // 示例2：获取音频流
                    val streamResult = engine.synthesizeToStream(
                        text = "这是一段流式测试",
                        locale = "zh-CN"
                    )

                    when (streamResult) {
                        is Ok -> {
                            val inputStream = streamResult.value
                            // 读取音频数据
                            val buffer = ByteArray(1024)
                            while (true) {
                                val bytesRead = inputStream.read(buffer)
                                if (bytesRead == -1) break
                                // 处理音频数据
                            }
                        }
                        is Err -> println("流式合成失败")
                    }

                    // 示例3：直接播放
                    engine.speak("直接播放测试")

                    // 完成后释放
                    engine.shutdown()
                }
            }
        }
    }
}
