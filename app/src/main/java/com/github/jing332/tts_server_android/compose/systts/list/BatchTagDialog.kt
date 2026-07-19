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
import com.github.jing332.database.entities.SpeechRule
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.tts_server_android.model.rhino.speech_rule.SpeechRuleEngine
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.service.systts.SystemTtsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchTagDialog(
    groupItems: List<SystemTtsV2>,
    preselectedItems: Set<SystemTtsV2> = emptySet(),
    onDismissRequest: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current

    var selectedItems by remember { mutableStateOf<Set<SystemTtsV2>>(preselectedItems) }
    var selectedTagKey by remember { mutableStateOf<String>("") }

    // 按当前顺序排列，并只保留有 TtsConfigurationDTO 的项
    val sortedItems = remember(groupItems) {
        groupItems.sortedBy { it.order }.filter { it.config is TtsConfigurationDTO }
    }

    // 检测选中项是否使用同一个 tagRuleId
    val commonTagRuleId by remember(selectedItems) {
        derivedStateOf {
            val ruleIds = selectedItems.map {
                (it.config as TtsConfigurationDTO).speechRule.tagRuleId
            }.distinct()
            if (ruleIds.size == 1) ruleIds.first() else null
        }
    }

    // 获取可用标签列表：优先从数据库读取朗读规则；若规则不存在或 tags 为空，
    // 则从当前选中项已有的 tag/tagName 集合中提取，保证批量分配标签可用。
    // 即使 tagRuleId 不一致，也允许从所有选中项中提取标签进行分配。
    var speechRule by remember { mutableStateOf<SpeechRule?>(null) }
    val derivedTags by remember(selectedItems) {
        derivedStateOf {
            selectedItems.map {
                val sr = (it.config as TtsConfigurationDTO).speechRule
                sr.tag to sr.tagName
            }.filter { it.first.isNotBlank() }
                .distinctBy { it.first }
                .toMap()
        }
    }
    val effectiveTags = remember(speechRule, derivedTags) {
        val map = mutableMapOf<String, String>()
        speechRule?.tags?.let { map.putAll(it) }
        map.putAll(derivedTags)
        map
    }
    // 按自然顺序排序，保证"按顺序分配"时标签递增（如 条目001 -> 条目002）
    val tagKeys = remember(effectiveTags) { effectiveTags.keys.sorted() }
    LaunchedEffect(commonTagRuleId, selectedItems) {
        val dbRule = commonTagRuleId?.let { dbm.speechRuleDao.getByRuleId(it) }
        speechRule = if (!dbRule?.tags.isNullOrEmpty()) dbRule else null
        val keys = tagKeys
        if (keys.isNotEmpty() && (selectedTagKey.isBlank() || !keys.contains(selectedTagKey))) {
            selectedTagKey = keys.first()
        }
    }

    // 当没有任何现成标签时，允许用户自由输入一个标签 key
    var customTagKey by remember { mutableStateOf("") }
    var customTagName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.92f),
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(R.string.batch_assign_tags),
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
                            if (config.speechRule.target == com.github.jing332.database.constants.SpeechTarget.ALL)
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

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(8.dp))

                // 标签选择区
                if (selectedItems.isEmpty()) {
                    Text(
                        text = stringResource(R.string.please_select_voice),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (tagKeys.isEmpty()) {
                    // 没有任何现成标签时，允许自由输入
                    Text(
                        text = stringResource(R.string.start_tag),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = customTagKey,
                        onValueChange = { customTagKey = it },
                        label = { Text(stringResource(R.string.tag)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = customTagName,
                        onValueChange = { customTagName = it },
                        label = { Text(stringResource(R.string.tag_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = stringResource(R.string.start_tag),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    com.github.jing332.compose.widgets.AppSpinner(
                        modifier = Modifier.fillMaxWidth(),
                        labelText = stringResource(R.string.tag),
                        value = selectedTagKey,
                        values = tagKeys,
                        entries = tagKeys.map { effectiveTags[it] ?: it },
                        onSelectedChange = { key, _ ->
                            selectedTagKey = key as String
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            val keys = tagKeys
                            val startIndex = keys.indexOf(selectedTagKey).coerceAtLeast(0)
                            val sortedSelected = sortedItems.filter { it in selectedItems }
                            sortedSelected.forEachIndexed { idx, item ->
                                val config = item.config as TtsConfigurationDTO
                                val ruleData = config.speechRule.copy()
                                val tagKey = if (keys.isNotEmpty()) {
                                    keys.getOrNull((startIndex + idx) % keys.size) ?: return@forEachIndexed
                                } else {
                                    customTagKey
                                }
                                val tagName = effectiveTags[tagKey]
                                    ?: customTagName.takeIf { it.isNotBlank() }
                                    ?: tagKey
                                ruleData.target = com.github.jing332.tts_server_android.constant.SpeechTarget.TAG
                                ruleData.tag = tagKey
                                // tagRuleId 不一致时保持原值，一致时更新为公共 ruleId
                                ruleData.tagRuleId = commonTagRuleId ?: config.speechRule.tagRuleId
                                // 清除旧的 tagData，避免旧标签的残留数据导致 getTagName 计算错误（如显示为旁白）
                                ruleData.tagData = emptyMap()
                                ruleData.tagName = tagName
                                runCatching {
                                    speechRule?.let { sr ->
                                        val computed = SpeechRuleEngine.getTagName(context, sr, ruleData)
                                        if (computed.isNotBlank()) ruleData.tagName = computed
                                    }
                                }
                                dbm.systemTtsV2.update(
                                    item.copy(config = config.copy(speechRule = ruleData))
                                )
                            }
                        }
                        if (selectedItems.any { it.isEnabled }) {
                            SystemTtsService.notifyUpdateConfig()
                        }
                        onDismissRequest()
                    }
                },
                enabled = selectedItems.isNotEmpty() && (tagKeys.isNotEmpty() || customTagKey.isNotBlank())
            ) {
                Text(stringResource(R.string.assign_in_order))
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                selectedItems.forEach { item ->
                                    val config = item.config as TtsConfigurationDTO
                                    val ruleData = config.speechRule.copy()
                                    ruleData.target = com.github.jing332.tts_server_android.constant.SpeechTarget.ALL
                                    ruleData.resetTag()
                                    dbm.systemTtsV2.update(
                                        item.copy(config = config.copy(speechRule = ruleData))
                                    )
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
                    Text(stringResource(R.string.clear_tag))
                }
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    )
}
