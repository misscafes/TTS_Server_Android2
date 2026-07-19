package com.github.jing332.database.entities.plugin.jread

import com.github.jing332.database.entities.plugin.Plugin

object JReadPluginConverter {

    fun convert(bundle: JReadPluginBundle): List<Plugin> {
        return bundle.plugins.map { convert(it) }
    }

    fun convert(plugin: JReadPlugin): Plugin {
        return Plugin(
            name = plugin.name.ifBlank { plugin.pluginId },
            pluginId = plugin.pluginId.ifBlank { plugin.id },
            author = plugin.author,
            iconUrl = plugin.iconUrl,
            code = plugin.code,
            version = parseVersion(plugin.version),
        )
    }

    /**
     * JRead 的 version 通常是字符串（如 "20260718.unified-2852..."），
     * 而 TTS Server 的 Plugin.version 是 Int。优先取纯数字前缀，
     * 无法解析时回退为字符串 hashCode 的绝对值，避免冲突。
     */
    private fun parseVersion(version: String): Int {
        if (version.isBlank()) return 1
        val digits = version.takeWhile { it.isDigit() }
        return digits.toIntOrNull()?.coerceAtLeast(1)
            ?: kotlin.math.abs(version.hashCode()).coerceAtLeast(1)
    }
}
