package com.github.jing332.tts_server_android.compose.systts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.source.LocalTtsSource
import com.github.jing332.database.entities.systts.source.PluginTtsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupEditContentViewModel : ViewModel() {
    private val _availableConfigs = MutableStateFlow<List<SystemTtsV2>>(emptyList())
    val availableConfigs: StateFlow<List<SystemTtsV2>> = _availableConfigs.asStateFlow()
    
    private var currentGroupId: Long = 0
    private var allConfigs: List<SystemTtsV2> = emptyList()
    
    fun load(groupId: Long) {
        viewModelScope.launch {
            currentGroupId = groupId
            withContext(Dispatchers.IO) {
                allConfigs = dbm.systemTtsV2.all
                // 获取不在当前分组的配置
                _availableConfigs.value = allConfigs.filter { it.groupId != groupId }
            }
        }
    }
    
    fun filterConfigs(
        configs: List<SystemTtsV2>,
        query: String,
        searchType: SearchType
    ): List<SystemTtsV2> {
        if (query.isBlank()) return configs
        
        return configs.filter { config ->
            when (searchType) {
                SearchType.NAME -> {
                    config.displayName.contains(query, ignoreCase = true)
                }
                SearchType.TAG -> {
                    val speechRule = config.ttsConfig.speechRule
                    speechRule.tagName.contains(query, ignoreCase = true) ||
                    speechRule.tag.contains(query, ignoreCase = true) ||
                    speechRule.tagData.values.any { it.contains(query, ignoreCase = true) }
                }
                SearchType.PLUGIN -> {
                    when (val source = config.ttsConfig.source) {
                        is PluginTtsSource -> 
                            source.pluginId.contains(query, ignoreCase = true) ||
                            source.plugin?.name?.contains(query, ignoreCase = true) == true
                        is LocalTtsSource -> 
                            "本地".contains(query, ignoreCase = true) || 
                            "local".contains(query, ignoreCase = true)
                        else -> false
                    }
                }
            }
        }
    }
    
    suspend fun moveConfigsToGroup(configs: List<SystemTtsV2>) {
        withContext(Dispatchers.IO) {
            configs.forEach { config ->
                val updatedConfig = config.copy(groupId = currentGroupId)
                dbm.systemTtsV2.update(updatedConfig)
            }
        }
    }
    
    fun refresh() {
        load(currentGroupId)
    }
}