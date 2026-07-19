package com.github.jing332.tts_server_android.compose.systts.plugin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.jing332.tts_server_android.compose.systts.ConfigImportBottomSheet
import com.github.jing332.tts_server_android.compose.systts.ConfigModel
import com.github.jing332.tts_server_android.compose.systts.SelectImportConfigDialog
import com.github.jing332.common.utils.toJsonListString
import com.github.jing332.tts_server_android.constant.AppConst
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.plugin.Plugin
import com.github.jing332.database.entities.plugin.jread.JReadPluginBundle
import com.github.jing332.database.entities.plugin.jread.JReadPluginConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun PluginImportBottomSheet(onDismissRequest: () -> Unit) {
    var list by remember { mutableStateOf<List<Plugin>?>(null) }
    if (list != null) {
        SelectImportConfigDialog(
            onDismissRequest = { list = null },
            models = list!!.map {
                ConfigModel(
                    isSelected = true,
                    title = it.name,
                    subtitle = it.author,
                    it
                )
            },
            onSelectedList = {
                withContext(Dispatchers.IO) {
                    dbm.pluginDao.insert(*it.map { plugin -> plugin as Plugin }.toTypedArray())
                }
                it.size
            }
        )
    }

    ConfigImportBottomSheet(
        onDismissRequest = onDismissRequest,
        autoWrapJsonList = false,
        onImport = { json ->
            val decoded = withContext(Dispatchers.IO) {
                val trimmed = json.trim()
                val element = runCatching {
                    AppConst.jsonBuilder.parseToJsonElement(trimmed)
                }.getOrNull()
                val format = element?.jsonObject?.get("format")?.jsonPrimitive?.content
                if (format == JReadPluginBundle.FORMAT) {
                    val bundle = AppConst.jsonBuilder.decodeFromJsonElement(
                        JReadPluginBundle.serializer(),
                        element
                    )
                    JReadPluginConverter.convert(bundle)
                } else {
                    AppConst.jsonBuilder.decodeFromString<List<Plugin>>(trimmed.toJsonListString())
                }
            }
            list = decoded
        }
    )
}
