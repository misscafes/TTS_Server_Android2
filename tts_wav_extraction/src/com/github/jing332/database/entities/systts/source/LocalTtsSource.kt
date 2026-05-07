package com.github.jing332.database.entities.systts.source

import com.github.jing332.database.entities.systts.BasicAudioFormat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 本地 TTS 数据源配置
 *
 * 存储本地 TTS 的所有配置信息
 */
@Serializable
@SerialName("local")
data class LocalTtsSource(
    /** TTS 引擎包名或名称 */
    val engine: String = "",

    /** 语言标签（如 "zh-CN"） */
    override val locale: String = "",

    /** 声音名称 */
    override val voice: String = "",

    /** 语速：0f 表示跟随系统 */
    val speed: Float = SPEED_FOLLOW,

    /** 音高：0f 表示跟随系统 */
    val pitch: Float = PITCH_FOLLOW,

    /** 音量：0f 表示跟随系统 */
    val volume: Float = VOLUME_FOLLOW,

    /** 额外参数列表 */
    val extraParams: MutableList<LocalTtsParameter>? = null,

    /**
     * 是否直接播放模式
     *
     * - true：直接播放，不保存文件
     * - false：中转模式，先保存WAV再播放
     *
     * 默认值为 true
     */
    val isDirectPlayMode: Boolean = true,
) : TextToSpeechSource() {

    companion object {
        /** 跟随系统语速 */
        const val SPEED_FOLLOW = 0f

        /** 跟随系统音高 */
        const val PITCH_FOLLOW = 0f

        /** 跟随系统音量 */
        const val VOLUME_FOLLOW = 0f
    }

    /**
     * 获取引擎唯一标识
     */
    override fun getKey(): String = engine

    /**
     * 是否为同步播放模式
     * 与 isDirectPlayMode 关联
     */
    override fun isSyncPlayMode(): Boolean = isDirectPlayMode

    /**
     * 是否需要解码
     *
     * 直接播放模式下：
     * - true：需要解码
     * - false：不需要解码
     *
     * 中转模式下：不需要解码
     */
    override fun shouldDecode(format: BasicAudioFormat): Boolean {
        return if (!isDirectPlayMode)
            false
        else
            super.shouldDecode(format)
    }
}
