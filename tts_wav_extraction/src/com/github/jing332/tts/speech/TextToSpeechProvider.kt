package com.github.jing332.tts.speech

import com.github.jing332.database.entities.systts.source.TextToSpeechSource
import com.github.jing332.tts.synthesizer.SystemParams
import java.io.InputStream

/**
 * 文本转语音提供者基类
 *
 * 这是一个抽象基类，定义了 TTS 提供者的标准接口
 *
 * @param T 泛型参数，继承自 TextToSpeechSource
 */
abstract class TextToSpeechProvider<in T : TextToSpeechSource> : ILifeState {

    /**
     * 引擎状态
     */
    abstract var state: EngineState

    /**
     * 是否为同步播放模式
     *
     * 子类可以重写此方法来判断是否使用直接播放模式
     *
     * @param source TTS 配置
     * @return true 使用 syncPlay 直接播放，false 使用 getStream 获取音频流
     */
    open fun isSyncPlay(source: T): Boolean {
        return false
    }

    override fun onStop() {
    }

    /**
     * 获取音频流
     *
     * 这是核心方法，用于获取 TTS 转换后的音频数据
     *
     * @param params 系统参数（包含要转换的文本）
     * @param source TTS 配置
     * @return InputStream 音频流
     */
    abstract suspend fun getStream(params: SystemParams, source: T): InputStream

    /**
     * 同步播放
     *
     * 当 isSyncPlay 返回 true 时调用此方法
     * 直接播放语音，不返回音频流
     *
     * @param params 系统参数（包含要转换的文本）
     * @param source TTS 配置
     */
    open suspend fun syncPlay(params: SystemParams, source: T) {
        TODO("not yet implemented")
    }
}
