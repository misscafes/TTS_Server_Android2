package com.github.jing332.tts_server_android.compose.systts.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jing332.database.entities.systts.SystemTtsGroup
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.database.entities.systts.source.LocalTtsSource
import com.github.jing332.database.entities.systts.source.PluginTtsSource
import com.github.jing332.tts_server_android.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupEditContentDialog(
    group: SystemTtsGroup,
    onDismissRequest: () -> Unit,
    vm: GroupEditContentViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedConfigs by remember { mutableStateOf<Set<SystemTtsV2>>(emptySet()) }
    var searchType by remember { mutableStateOf(SearchType.NAME) }
    val availableConfigs by vm.availableConfigs.collectAsStateWithLifecycle()
    
    LaunchedEffect(group.id) {
        vm.load(group.id)
    }
    
    val filteredConfigs = remember(searchQuery, searchType, availableConfigs) {
        vm.filterConfigs(availableConfigs, searchQuery, searchType)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(R.string.edit_group_content, group.name),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            ) {
                // 搜索框
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(stringResource(R.string.search)) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                            }
                        }
                    },
                    singleLine = true
                )
                
                // 搜索类型选择
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SearchTypeChip(
                        text = stringResource(R.string.search_by_name),
                        selected = searchType == SearchType.NAME,
                        onClick = { searchType = SearchType.NAME }
                    )
                    SearchTypeChip(
                        text = stringResource(R.string.search_by_tag),
                        selected = searchType == SearchType.TAG,
                        onClick = { searchType = SearchType.TAG }
                    )
                    SearchTypeChip(
                        text = stringResource(R.string.search_by_plugin),
                        selected = searchType == SearchType.PLUGIN,
                        onClick = { searchType = SearchType.PLUGIN }
                    )
                }
                
                // 统计信息
                Text(
                    text = stringResource(
                        R.string.available_config_count,
                        filteredConfigs.size
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                HorizontalDivider()
                
                // 配置列表
                if (filteredConfigs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isEmpty())
                                stringResource(R.string.no_config_to_move)
                            else
                                stringResource(R.string.empty_list),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredConfigs, key = { it.id }) { config ->
                            val isSelected = selectedConfigs.contains(config)
                            ConfigItem(
                                config = config,
                                isSelected = isSelected,
                                onToggleSelection = {
                                    selectedConfigs = if (isSelected) {
                                        selectedConfigs - config
                                    } else {
                                        selectedConfigs + config
                                    }
                                },
                                searchType = searchType
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        vm.moveConfigsToGroup(selectedConfigs.toList())
                        onDismissRequest()
                    }
                },
                enabled = selectedConfigs.isNotEmpty()
            ) {
                Text(
                    stringResource(
                        R.string.move_to_group_with_count,
                        selectedConfigs.size,
                        group.name
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun SearchTypeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) }
    )
}

@Composable
private fun ConfigItem(
    config: SystemTtsV2,
    isSelected: Boolean,
    onToggleSelection: () -> Unit,
    searchType: SearchType
) {
    val ttsConfig = config.ttsConfig
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleSelection() }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = config.displayName,
                style = MaterialTheme.typography.bodyMedium
            )
            
            // 显示额外信息
            val extraInfo = when (searchType) {
                SearchType.TAG -> {
                    if (ttsConfig.speechRule.tagName.isNotEmpty()) {
                        "标签: ${ttsConfig.speechRule.tagName}"
                    } else null
                }
                SearchType.PLUGIN -> {
                    when (val source = ttsConfig.source) {
                        is PluginTtsSource -> "插件: ${source.pluginId}"
                        is LocalTtsSource -> "本地TTS"
                        else -> "其他"
                    }
                }
                else -> null
            }
            
            if (extraInfo != null) {
                Text(
                    text = extraInfo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

enum class SearchType {
    NAME, TAG, PLUGIN
}

@Composable
fun GroupEditContentViewModel.groupEditContentViewModel(): GroupEditContentViewModel = viewModel()
