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
)
