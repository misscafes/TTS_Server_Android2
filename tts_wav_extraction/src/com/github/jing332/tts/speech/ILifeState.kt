package com.github.jing332.tts.speech

/**
 * 生命周期状态接口
 *
 * 定义 TTS 提供者需要实现的生命周期方法
 */
interface ILifeState {
    /**
     * 初始化
     */
    suspend fun onInit()

    /**
     * 停止
     */
    fun onStop()

    /**
     * 销毁
     */
    fun onDestroy()
}
