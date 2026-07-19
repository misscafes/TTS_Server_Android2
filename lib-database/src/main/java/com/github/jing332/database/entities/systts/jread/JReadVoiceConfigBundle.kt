package com.github.jing332.database.entities.systts.jread

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * JRead 阅读器导出的音色配置包（jread_voice_config_bundle）。
 */
@Serializable
@SerialName("jread_voice_config_bundle")
data class JReadVoiceConfigBundle(
    val format: String = FORMAT,
    val version: String = "1",
    val groups: List<JReadVoiceGroup> = emptyList(),
    val configs: List<JReadVoiceConfig> = emptyList(),
    val description: String = "",
) {
    companion object {
        const val FORMAT = "jread_voice_config_bundle"
    }
}

@Serializable
data class JReadVoiceGroup(
    val id: String = "",
    val groupName: String = "",
    val subGroupName: String = "",
    val thirdGroupName: String = "",
    val displayName: String = "",
)

@Serializable
data class JReadVoiceConfig(
    val id: String = "",
    val voiceTag: String = "",
    val groupName: String = "",
    val subGroupName: String = "",
    val thirdGroupName: String = "",
    val displayName: String = "",
    val pluginId: String = "",
    val locale: String = "",
    val voice: String = "",
    val previewText: String = "",
    val data: Map<String, String> = emptyMap(),
    val speed: Float = 1f,
    val volume: Float = 1f,
    val pitch: Float = 1f,
    val method: String = "GET",
    val urlTemplate: String = "",
    val headersText: String = "",
    val bodyTemplate: String = "",
    val responseAudioPath: String = "",
    val enabled: Boolean = false,
)
