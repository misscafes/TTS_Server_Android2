package com.github.jing332.tts_server_android.compose.systts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.systts.SystemTtsV2
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