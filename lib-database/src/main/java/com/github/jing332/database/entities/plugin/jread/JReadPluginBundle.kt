package com.github.jing332.database.entities.plugin.jread

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * JRead 阅读器导出的插件包（jread_voice_plugin_bundle）。
 */
@Serializable
@SerialName("jread_voice_plugin_bundle")
data class JReadPluginBundle(
    val format: String = FORMAT,
    val version: Int = 1,
    val plugins: List<JReadPlugin> = emptyList(),
) {
    companion object {
        const val FORMAT = "jread_voice_plugin_bundle"
    }
}

@Serializable
data class JReadPlugin(
    val id: String = "",
    val name: String = "",
    @SerialName("pluginId")
    val pluginId: String = "",
    @SerialName("pluginGroupId")
    val pluginGroupId: String = "",
    @SerialName("pluginGroupName")
    val pluginGroupName: String = "",
    val author: String = "",
    val version: String = "1",
    val streaming: Map<String, String> = emptyMap(),
    val iconUrl: String = "",
    val code: String = "",
    /**
     * JRead 变量声明，结构为 { key: { name: "显示名", hint: "提示", binding: "..." } }。
     * 导入 TTS Server 时需把 name 映射为 label。
     */
    val defVars: Map<String, JReadPluginVar> = emptyMap(),
    val userVars: Map<String, String> = emptyMap(),
    val enabled: Boolean = false,
    val method: String = "GET",
    val urlTemplate: String = "",
    val headersText: String = "",
    val bodyTemplate: String = "",
    val responseAudioPath: String = "",
)

@Serializable
data class JReadPluginVar(
    val name: String = "",
    val hint: String = "",
    val binding: String = "",
)
