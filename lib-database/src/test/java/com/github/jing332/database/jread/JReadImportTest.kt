package com.github.jing332.database.jread

import com.github.jing332.database.entities.plugin.Plugin
import com.github.jing332.database.entities.plugin.jread.JReadPluginBundle
import com.github.jing332.database.entities.plugin.jread.JReadPluginConverter
import com.github.jing332.database.entities.systts.jread.JReadVoiceConfigBundle
import com.github.jing332.database.entities.systts.jread.JReadVoiceConfigConverter
import com.github.jing332.database.entities.systts.source.PluginTtsSource
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JReadImportTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Test
    fun pluginBundle_convertToPlugin() {
        val bundleJson = """
            {
              "format": "jread_voice_plugin_bundle",
              "version": 1,
              "plugins": [
                {
                  "id": "test.plugin.id",
                  "name": "测试插件",
                  "pluginId": "test.plugin.id",
                  "author": "测试作者",
                  "version": "20260718.v1",
                  "iconUrl": "https://example.com/icon.svg",
                  "code": "var PluginJS = {};"
                }
              ]
            }
        """.trimIndent()

        val bundle = json.decodeFromString(JReadPluginBundle.serializer(), bundleJson)
        val plugins = JReadPluginConverter.convert(bundle)

        assertEquals(1, plugins.size)
        val plugin = plugins.first()
        assertEquals("测试插件", plugin.name)
        assertEquals("test.plugin.id", plugin.pluginId)
        assertEquals("测试作者", plugin.author)
        assertEquals("https://example.com/icon.svg", plugin.iconUrl)
        assertEquals("var PluginJS = {};", plugin.code)
        assertTrue(plugin.version > 0)
    }

    @Test
    fun voiceConfigBundle_convertToSystemTts() {
        val bundleJson = """
            {
              "format": "jread_voice_config_bundle",
              "version": "1",
              "groups": [
                { "id": "g1", "groupName": "测试组", "subGroupName": "子分组" }
              ],
              "configs": [
                {
                  "id": "c1",
                  "voiceTag": "测试音色",
                  "groupName": "测试组",
                  "subGroupName": "子分组",
                  "displayName": "测试显示名",
                  "pluginId": "test.plugin.id",
                  "locale": "zh-CN",
                  "voice": "voice_001",
                  "speed": 1,
                  "volume": 1,
                  "pitch": 1,
                  "enabled": true
                }
              ]
            }
        """.trimIndent()

        val bundle = json.decodeFromString(JReadVoiceConfigBundle.serializer(), bundleJson)
        val groups = JReadVoiceConfigConverter.convert(bundle)

        assertEquals(1, groups.size)
        val groupWithTts = groups.first()
        assertEquals("测试组", groupWithTts.group.name)
        assertEquals(1, groupWithTts.list.size)

        val tts = groupWithTts.list.first()
        assertEquals("测试显示名", tts.displayName)
        assertEquals("子分组", tts.categoryPath)

        val config = tts.ttsConfig
        val source = config.source
        assertTrue(source is PluginTtsSource)
        val pluginSource = source as PluginTtsSource
        assertEquals("test.plugin.id", pluginSource.pluginId)
        assertEquals("voice_001", pluginSource.voice)
        assertEquals("zh-CN", pluginSource.locale)
    }
}
