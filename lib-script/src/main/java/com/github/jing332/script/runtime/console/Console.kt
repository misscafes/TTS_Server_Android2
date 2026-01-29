package com.github.jing332.script.runtime.console

import android.util.Log // 👈 使用原生 Log
import com.github.jing332.common.LogEntry
import com.github.jing332.common.LogLevel

class Console : LogListenerManager, Writeable {
    companion object {
        private const val TAG = "JS-Console"
        
        // 全局插件日志监听器，由 app 模块设置
        var globalPluginLogListener: ((LogEntry) -> Unit)? = null
    }

    private val listeners = mutableListOf<LogListener>()

    @Synchronized
    override fun addLogListener(listener: LogListener) {
        listeners.add(listener)
    }

    @Synchronized
    override fun removeLogListener(listener: LogListener) {
        listeners.remove(listener)
    }

    override fun write(@LogLevel level: Int, str: String) {
        // 👈 使用原生 Log.i，绕过损坏的 Logback 框架
        Log.i(TAG, str)
        
        // 同时输出到应用日志系统，标记为插件日志
        val logEntry = LogEntry(
            level = level,
            message = "[Plugin] $str",
            isPluginLog = true
        )
        
        // 通知全局监听器
        globalPluginLogListener?.invoke(logEntry)
        
        listeners.forEach {
            it.onNewLog(logEntry)
        }
    }

    fun println(str: String?) = write(LogLevel.INFO, str ?: "null")
    fun debug(str: String?) = write(LogLevel.DEBUG, str ?: "null")
    fun info(str: String?) = write(LogLevel.INFO, str ?: "null")
    fun warn(str: String?) = write(LogLevel.WARN, str ?: "null")
    fun error(str: String?) = write(LogLevel.ERROR, str ?: "null")
}
