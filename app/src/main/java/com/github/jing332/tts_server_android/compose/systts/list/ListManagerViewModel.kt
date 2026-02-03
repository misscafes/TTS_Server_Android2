package com.github.jing332.tts_server_android.compose.systts.list

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.AbstractListGroup.Companion.DEFAULT_GROUP_ID
import com.github.jing332.database.entities.systts.GroupWithSystemTts
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.database.entities.systts.source.LocalTtsSource
import com.github.jing332.database.entities.systts.source.PluginTtsSource
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.conf.SystemTtsConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ItemPosition
import java.util.Collections

class ListManagerViewModel : ViewModel() {
    companion object {
        const val TAG = "ListManagerViewModel"
    }

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> get() = _keyword

    private val _searchType = MutableStateFlow(SearchType.NAME)
    val searchType: StateFlow<SearchType> get() = _searchType

    private val _list = MutableStateFlow<List<GroupWithSystemTts>>(emptyList())
    val list: StateFlow<List<GroupWithSystemTts>> get() = _list

    // 缓存插件名称
    private val pluginNameCache = MutableStateFlow<Map<String, String>>(emptyMap())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // 加载插件名称缓存
            val plugins = dbm.pluginDao.all
            pluginNameCache.value = plugins.associate { it.pluginId to it.name }
            
            dbm.systemTtsV2.updateAllOrder()
            
            dbm.systemTtsV2.flowAllGroupWithTts().conflate()
                .combine(_keyword) { list, key -> Pair(list, key) }
                .combine(_searchType) { pair, type -> Triple(pair.first, pair.second, type) }
                .collect { (list, key, searchType) ->
                    val result = if (key.isBlank()) {
                        list
                    } else {
                        filterList(list, key, searchType)
                    }
                    Log.d(TAG, "update list: ${result.size}")
                    _list.value = result
                }
        }
    }

    private fun filterList(
        list: List<GroupWithSystemTts>,
        key: String,
        searchType: SearchType
    ): List<GroupWithSystemTts> {
        return when (searchType) {
            SearchType.NAME -> {
                list.mapNotNull { groupWithTts ->
                    val filteredItems = groupWithTts.list.filter {
                        it.displayName.contains(key, ignoreCase = true)
                    }
                    if (filteredItems.isNotEmpty()) {
                        groupWithTts.copy(
                            list = filteredItems,
                            group = groupWithTts.group.copy(isExpanded = true)
                        )
                    } else null
                }
            }
            SearchType.TAG -> {
                list.mapNotNull { groupWithTts ->
                    val filteredItems = groupWithTts.list.filter { item ->
                        val ttsConfig = item.config as? TtsConfigurationDTO
                        if (ttsConfig != null) {
                            val speechRule = ttsConfig.speechRule
                            speechRule.tagName.contains(key, ignoreCase = true) ||
                            speechRule.tag.contains(key, ignoreCase = true) ||
                            speechRule.tagData.values.any { it.contains(key, ignoreCase = true) }
                        } else false
                    }
                    if (filteredItems.isNotEmpty()) {
                        groupWithTts.copy(
                            list = filteredItems,
                            group = groupWithTts.group.copy(isExpanded = true)
                        )
                    } else null
                }
            }
            SearchType.PLUGIN -> {
                list.mapNotNull { groupWithTts ->
                    val filteredItems = groupWithTts.list.filter { item ->
                        val ttsConfig = item.config as? TtsConfigurationDTO
                        if (ttsConfig != null) {
                            when (val source = ttsConfig.source) {
                                is PluginTtsSource -> {
                                    val pluginName = pluginNameCache.value[source.pluginId] ?: source.pluginId
                                    source.pluginId.contains(key, ignoreCase = true) ||
                                    pluginName.contains(key, ignoreCase = true)
                                }
                                is LocalTtsSource ->
                                    "本地".contains(key, ignoreCase = true) ||
                                    "local".contains(key, ignoreCase = true)
                                else -> false
                            }
                        } else false
                    }
                    if (filteredItems.isNotEmpty()) {
                        groupWithTts.copy(
                            list = filteredItems,
                            group = groupWithTts.group.copy(isExpanded = true)
                        )
                    } else null
                }
            }
            SearchType.GROUP -> {
                list.filter {
                    it.group.name.contains(key, ignoreCase = true)
                }.map {
                    it.copy(group = it.group.copy(isExpanded = true))
                }
            }
        }
    }

    fun setSearchKeyword(key: String) {
        _keyword.value = key
    }

    fun setSearchType(type: SearchType) {
        _searchType.value = type
    }

    fun updateTtsEnabled(
        item: SystemTtsV2,
        enabled: Boolean,
    ) {
        if (!SystemTtsConfig.isVoiceMultipleEnabled.value && enabled) {
            val itemConfig = (item.config as? TtsConfigurationDTO)
            if (itemConfig != null)
                dbm.systemTtsV2.allEnabled.forEach { systts ->
                    if (systts.config is TtsConfigurationDTO) {
                        val config = systts.config as TtsConfigurationDTO
                        if (config.speechRule.target == itemConfig.speechRule.target) {
                            if (config.speechRule.tagRuleId == itemConfig.speechRule.tagRuleId
                                && config.speechRule.tag == itemConfig.speechRule.tag
                                && config.speechRule.tagName == itemConfig.speechRule.tagName
                                && config.speechRule.isStandby == itemConfig.speechRule.isStandby
                            )
                                dbm.systemTtsV2.update(systts.copy(isEnabled = false))
                        }
                    }
                }
        }

        dbm.systemTtsV2.update(item.copy(isEnabled = enabled))
    }

    fun updateGroupEnable(
        item: GroupWithSystemTts,
        enabled: Boolean,
    ) {
        if (!SystemTtsConfig.isGroupMultipleEnabled.value && enabled) {
            list.value.forEach {
                it.list.forEach { systts ->
                    if (systts.isEnabled)
                        dbm.systemTtsV2.update(systts.copy(isEnabled = false))
                }
            }
        }

        dbm.systemTtsV2.update(
            *item.list.filter { it.isEnabled != enabled }.map { it.copy(isEnabled = enabled) }
                .toTypedArray()
        )
    }

    fun reorder(from: ItemPosition, to: ItemPosition) {
        if (_keyword.value.isNotEmpty()) return

        if (from.key is String && to.key is String) {
            val fromKey = from.key as String
            val toKey = to.key as String

            if (fromKey.startsWith("g") && toKey.startsWith("g")) {
                val mList = list.value.map { it.group }.toMutableList()

                val fromId = fromKey.substring(2).toLong()
                val fromIndex = mList.indexOfFirst { it.id == fromId }

                val toId = toKey.substring(2).toLong()
                val toIndex = mList.indexOfFirst { it.id == toId }

                try {
                    Collections.swap(mList, fromIndex, toIndex)
                } catch (_: IndexOutOfBoundsException) {
                    return
                }
                mList.forEachIndexed { index, systemTtsGroup ->
                    if (systemTtsGroup.order != index)
                        dbm.systemTtsV2.updateGroup(systemTtsGroup.copy(order = index))
                }
            } else if (!fromKey.startsWith("g") && !toKey.startsWith("g")) {
                val (fromGId, fromId) = fromKey.split("_").map { it.toLong() }
                val (toGId, toId) = toKey.split("_").map { it.toLong() }
                if (fromGId != toGId) return

                val listInGroup = findListInGroup(fromGId).toMutableList()
                val fromIndex = listInGroup.indexOfFirst { it.id == fromId }
                val toIndex = listInGroup.indexOfFirst { it.id == toId }
                Log.d(TAG, "fromIndex: $fromIndex, toIndex: $toIndex")

                try {
                    Collections.swap(listInGroup, fromIndex, toIndex)
                } catch (_: IndexOutOfBoundsException) {
                    return
                }

                listInGroup.forEachIndexed { index, systts ->
                    Log.d(TAG, "$index ${systts.displayName}")
                    if (systts.order != index)
                        dbm.systemTtsV2.update(systts.copy(order = index))
                }
            }

        }
    }

    private fun findListInGroup(groupId: Long): List<SystemTtsV2> {
        return list.value.find { it.group.id == groupId }?.list?.sortedBy { it.order }
            ?: emptyList()
    }

    fun checkListData(context: Context) {
        dbm.systemTtsV2.getGroup(DEFAULT_GROUP_ID) ?: kotlin.run {
            dbm.systemTtsV2.insertGroup(
                com.github.jing332.database.entities.systts.SystemTtsGroup(
                    DEFAULT_GROUP_ID,
                    context.getString(R.string.default_group),
                    dbm.systemTtsV2.groupCount
                )
            )
        }
    }
}
