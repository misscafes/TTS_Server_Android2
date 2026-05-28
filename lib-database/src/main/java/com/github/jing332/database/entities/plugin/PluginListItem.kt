package com.github.jing332.database.entities.plugin

data class PluginListItem(
    val id: Long,
    val isEnabled: Boolean,
    val name: String,
    val version: Int,
    val pluginId: String,
    val author: String,
    val iconUrl: String,
    val order: Int,
    val hasDefVars: Boolean,
    val needSetVars: Boolean,
)
