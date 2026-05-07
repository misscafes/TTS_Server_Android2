package com.github.jing332.tts.speech

/**
 * 引擎状态
 *
 * 用于跟踪 TTS 引擎的当前状态
 */
sealed class EngineState {

    /** 未初始化状态 */
    data class Uninitialized(val reason: Throwable? = null) : EngineState()

    /** 初始化中状态 */
    data object Initializing : EngineState()

    /** 已初始化状态 */
    data object Initialized : EngineState()
}
