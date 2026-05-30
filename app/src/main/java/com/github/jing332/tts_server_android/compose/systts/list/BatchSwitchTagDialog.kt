package com.github.jing332.tts_server_android.compose.systts.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.constant.SpeechTarget
import com.github.jing332.tts_server_android.model.rhino.speech_rule.SpeechRuleEngine
import com.github.jing332.tts_server_android.service.systts.SystemTtsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchSwitchTagDialog(
    allItems: List<SystemTtsV2>,
    onDismissRequest: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current

    var selectedItems by remember { mutableStateOf<Set<SystemTtsV2>>(emptySet()) }

    // 按 order 排序，并只保留有 TtsConfigurationDTO 且非 BGM 的项
    val sortedItems = remember(allItems) {
        allItems.sortedBy { it.order }.filter {
            it.config is TtsConfigurationDTO &&
                    (it.config as TtsConfigurationDTO).speechRule.target != SpeechTarget.BGM
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.92f),
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(R.string.batch_switch_tag),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
            ) {
                // 全选 / 取消全选
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val allSelected = sortedItems.isNotEmpty() && sortedItems.all { it in selectedItems }
                    IconButton(
                        onClick = {
                            selectedItems = if (allSelected) emptySet() else sortedItems.toSet()
                        },
                        enabled = sortedItems.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = if (allSelected) Icons.Default.Clear else Icons.Default.DoneAll,
                            contentDescription = if (allSelected) stringResource(R.string.clear) else stringResource(R.string.select_all)
                        )
                    }
                    Text(
                        text = stringResource(R.string.selected_count, selectedItems.size),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 音色列表
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    itemsIndexed(sortedItems, key = { _, it -> it.id }) { index, item ->
                        val isSelected = item in selectedItems
                        val config = item.config as TtsConfigurationDTO
                        val currentTagName = config.speechRule.tagName.ifBlank {
                            if (config.speechRule.target == SpeechTarget.ALL)
                                stringResource(R.string.no_tag) else ""
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedItems = if (isSelected) selectedItems - item else selectedItems + item
                                }
                                .padding(horizontal = 4.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${index + 1}. ${item.displayName}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (currentTagName.isNotEmpty()) {
                                    Text(
                                        text = "${stringResource(R.string.tag)}: $currentTagName",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                if (sortedItems.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_switchable_items),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            selectedItems.forEach { item ->
                                val config = item.config as TtsConfigurationDTO
                                val ruleData = config.speechRule.copy()

                                dbm.speechRuleDao.getByRuleId(config.speechRule.tagRuleId)?.let { speechRule ->
                                    val keys = speechRule.tags.keys.toList()
                                    if (keys.isEmpty()) return@let

                                    if (config.speechRule.target == SpeechTarget.TAG) {
                                        val idx = keys.indexOf(config.speechRule.tag)
                                        val nextIndex = (idx + 1)
                                        val newTag = keys.getOrNull(nextIndex)
                                        if (newTag == null) {
                                            // 循环回第一个标签
                                            ruleData.target = SpeechTarget.TAG
                                            ruleData.tag = keys.first()
                                        } else {
                                            ruleData.tag = newTag
                                        }
                                    } else {
                                        // 从 ALL 切换到第一个标签
                                        ruleData.target = SpeechTarget.TAG
                                        ruleData.tag = keys.first()
                                    }

                                    runCatching {
                                        ruleData.tagName = SpeechRuleEngine.getTagName(context, speechRule, ruleData)
                                    }.onFailure {
                                        ruleData.tagName = ""
                                    }
                                    ruleData.tagRuleId = speechRule.ruleId

                                    dbm.systemTtsV2.update(
                                        item.copy(config = config.copy(speechRule = ruleData))
                                    )
                                }
                            }
                        }
                        if (selectedItems.any { it.isEnabled }) {
                            SystemTtsService.notifyUpdateConfig()
                        }
                        onDismissRequest()
                    }
                },
                enabled = selectedItems.isNotEmpty()
            ) {
                Text(stringResource(R.string.batch_switch))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
