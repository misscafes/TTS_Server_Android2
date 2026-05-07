package com.github.jing332.tts.synthesizer

/**
 * 系统参数
 *
 * 用于传递 TTS 转换所需的系统级参数
 */
data class SystemParams(
    /** 要转换的文本 */
    val text: String = "",

    /** 语速，1.0 为正常速度 */
    val speed: Float = 1f,

    /** 音量，1.0 为正常音量 */
    val volume: Float = 1f,

    /** 音高，1.0 为正常音高 */
    val pitch: Float = 1f
)
