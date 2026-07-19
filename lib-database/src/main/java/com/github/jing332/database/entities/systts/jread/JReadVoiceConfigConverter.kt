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
        // 按 (groupName, subGroupName, thirdGroupName) 分组
        val grouped = bundle.configs.groupBy {
            listOf(it.groupName, it.subGroupName, it.thirdGroupName)
                .filter { name -> name.isNotBlank() }
        }

        var groupOrder = 0
        return grouped.map { (pathParts, configs) ->
            val groupId = System.currentTimeMillis() + groupOrder
            val groupName = pathParts.firstOrNull()?.ifBlank { "未分组" } ?: "未分组"
            val categoryPath = pathParts.drop(1).joinToString("/")

            val group = SystemTtsGroup(
                id = groupId,
                name = groupName,
                order = groupOrder++,
            )

            val ttsList = configs.mapIndexed { index, config ->
                SystemTtsV2(
                    id = groupId + index + 1,
                    groupId = groupId,
                    displayName = config.displayName.ifBlank { config.voiceTag },
                    categoryPath = categoryPath,
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
            }

            GroupWithSystemTts(group = group, list = ttsList)
        }
    }
}
