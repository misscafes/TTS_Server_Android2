package com.github.jing332.tts_server_android.compose.systts.plugin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.github.jing332.compose.widgets.AppSpinner
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.plugin.Plugin
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.database.entities.systts.source.PluginTtsSource
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.service.systts.SystemTtsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchReplacePluginDialog(
    sourcePlugin: Plugin,
    onDismissRequest: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var allPlugins by remember { mutableStateOf<List<Plugin>>(emptyList()) }
    var targetPluginId by remember { mutableStateOf<String>("") }
    var matchedItems by remember { mutableStateOf<List<SystemTtsV2>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            allPlugins = dbm.pluginDao.all
            val allTts = dbm.systemTtsV2.all
            matchedItems = allTts.filter { systts ->
                val config = systts.config as? TtsConfigurationDTO ?: return@filter false
                val source = config.source as? PluginTtsSource ?: return@filter false
                source.pluginId == sourcePlugin.pluginId
            }
            // 默认选中第一个非当前插件
            targetPluginId = allPlugins.firstOrNull { it.pluginId != sourcePlugin.pluginId }?.pluginId ?: ""
        }
    }

    val targetPlugins = remember(allPlugins) {
        allPlugins.filter { it.pluginId != sourcePlugin.pluginId }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.92f),
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(R.string.batch_replace_plugin),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.batch_replace_plugin_desc,
                        sourcePlugin.name,
                        matchedItems.size
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (targetPlugins.isNotEmpty()) {
                    AppSpinner(
                        modifier = Modifier.fillMaxWidth(),
                        labelText = stringResource(R.string.target_plugin),
                        value = targetPluginId,
                        values = targetPlugins.map { it.pluginId },
                        entries = targetPlugins.map { it.name },
                        onSelectedChange = { id, _ ->
                            targetPluginId = id as String
                        }
                    )
                } else {
                    Text(
                        text = stringResource(R.string.no_other_plugins),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            val updatedList = matchedItems.map { systts ->
                                val config = systts.config as TtsConfigurationDTO
                                val source = config.source as PluginTtsSource
                                systts.copy(
                                    config = config.copy(
                                        source = source.copy(
                                            pluginId = targetPluginId,
                                            locale = "",
                                            voice = ""
                                        )
                                    )
                                )
                            }
                            if (updatedList.isNotEmpty()) {
                                dbm.systemTtsV2.update(*updatedList.toTypedArray())
                            }
                        }
                        if (matchedItems.any { it.isEnabled }) {
                            SystemTtsService.notifyUpdateConfig()
                        }
                        onDismissRequest()
                    }
                },
                enabled = matchedItems.isNotEmpty() && targetPluginId.isNotEmpty()
            ) {
                Text(stringResource(R.string.replace))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
