package com.github.jing332.tts_server_android.compose.systts

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drake.net.utils.withMain
import com.github.jing332.common.LogEntry
import com.github.jing332.common.LogLevel
import com.github.jing332.common.toLogLevel
import com.github.jing332.common.utils.runOnUI
import com.github.jing332.script.runtime.console.Console
import com.github.jing332.tts_server_android.SysttsLogger
import com.github.jing332.tts_server_android.constant.AppConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter

class TtsLogViewModel : ViewModel() {
    companion object {
        const val TAG = "TtsLogViewModel"
        const val MAX_SIZE = 150
        
        // 支持的日志级别
        val LOG_LEVELS = listOf(
            LogLevel.ERROR,
            LogLevel.WARN,
            LogLevel.INFO,
            LogLevel.DEBUG,
            LogLevel.TRACE
        )
        
        // 修改点：路径从 files/log 指向 cache/log
        // AppConst.externalFilesDir 指向 .../files，parentFile 指向 .../包名，再 resolve cache 即为 cache 目录
        val file = File(AppConst.externalFilesDir.parentFile, "cache/log/system_tts.log")
    }

    val logs = mutableStateListOf<LogEntry>()
    val searchQuery = mutableStateOf("")
    
    // 日志级别筛选（存储选中的日志级别 Int 值）
    val selectedLevels = mutableStateListOf<Int>()
    val showFilterDialog = mutableStateOf(false)
    
    // 调试模式开关 - 显示/隐藏插件日志
    val showPluginLogs = mutableStateOf(false)
    
    // 调试模式开关 - 显示/隐藏朗读规则日志
    val showSpeechRuleLogs = mutableStateOf(false)
    
    val filteredLogs: List<LogEntry>
        get() {
            var filtered = logs.toList()
            
            // 按日志级别筛选
            if (selectedLevels.isNotEmpty()) {
                filtered = filtered.filter { it.level in selectedLevels }
            }
            
            // 按搜索词筛选
            val query = searchQuery.value.trim()
            if (query.isNotEmpty()) {
                filtered = filtered.filter { log ->
                    log.message.contains(query, ignoreCase = true) ||
                    log.time.contains(query, ignoreCase = true)
                }
            }
            
            // 调试模式：控制是否显示插件日志
            if (!showPluginLogs.value) {
                filtered = filtered.filter { !it.isPluginLog }
            }
            
            // 调试模式：控制是否显示朗读规则日志
            if (!showSpeechRuleLogs.value) {
                filtered = filtered.filter { !it.isSpeechRuleLog }
            }
            
            return filtered
        }
    
    fun toggleLevel(level: Int) {
        if (level in selectedLevels) {
            selectedLevels.remove(level)
        } else {
            selectedLevels.add(level)
        }
    }
    
    fun clearFilter() {
        selectedLevels.clear()
    }

    fun clear() {
        logs.clear()
        runCatching {
            FileWriter(file, false).use { it.write(CharArray(0)) }
        }.onFailure {
            logs.add(LogEntry(level = LogLevel.ERROR, message = it.stackTraceToString()))
            Log.e(TAG, "clear: ", it) 
        }
    }


    private fun toLogEntry(line: String): LogEntry {
        return line.split(" | ").let {
            val time = it[0]
            val level = it[1]
            val msg = it[2]
            LogEntry(
                level = level.toLogLevel(), time = time, message = msg
            )
        }
    }

    fun logDir(): String {
        return file.absolutePath
    }

    init {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                pull()
                SysttsLogger.register({ log ->
                    runOnUI {
                        logs.add(log)
                    }
                })
                
                // 注册插件日志监听器
                Console.globalPluginLogListener = { logEntry ->
                    runOnUI {
                        if (logs.size > MAX_SIZE)
                            logs.removeRange(0, 10)
                        logs.add(logEntry)
                    }
                }
                
                // 注册朗读规则日志监听器
                Console.globalSpeechRuleLogListener = { logEntry ->
                    runOnUI {
                        if (logs.size > MAX_SIZE)
                            logs.removeRange(0, 10)
                        logs.add(logEntry)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "init: ", e)
        }
    }

    fun add(line: String) {
        try {
            val logEntry = toLogEntry(line)
            if (logs.size > MAX_SIZE)
                logs.removeRange(0, 10)
            logs.add(logEntry)

        } catch (e: Exception) {
            Log.e(TAG, "add: ", e) 
        }
    }

    @Suppress("DEPRECATION")
    suspend fun pull() {
        runCatching {
            if (file.exists()) {
                file.readLines().takeLast(MAX_SIZE).apply {
                    withMain {
                        forEach { add(it) }
                    }
                }
            }
        }.onFailure {
            logs.add(LogEntry(level = LogLevel.ERROR, message = it.stackTraceToString()))
            Log.e(TAG, "pull: ", it) 
        }

    }
}
