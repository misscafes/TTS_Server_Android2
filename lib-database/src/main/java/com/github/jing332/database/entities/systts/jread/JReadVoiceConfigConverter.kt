package com.github.jing332.database.entities.systts.jread

import com.github.jing332.database.entities.systts.AudioParams
import com.github.jing332.database.entities.systts.BasicAudioFormat
import com.github.jing332.database.entities.systts.GroupWithSystemTts
import com.github.jing332.database.entities.systts.SpeechRuleInfo
import com.github.jing332.database.entities.systts.SystemTtsGroup
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.database.entities.systts.source.PluginTtsSource

object JReadVoiceConfigConverter {

    fun convert(bundle: JReadVoiceConfigBundle): List<GroupWithSystemTts> {
        // 1. 收集所有分组层级：groupName -> subGroupName -> thirdGroupName
        val groupStructure = linkedMapOf<String, LinkedHashMap<String, MutableSet<String>>>()

        // 优先以 groups 字段声明的结构为准
        bundle.groups.forEach { group ->
            groupStructure
                .getOrPut(group.groupName.ifBlank { "未分组" }) { linkedMapOf() }
                .getOrPut(group.subGroupName.ifBlank { "" }) { mutableSetOf() }
                .add(group.thirdGroupName.ifBlank { "" })
        }

        // 再用 configs 中实际出现的分组补全（防止 groups 为空或不完整）
        bundle.configs.forEach { config ->
            val g = config.groupName.ifBlank { "未分组" }
            val s = config.subGroupName.ifBlank { "" }
            val t = config.thirdGroupName.ifBlank { "" }
            groupStructure
                .getOrPut(g) { linkedMapOf() }
                .getOrPut(s) { mutableSetOf() }
                .add(t)
        }

        // 2. 创建主分组和子分组对象
        val baseTime = System.currentTimeMillis()
        var order = 0

        val mainGroups = linkedMapOf<String, SystemTtsGroup>()
        val subGroups = linkedMapOf<Pair<String, String>, SystemTtsGroup>()
        val thirdGroups = linkedMapOf<Triple<String, String, String>, SystemTtsGroup>()

        groupStructure.forEach { (groupName, subMap) ->
            val mainGroup = SystemTtsGroup(
                id = baseTime + order++,
                name = groupName,
                order = order,
                parentGroupId = 0L,
            )
            mainGroups[groupName] = mainGroup

            subMap.forEach { (subGroupName, thirdSet) ->
                if (subGroupName.isNotBlank()) {
                    val subGroup = SystemTtsGroup(
                        id = baseTime + order++,
                        name = subGroupName,
                        order = order,
                        parentGroupId = mainGroup.id,
                    )
                    subGroups[groupName to subGroupName] = subGroup

                    thirdSet.forEach { thirdGroupName ->
                        if (thirdGroupName.isNotBlank()) {
                            val thirdGroup = SystemTtsGroup(
                                id = baseTime + order++,
                                name = thirdGroupName,
                                order = order,
                                parentGroupId = subGroup.id,
                            )
                            thirdGroups[Triple(groupName, subGroupName, thirdGroupName)] = thirdGroup
                        }
                    }
                }
            }
        }

        // 3. 把 configs 按分组归类
        val ttsByGroup = linkedMapOf<Long, MutableList<SystemTtsV2>>()

        bundle.configs.forEachIndexed { index, config ->
            val groupName = config.groupName.ifBlank { "未分组" }
            val subGroupName = config.subGroupName.ifBlank { "" }
            val thirdGroupName = config.thirdGroupName.ifBlank { "" }

            val group = when {
                thirdGroupName.isNotBlank() ->
                    thirdGroups[Triple(groupName, subGroupName, thirdGroupName)]

                subGroupName.isNotBlank() ->
                    subGroups[groupName to subGroupName]

                else ->
                    mainGroups[groupName]
            } ?: mainGroups.values.firstOrNull() ?: return@forEachIndexed

            val tts = SystemTtsV2(
                id = baseTime + order + index,
                groupId = group.id,
                displayName = config.displayName.ifBlank { config.voiceTag },
                categoryPath = buildCategoryPath(groupName, subGroupName, thirdGroupName),
                isEnabled = config.enabled,
                order = index,
                config = TtsConfigurationDTO(
                    speechRule = SpeechRuleInfo(),
                    audioParams = AudioParams(),
                    audioFormat = BasicAudioFormat(isNeedDecode = true),
                    source = PluginTtsSource(
                        locale = config.locale,
                        voice = config.voice,
                        pluginId = config.pluginId,
                        speed = config.speed,
                        volume = config.volume,
                        pitch = config.pitch,
                        data = config.data.toMutableMap(),
                    ),
                ),
            )
            ttsByGroup.getOrPut(group.id) { mutableListOf() }.add(tts)
        }

        // 4. 组装 GroupWithSystemTts：主分组在前，子/孙分组在后，保持创建顺序。
        // 主分组即使自身没有 config，只要其子分组有 config 也要保留，以形成树形结构。
        val keptSubGroupIds = subGroups.values.map { it.id }.filter { ttsByGroup[it]?.isNotEmpty() == true }.toSet()
        val keptThirdGroupIds = thirdGroups.values.map { it.id }.filter { ttsByGroup[it]?.isNotEmpty() == true }.toSet()
        val keptMainGroupIds = mainGroups.values
            .filter { main ->
                ttsByGroup[main.id]?.isNotEmpty() == true ||
                        subGroups.values.any { it.parentGroupId == main.id && it.id in keptSubGroupIds }
            }
            .map { it.id }
            .toSet()

        val allGroups = mutableListOf<SystemTtsGroup>()
        allGroups.addAll(mainGroups.values.filter { it.id in keptMainGroupIds })
        allGroups.addAll(subGroups.values.filter { it.id in keptSubGroupIds })
        allGroups.addAll(thirdGroups.values.filter { it.id in keptThirdGroupIds })

        return allGroups.mapNotNull { group ->
            val list = ttsByGroup[group.id] ?: emptyList()
            GroupWithSystemTts(group = group, list = list)
        }
    }

    private fun buildCategoryPath(groupName: String, subGroupName: String, thirdGroupName: String): String {
        return listOf(subGroupName, thirdGroupName)
            .filter { it.isNotBlank() }
            .joinToString("/")
    }
}
