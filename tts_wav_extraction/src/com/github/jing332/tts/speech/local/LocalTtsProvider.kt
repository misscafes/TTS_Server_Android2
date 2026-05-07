package com.github.jing332.tts.speech.local

import android.content.Context
import com.github.jing332.database.entities.systts.AudioParams
import com.github.jing332.database.entities.systts.source.LocalTtsSource
import com.github.jing332.tts.exception.EngineException
import com.github.jing332.tts.speech.EngineState
import com.github.jing332.tts.speech.TextToSpeechProvider
import com.github.jing332.tts.synthesizer.SystemParams
import com.github.michaelbull.result.onFailure
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.InputStream

/**
 * 本地 TTS 提供者
 *
 * 这是一个核心类，负责：
 * 1. 封装 AndroidTtsEngine
 * 2. 实现 getStream() 方法用于中转模式（保存WAV）
 * 3. 实现 syncPlay() 方法用于直接播放模式
 * 4. 根据 isDirectPlayMode 决定使用哪种模式
 */
class LocalTtsProvider(
    private val context: Context,
    private val engine: String,
) : TextToSpeechProvider<LocalTtsSource>() {
    companion object {
        const val TAG = "LocalTtsService"
        private val logger = KotlinLogging.logger(TAG)
    }

    // Android TTS 引擎实例
    private var mTts: AndroidTtsEngine? = null

    // 懒加载获取引擎
    private val tts: AndroidTtsEngine
        get() = mTts ?: throw EngineException("Android TTS engine is not initialized")


    /**
     * 初始化音频参数
     *
     * 如果用户配置的值为 FOLLOW，则使用系统参数
     * 否则使用用户配置的值
     *
     * @param source 本地TTS配置
     * @param params 系统参数
     * @return 初始化后的音频参数
     */
    private fun init(source: LocalTtsSource, params: SystemParams): AudioParams {
        // 语速：如果配置为跟随系统，则使用系统参数
        val speed = if (source.speed == LocalTtsSource.SPEED_FOLLOW) params.speed else source.speed

        // 音高：如果配置为跟随系统，则使用系统参数
        val pitch = if (source.pitch == LocalTtsSource.PITCH_FOLLOW) params.pitch else source.pitch

        // 音量：如果配置为跟随系统，则使用系统参数
        val volume =
            if (source.volume == LocalTtsSource.VOLUME_FOLLOW) params.volume else source.volume

        return AudioParams(speed = speed, pitch = pitch, volume = volume)
    }


    /**
     * 获取音频流（中转模式的核心方法）
     *
     * 当 isDirectPlayMode = false 时调用此方法
     * 该方法会：
     * 1. 调用 AndroidTtsEngine.getStream()
     * 2. 生成临时WAV文件
     * 3. 通过管道返回音频流
     *
     * @param params 系统参数（包含文本）
     * @param source 本地TTS配置
     * @return InputStream 音频流
     */
    override suspend fun getStream(params: SystemParams, source: LocalTtsSource): InputStream {
        return tts.getStream(
            params.text,
            locale = source.locale,
            voice = source.voice,
            extraParams = source.extraParams ?: emptyList(),
            init(source, params)
        ).onFailure {
            // 如果失败，销毁并重新初始化引擎
            onDestroy()
            onInit()
            throw EngineException(it.toString())
        }.value
    }

    /**
     * 同步播放（直接播放模式的核心方法）
     *
     * 当 isDirectPlayMode = true 时调用此方法
     * 该方法会直接调用 TTS 引擎播放，不保存文件
     *
     * @param params 系统参数（包含文本）
     * @param source 本地TTS配置
     */
    override suspend fun syncPlay(params: SystemParams, source: LocalTtsSource) {
        tts.play(
            params.text,
            locale = source.locale,
            voice = source.voice,
            extraParams = source.extraParams ?: emptyList(),
            params = init(source, params)
        ).onFailure {
            // 如果失败，销毁并重新初始化引擎
            onDestroy()
            onInit()
        }
    }

    /**
     * 引擎状态
     */
    override var state: EngineState = EngineState.Uninitialized()

    /**
     * 是否为直接播放模式
     *
     * 这是关键的判断方法：
     * - 返回 true：使用 syncPlay() 直接播放
     * - 返回 false：使用 getStream() 中转音频
     *
     * @param source 本地TTS配置
     * @return 是否直接播放
     */
    override fun isSyncPlay(source: LocalTtsSource): Boolean {
        return source.isDirectPlayMode
    }


    /**
     * 初始化引擎
     */
    override suspend fun onInit() {
        mTts = AndroidTtsEngine(context).apply { init(engine) }
        state = EngineState.Initialized
    }

    /**
     * 停止播放
     */
    override fun onStop() {
    }

    /**
     * 销毁引擎
     */
    override fun onDestroy() {
        mTts?.release()
        mTts = null
    }
}
