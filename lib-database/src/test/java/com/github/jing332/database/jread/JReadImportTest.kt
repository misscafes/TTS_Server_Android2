package com.github.jing332.database.jread

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
                  "code": "var PluginJS = {};",
                  "enabled": true,
                  "defVars": {
                    "previewEmotion": {
                      "name": "预览情绪",
                      "hint": "填写情绪名称",
                      "binding": "user_input"
                    }
                  },
                  "userVars": {
                    "previewEmotion": "高兴"
                  }
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
        assertTrue(plugin.isEnabled)
        assertTrue(plugin.version > 0)

        // defVars 的 name 应映射为 label
        assertEquals("预览情绪", plugin.defVars["previewEmotion"]?.get("label"))
        assertEquals("填写情绪名称", plugin.defVars["previewEmotion"]?.get("hint"))
        assertEquals("高兴", plugin.userVars["previewEmotion"])
    }

    @Test
    fun voiceConfigBundle_convertToSystemTts_withSubGroups() {
        val bundleJson = """
            {
              "format": "jread_voice_config_bundle",
              "version": "1",
              "groups": [
                { "id": "g1", "groupName": "火山豆包", "subGroupName": "女性青年通用" },
                { "id": "g2", "groupName": "火山豆包", "subGroupName": "男性青年通用" }
              ],
              "configs": [
                {
                  "id": "c1",
                  "voiceTag": "音色01",
                  "groupName": "火山豆包",
                  "subGroupName": "女性青年通用",
                  "displayName": "火山豆包·音色01",
                  "pluginId": "test.plugin.id",
                  "locale": "zh-CN",
                  "voice": "voice_001",
                  "speed": 1,
                  "volume": 1,
                  "pitch": 1,
                  "enabled": true
                },
                {
                  "id": "c2",
                  "voiceTag": "音色02",
                  "groupName": "火山豆包",
                  "subGroupName": "男性青年通用",
                  "displayName": "火山豆包·音色02",
                  "pluginId": "test.plugin.id",
                  "locale": "zh-CN",
                  "voice": "voice_002",
                  "speed": 1,
                  "volume": 1,
                  "pitch": 1,
                  "enabled": false
                }
              ]
            }
        """.trimIndent()

        val bundle = json.decodeFromString(JReadVoiceConfigBundle.serializer(), bundleJson)
        val groups = JReadVoiceConfigConverter.convert(bundle)

        // 应生成 1 个主分组 + 2 个子分组，但子分组有才返回
        val mainGroup = groups.find { it.group.name == "火山豆包" && it.group.parentGroupId == 0L }
        assertTrue("主分组应存在", mainGroup != null)

        val femaleGroup = groups.find { it.group.name == "女性青年通用" }
        val maleGroup = groups.find { it.group.name == "男性青年通用" }
        assertTrue("女性青年通用子分组应存在", femaleGroup != null)
        assertTrue("男性青年通用子分组应存在", maleGroup != null)

        assertEquals(mainGroup!!.group.id, femaleGroup!!.group.parentGroupId)
        assertEquals(mainGroup.group.id, maleGroup!!.group.parentGroupId)

        assertEquals(1, femaleGroup.list.size)
        assertEquals("火山豆包·音色01", femaleGroup.list.first().displayName)
        assertEquals("女性青年通用", femaleGroup.list.first().categoryPath)

        assertEquals(1, maleGroup.list.size)
        assertEquals("火山豆包·音色02", maleGroup.list.first().displayName)
        assertEquals("男性青年通用", maleGroup.list.first().categoryPath)

        val pluginSource = femaleGroup.list.first().ttsConfig.source as PluginTtsSource
        assertEquals("voice_001", pluginSource.voice)
    }

    @Test
    fun voiceConfigBundle_convertToSystemTts_withoutGroups() {
        val bundleJson = """
            {
              "format": "jread_voice_config_bundle",
              "version": "1",
              "configs": [
                {
                  "id": "c1",
                  "voiceTag": "音色01",
                  "groupName": "默认组",
                  "displayName": "默认音色",
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
        assertEquals("默认组", groups.first().group.name)
        assertEquals(0L, groups.first().group.parentGroupId)
        assertEquals("", groups.first().list.first().categoryPath)
    }
}
