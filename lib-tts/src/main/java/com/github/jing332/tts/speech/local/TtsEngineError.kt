package com.github.jing332.tts.speech.local

sealed interface TtsEngineError {
    data object Initialization : TtsEngineError
    data class Engine(val errorCode: Int? = null) : TtsEngineError
    data object File : TtsEngineError
}
