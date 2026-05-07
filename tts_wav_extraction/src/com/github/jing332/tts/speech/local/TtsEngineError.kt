package com.github.jing332.tts.speech.local

/**
 * TTS 引擎错误类型
 */
sealed interface TtsEngineError {
    /** 引擎未初始化 */
    object Initialization : TtsEngineError

    /** 引擎执行错误 */
    object Engine : TtsEngineError

    /** 文件操作错误 */
    object File : TtsEngineError
}
