package com.github.jing332.tts_server_android.compose.systts.list

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallMerge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drake.net.utils.withIO
import com.github.jing332.common.utils.longToast
import com.github.jing332.common.utils.toast
import com.github.jing332.compose.widgets.ControlBottomBarVisibility
import com.github.jing332.compose.widgets.LazyListIndexStateSaver
import com.github.jing332.compose.widgets.ShadowedDraggableItem
import com.github.jing332.compose.widgets.TextFieldDialog
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.AbstractListGroup
import com.github.jing332.database.entities.systts.BgmConfiguration
import com.github.jing332.database.entities.systts.GroupWithSystemTts
import com.github.jing332.database.entities.systts.AudioParams
import com.github.jing332.database.entities.systts.SystemTtsGroup
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.database.entities.systts.source.LocalTtsSource
import com.github.jing332.database.entities.systts.source.PluginTtsSource
import com.github.jing332.tts_server_android.AppLocale
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.compose.AppDefaultProperties
import com.github.jing332.tts_server_android.conf.AppConfig
import com.github.jing332.tts_server_android.compose.LocalBottomBarBehavior
import com.github.jing332.tts_server_android.compose.LocalNavController
import com.github.jing332.tts_server_android.compose.SharedViewModel
import com.github.jing332.tts_server_android.compose.nav.NavRoutes
import com.github.jing332.tts_server_android.compose.nav.NavTopAppBar
import com.github.jing332.tts_server_android.compose.systts.AuditionDialog
import com.github.jing332.tts_server_android.compose.systts.ConfigDeleteDialog
import com.github.jing332.tts_server_android.compose.systts.ConfigExportBottomSheet
import com.github.jing332.tts_server_android.compose.systts.list.ui.ItemDescriptorFactory
import com.github.jing332.tts_server_android.compose.systts.list.ui.widgets.QuickEditBottomSheet
import com.github.jing332.tts_server_android.compose.systts.list.ui.widgets.TagDataClearConfirmDialog
import com.github.jing332.tts_server_android.compose.systts.plugin.PluginSelectionDialog
import com.github.jing332.tts_server_android.compose.systts.list.SearchTextField
import com.github.jing332.tts_server_android.compose.systts.list.SearchType
import com.github.jing332.tts_server_android.compose.systts.list.SubGroupHeader
import com.github.jing332.tts_server_android.compose.systts.list.buildSubCategoryTree
import com.github.jing332.tts_server_android.compose.systts.list.flattenSubCategoryTree
import com.github.jing332.tts_server_android.compose.systts.list.FlattenedCategoryItem
import com.github.jing332.tts_server_android.compose.systts.sizeToToggleableState
import com.github.jing332.tts_server_android.constant.AppConst
import com.github.jing332.tts_server_android.constant.SpeechTarget
import com.github.jing332.tts_server_android.model.rhino.speech_rule.SpeechRuleEngine
import com.github.jing332.tts_server_android.service.systts.SystemTtsService
import android.content.Intent
import com.github.jing332.tts_server_android.toCode
import com.github.jing332.database.entities.SpeechRule
import com.github.jing332.database.entities.plugin.Plugin
import com.github.jing332.database.entities.systts.BasicAudioFormat
import com.github.jing332.database.entities.systts.SpeechRuleInfo
import com.github.jing332.tts.speech.plugin.engine.TtsPluginUiEngineV2
import com.github.jing332.tts_server_android.ui.view.AppDialogs.displayErrorDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.OutputStreamWriter
import java.net.URL


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun ListManagerScreen(
    sharedVM: SharedViewModel,
    vm: ListManagerViewModel = viewModel(),
) {
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val models by vm.list.collectAsStateWithLifecycle()
    val searchKeyword by vm.keyword.collectAsStateWithLifecycle()
    val searchType by vm.searchType.collectAsStateWithLifecycle()
    
    var isSearchMode by rememberSaveable { mutableStateOf(false) }

    // 批量编辑状态
    var isBatchEditMode by rememberSaveable { mutableStateOf(false) }
    var selectedTtsIds by rememberSaveable { mutableStateOf<Set<Long>>(emptySet()) }

    // AI生成配置状态
    var showAiGenerateConfigDialog by remember { mutableStateOf(false) }
    var aiGenerateSelectedPluginId by rememberSaveable { mutableStateOf("") }
    var aiGenerateDynamicPluginId by rememberSaveable { mutableStateOf("") }
    var aiGenerateDynamicVoices by remember { mutableStateOf<List<AiPluginVoicePreview>>(emptyList()) }
    var aiGenerateVoiceLoading by remember { mutableStateOf(false) }
    var aiGenerateVoiceError by remember { mutableStateOf("") }
    var aiGenerateMatchPreviewText by remember { mutableStateOf("") }
    var showAiGenerateModelDialog by remember { mutableStateOf(false) }
    var aiGenerateModelText by remember { mutableStateOf("") }
    var aiRulePreviewExpanded by rememberSaveable { mutableStateOf(false) }

    // 树形分组状态
    var addGroupParentId by remember { mutableStateOf(0L) }
    var moveGroupDialog by remember { mutableStateOf<SystemTtsGroup?>(null) }

    // 子分组展开状态：存储已展开的子分组完整路径（持久化，默认全部折叠）
    var expandedSubGroups by remember { AppConfig.expandedSubGroups }

    BackHandler(enabled = isSearchMode) {
        isSearchMode = false
        vm.setSearchKeyword("")
    }

    BackHandler(enabled = isBatchEditMode) {
        isBatchEditMode = false
        selectedTtsIds = emptySet()
    }

    var showSortDialog by remember { mutableStateOf<Pair<List<SystemTtsV2>, List<SystemTtsV2>?>?>(null) }
    if (showSortDialog != null) {
        val (list, groupList) = showSortDialog!!
        SortDialog(
            onDismissRequest = { showSortDialog = null },
            list = list,
            groupList = groupList
        )
    }

    // 子分组操作对话框状态
    var showSubGroupRename by remember { mutableStateOf<Pair<List<SystemTtsV2>, String>?>(null) }
    if (showSubGroupRename != null) {
        val (items, oldPath) = showSubGroupRename!!
        var newName by remember { mutableStateOf(oldPath.substringAfterLast('/')) }
        TextFieldDialog(
            title = "重命名子分组",
            text = newName,
            onTextChange = { newName = it },
            onDismissRequest = { showSubGroupRename = null }
        ) {
            scope.launch {
                val newPath = if (oldPath.contains('/')) {
                    oldPath.substringBeforeLast('/') + "/" + newName
                } else newName
                items.forEach { item ->
                    dbm.systemTtsV2.update(item.copy(categoryPath = newPath))
                }
                showSubGroupRename = null
            }
        }
    }

    var showSubGroupAudioParams by remember { mutableStateOf<Pair<SystemTtsGroup, String>?>(null) }
    if (showSubGroupAudioParams != null) {
        val (group, path) = showSubGroupAudioParams!!
        val subGroupMap = group.subGroupAudioParamsJson.let { jsonStr ->
            if (jsonStr.isBlank() || jsonStr == "{}") emptyMap()
            else SystemTtsV2.Converters.json.decodeFromString<Map<String, com.github.jing332.database.entities.systts.AudioParams>>(jsonStr)
        }
        val currentParams = subGroupMap[path] ?: com.github.jing332.database.entities.systts.AudioParams()
        GroupAudioParamsDialog(
            onDismissRequest = { showSubGroupAudioParams = null },
            params = currentParams,
            onConfirm = { params ->
                scope.launch {
                    val newMap = subGroupMap.toMutableMap().apply { put(path, params) }
                    val newJson = SystemTtsV2.Converters.json.encodeToString(newMap)
                    dbm.systemTtsV2.updateGroup(group.copy(subGroupAudioParamsJson = newJson))
                    SystemTtsService.notifyUpdateConfig()
                    showSubGroupAudioParams = null
                }
            }
        )
    }

    var showSubGroupBatchTag by remember { mutableStateOf<List<SystemTtsV2>?>(null) }
    if (showSubGroupBatchTag != null) {
        BatchTagDialog(
            groupItems = showSubGroupBatchTag!!,
            onDismissRequest = { showSubGroupBatchTag = null }
        )
    }

    // 子分组转为大分组确认对话框
    var showSubGroupExtractToGroup by remember { mutableStateOf<Pair<SystemTtsGroup, String>?>(null) }
    if (showSubGroupExtractToGroup != null) {
        val (group, path) = showSubGroupExtractToGroup!!
        AlertDialog(
            onDismissRequest = { showSubGroupExtractToGroup = null },
            title = { Text("转为大分组") },
            text = { Text("将子分组 \"${path.substringAfterLast('/')}\" 移出为独立的大分组？") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        val currentGroupWithTts = models.find { it.group.id == group.id }
                        val itemsToMove = currentGroupWithTts?.list
                            ?.filter { it.categoryPath == path }
                            ?: emptyList()

                        val subGroupAudioParams = group.subGroupAudioParamsJson.let { jsonStr ->
                            if (jsonStr.isBlank() || jsonStr == "{}") emptyMap()
                            else SystemTtsV2.Converters.json.decodeFromString<Map<String, AudioParams>>(jsonStr)
                        }
                        val audioParamsForNewGroup = subGroupAudioParams[path] ?: AudioParams()

                        if (subGroupAudioParams.containsKey(path)) {
                            val newSubMap = subGroupAudioParams.toMutableMap().apply { remove(path) }
                            val newSubJson = SystemTtsV2.Converters.json.encodeToString(newSubMap)
                            dbm.systemTtsV2.updateGroup(group.copy(subGroupAudioParamsJson = newSubJson))
                        }

                        val groupName = path.substringAfterLast('/')
                        val newGroup = SystemTtsGroup(
                            id = System.currentTimeMillis(),
                            name = groupName,
                            audioParams = audioParamsForNewGroup
                        )
                        dbm.systemTtsV2.insertGroup(newGroup)
                        itemsToMove.forEach { item ->
                            dbm.systemTtsV2.update(
                                item.copy(
                                    groupId = newGroup.id,
                                    categoryPath = ""
                                )
                            )
                        }
                        showSubGroupExtractToGroup = null
                    }
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubGroupExtractToGroup = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    var showQuickEdit by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (showQuickEdit != null) {
        QuickEditBottomSheet(onDismissRequest = {
            dbm.systemTtsV2.insert(showQuickEdit!!)
            if (showQuickEdit?.isEnabled == true) SystemTtsService.notifyUpdateConfig()
            showQuickEdit = null
        }, systts = showQuickEdit!!, onSysttsChange = {
            showQuickEdit = it
        })
    }

    if (showAiGenerateModelDialog) {
        TextFieldDialog(
            title = "🤖 AI生成配置模型设置",
            text = aiGenerateModelText,
            onTextChange = { aiGenerateModelText = it },
            onDismissRequest = {
                showAiGenerateModelDialog = false
            }
        ) {
            writeAiGenerateConfigApiText(context, aiGenerateModelText)
            showAiGenerateModelDialog = false
            context.toast("已保存 AI生成配置模型设置")
        }
    }

    if (showAiGenerateConfigDialog) {
        AlertDialog(
            onDismissRequest = {
                showAiGenerateConfigDialog = false
            },
            title = {
                Text("🤖 AI生成配置列表")
            },
            text = {
                val activeRule = remember(showAiGenerateConfigDialog) {
                    dbm.speechRuleDao.allEnabled.firstOrNull()
                }

                val aiPluginList = remember(showAiGenerateConfigDialog) {
                    dbm.pluginDao.all.filter { it.isEnabled }
                }

                val selectedPlugin = remember(aiGenerateSelectedPluginId, aiPluginList) {
                    aiPluginList.firstOrNull { it.pluginId == aiGenerateSelectedPluginId }
                }

                val staticVoicePreview = remember(selectedPlugin?.pluginId) {
                    selectedPlugin?.let { extractAiPluginVoicePreview(it.code.toString()) }.orEmpty()
                }

                val voicePreview = remember(
                    selectedPlugin?.pluginId,
                    aiGenerateDynamicPluginId,
                    aiGenerateDynamicVoices,
                    staticVoicePreview
                ) {
                    if (
                        selectedPlugin != null &&
                        aiGenerateDynamicPluginId == selectedPlugin.pluginId &&
                        aiGenerateDynamicVoices.isNotEmpty()
                    ) {
                        aiGenerateDynamicVoices
                    } else {
                        staticVoicePreview
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    val rolePreviewText = buildAiRuleRolePreviewText(activeRule)
                    val rolePreviewLines = rolePreviewText.lines()
                    val rolePreviewCollapsedText = buildString {
                        rolePreviewLines.take(8).forEachIndexed { index, line ->
                            append(line)
                            if (index != minOf(7, rolePreviewLines.size - 1)) {
                                append("\n")
                            }
                        }

                        if (rolePreviewLines.size > 8) {
                            append("\n…（已折叠，点击展开查看全部）")
                        }
                    }

                    Text(
                        text = if (aiRulePreviewExpanded) rolePreviewText else rolePreviewCollapsedText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    TextButton(
                        onClick = {
                            aiRulePreviewExpanded = !aiRulePreviewExpanded
                        }
                    ) {
                        Text(if (aiRulePreviewExpanded) "收起规则详情" else "展开规则详情")
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AI匹配模型",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.weight(1f)
                        )

                        TextButton(
                            onClick = {
                                aiGenerateModelText = readAiGenerateConfigApiText(context)
                                showAiGenerateModelDialog = true
                            }
                        ) {
                            Text("模型设置")
                        }
                    }

                    Text(
                        text = if (readAiGenerateConfigApiText(context).isBlank()) {
                            "未单独设置模型，后续将回退使用主规则 miyue.txt。"
                        } else {
                            "已设置独立 AI 生成配置模型。"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "选择 TTS 插件",
                        style = MaterialTheme.typography.titleSmall
                    )

                    if (aiPluginList.isEmpty()) {
                        Text(
                            text = "当前没有可用插件。",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 260.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            aiPluginList.forEach { plugin ->
                                TextButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        aiGenerateSelectedPluginId = plugin.pluginId
                                        aiGenerateDynamicPluginId = ""
                                        aiGenerateDynamicVoices = emptyList()
                                        aiGenerateVoiceError = ""
                                        aiGenerateMatchPreviewText = ""
                                    }
                                ) {
                                    Text(
                                        text = if (plugin.pluginId == aiGenerateSelectedPluginId) {
                                            "✅ ${plugin.name.ifBlank { plugin.pluginId }}"
                                        } else {
                                            plugin.name.ifBlank { plugin.pluginId }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (selectedPlugin != null) {
                        TextButton(
                            enabled = !aiGenerateVoiceLoading,
                            onClick = {
                                val plugin = selectedPlugin
                                if (plugin != null) {
                                    aiGenerateVoiceLoading = true
                                    aiGenerateVoiceError = ""

                                    scope.launch {
                                        val result = withIO {
                                            runCatching {
                                                fetchAiPluginVoicesDynamic(context, plugin)
                                            }
                                        }

                                        result.onSuccess { voices ->
                                            aiGenerateDynamicPluginId = plugin.pluginId
                                            aiGenerateDynamicVoices = voices
                                            aiGenerateMatchPreviewText = ""
                                            aiGenerateVoiceError = if (voices.isEmpty()) {
                                                "动态获取成功，但声音列表为空"
                                            } else {
                                                ""
                                            }
                                        }.onFailure { e ->
                                            aiGenerateVoiceError = e.message ?: "动态获取声音列表失败"
                                        }

                                        aiGenerateVoiceLoading = false
                                    }
                                }
                            }
                        ) {
                            Text(if (aiGenerateVoiceLoading) "📡 正在获取声音列表..." else "📡 动态获取声音列表")
                        }

                        if (aiGenerateVoiceError.isNotBlank()) {
                            Text(
                                text = aiGenerateVoiceError,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = buildAiPluginVoiceGroupPreviewText(
                            pluginName = selectedPlugin?.name.orEmpty(),
                            voices = voicePreview
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            aiGenerateMatchPreviewText = "🤖 AI正在匹配，请稍候..."

                            scope.launch {
                                aiGenerateMatchPreviewText = withIO {
                                    buildAiGenerateConfigMatchPreviewTextWithAi(
                                        context = context,
                                        rule = activeRule,
                                        pluginName = selectedPlugin?.name.orEmpty(),
                                        voices = voicePreview
                                    )
                                }
                            }
                        }
                    ) {
                        Text("🎯 生成匹配预览")
                    }

                    if (aiGenerateMatchPreviewText.isNotBlank()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text(
                            text = aiGenerateMatchPreviewText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                val count = generateAiConfigListFromPreview(
                                    context = context,
                                    rule = activeRule,
                                    plugin = selectedPlugin,
                                    voices = voicePreview
                                )

                                if (count > 0) {
                                    context.longToast("已生成 $count 条 TTS 配置")
                                    SystemTtsService.notifyUpdateConfig()
                                    showAiGenerateConfigDialog = false
                                } else {
                                    aiGenerateMatchPreviewText = "没有生成新配置。可能是已存在相同朗读规则 tag，或当前插件没有可匹配声音。"
                                }
                            }
                        ) {
                            Text("✅ 确认生成配置列表")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAiGenerateConfigDialog = false
                    }
                ) {
                    Text("关闭")
                }
            }
        )
    }

    fun navigateToEdit(systts: SystemTtsV2) {
        sharedVM.put(NavRoutes.TtsEdit.DATA, systts)
        navController.navigate(NavRoutes.TtsEdit.id)
    }

    var hasShownTip by rememberSaveable { mutableStateOf(false) }

    var showCreateSubGroup by remember { mutableStateOf<Long?>(null) }
    if (showCreateSubGroup != null) {
        val targetGroupWithTts = models.find { it.group.id == showCreateSubGroup }
        if (targetGroupWithTts != null) {
            val targetGroup = targetGroupWithTts.group
            val ungrouped = remember(targetGroup.id) {
                targetGroupWithTts.list.filter { it.categoryPath.isBlank() }
            }
            CreateSubGroupDialog(
                groupName = targetGroup.name,
                ungroupedItems = ungrouped,
                onDismissRequest = { showCreateSubGroup = null },
                onConfirm = { subGroupName, selectedItems ->
                    scope.launch {
                        selectedItems.forEach { item ->
                            dbm.systemTtsV2.update(
                                item.copy(categoryPath = subGroupName)
                            )
                        }
                        showCreateSubGroup = null
                    }
                }
            )
        }
    }

    var showMoveToSubGroup by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (showMoveToSubGroup != null) {
        val targetItem = showMoveToSubGroup!!
        val currentGroup = models.find { it.group.id == targetItem.groupId }
        val existingPaths = remember(currentGroup?.group?.id) {
            currentGroup?.list?.map { it.categoryPath }
                ?.filter { it.isNotBlank() }
                ?.distinct()
                ?.sorted()
                ?: emptyList()
        }
        MoveToSubGroupDialog(
            existingPaths = existingPaths,
            onDismissRequest = { showMoveToSubGroup = null },
            onConfirm = { path ->
                scope.launch {
                    dbm.systemTtsV2.update(targetItem.copy(categoryPath = path))
                    showMoveToSubGroup = null
                }
            }
        )
    }

    var showBatchTagDialog by remember { mutableStateOf<List<SystemTtsV2>?>(null) }
    var batchTagPreselected by remember { mutableStateOf<Set<SystemTtsV2>>(emptySet()) }
    if (showBatchTagDialog != null) {
        BatchTagDialog(
            groupItems = showBatchTagDialog!!,
            preselectedItems = batchTagPreselected,
            onDismissRequest = { 
                showBatchTagDialog = null
                batchTagPreselected = emptySet()
            }
        )
    }

    // === 子分组操作对话框 ===
    var showReleaseSubGroup by remember { mutableStateOf<SystemTtsGroup?>(null) }
    if (showReleaseSubGroup != null) {
        val targetGroup = showReleaseSubGroup!!
        val currentGroupWithTts = models.find { it.group.id == targetGroup.id }
        val subPaths = remember(currentGroupWithTts) {
            currentGroupWithTts?.list
                ?.map { it.categoryPath }
                ?.filter { it.isNotBlank() }
                ?.distinct()
                ?.sorted()
                ?: emptyList()
        }
        AlertDialog(
            onDismissRequest = { showReleaseSubGroup = null },
            title = { Text("释放子分组") },
            text = {
                Column {
                    if (subPaths.isEmpty()) {
                        Text("当前分组没有子分组")
                    } else {
                        Text("选择要释放的子分组，内容将移回根目录：", modifier = Modifier.padding(bottom = 8.dp))
                        subPaths.forEach { path ->
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        currentGroupWithTts?.list
                                            ?.filter { it.categoryPath == path }
                                            ?.forEach { item ->
                                                dbm.systemTtsV2.update(item.copy(categoryPath = ""))
                                            }
                                        showReleaseSubGroup = null
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(path)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showReleaseSubGroup = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    var showConvertToSubGroup by remember { mutableStateOf<SystemTtsGroup?>(null) }
    if (showConvertToSubGroup != null) {
        val targetGroup = showConvertToSubGroup!!
        val currentGroupWithTts = models.find { it.group.id == targetGroup.id }
        val hasSubGroups = currentGroupWithTts?.list?.any { it.categoryPath.isNotBlank() } == true

        if (hasSubGroups) {
            AlertDialog(
                onDismissRequest = { showConvertToSubGroup = null },
                title = { Text("无法转换") },
                text = { Text("当前分组包含子分组，无法转换为子分组。请先释放所有子分组。") },
                confirmButton = {
                    TextButton(onClick = { showConvertToSubGroup = null }) {
                        Text("确定")
                    }
                }
            )
        } else {
            AlertDialog(
                onDismissRequest = { showConvertToSubGroup = null },
                title = { Text("转为子分组") },
                text = {
                    val otherGroups = models.filter { it.group.id != targetGroup.id }.map { it.group }
                    Column {
                        Text("选择目标分组，当前分组将作为其子分组：", modifier = Modifier.padding(bottom = 8.dp))
                        otherGroups.forEach { otherGroup ->
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        // 将原大分组的音频参数作为子分组参数保存到目标分组
                                        val subMap = otherGroup.subGroupAudioParamsJson.let { jsonStr ->
                                            if (jsonStr.isBlank() || jsonStr == "{}") emptyMap()
                                            else SystemTtsV2.Converters.json.decodeFromString<Map<String, com.github.jing332.database.entities.systts.AudioParams>>(jsonStr)
                                        }.toMutableMap()
                                        subMap[targetGroup.name] = targetGroup.audioParams
                                        val newJson = SystemTtsV2.Converters.json.encodeToString(subMap)
                                        dbm.systemTtsV2.updateGroup(otherGroup.copy(subGroupAudioParamsJson = newJson))

                                        currentGroupWithTts?.list?.forEach { item ->
                                            dbm.systemTtsV2.update(
                                                item.copy(
                                                    groupId = otherGroup.id,
                                                    categoryPath = targetGroup.name
                                                )
                                            )
                                        }
                                        // 当前分组已为空，直接删除
                                        dbm.systemTtsV2.deleteGroup(targetGroup)
                                        showConvertToSubGroup = null
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(otherGroup.name)
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showConvertToSubGroup = null }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }

    var showExtractSubGroup by remember { mutableStateOf<SystemTtsGroup?>(null) }
    if (showExtractSubGroup != null) {
        val targetGroup = showExtractSubGroup!!
        val currentGroupWithTts = models.find { it.group.id == targetGroup.id }
        val subPaths = remember(currentGroupWithTts) {
            currentGroupWithTts?.list
                ?.map { it.categoryPath }
                ?.filter { it.isNotBlank() }
                ?.distinct()
                ?.sorted()
                ?: emptyList()
        }
        AlertDialog(
            onDismissRequest = { showExtractSubGroup = null },
            title = { Text("移出子分组") },
            text = {
                Column {
                    if (subPaths.isEmpty()) {
                        Text("当前分组没有子分组")
                    } else {
                        Text("选择要移出的子分组，将创建为独立分组：", modifier = Modifier.padding(bottom = 8.dp))
                        subPaths.forEach { path ->
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        val itemsToMove = currentGroupWithTts?.list
                                            ?.filter { it.categoryPath == path }
                                            ?: emptyList()

                                        // 读取子分组音频参数
                                        val subGroupAudioParams = targetGroup.subGroupAudioParamsJson.let { jsonStr ->
                                            if (jsonStr.isBlank() || jsonStr == "{}") emptyMap()
                                            else SystemTtsV2.Converters.json.decodeFromString<Map<String, com.github.jing332.database.entities.systts.AudioParams>>(jsonStr)
                                        }
                                        val audioParamsForNewGroup = subGroupAudioParams[path] ?: com.github.jing332.database.entities.systts.AudioParams()

                                        // 从原分组中移除该子分组参数记录
                                        if (subGroupAudioParams.containsKey(path)) {
                                            val newSubMap = subGroupAudioParams.toMutableMap().apply { remove(path) }
                                            val newSubJson = SystemTtsV2.Converters.json.encodeToString(newSubMap)
                                            dbm.systemTtsV2.updateGroup(targetGroup.copy(subGroupAudioParamsJson = newSubJson))
                                        }

                                        val groupName = path.substringAfterLast('/')
                                        val newGroup = SystemTtsGroup(
                                            id = System.currentTimeMillis(),
                                            name = groupName,
                                            audioParams = audioParamsForNewGroup
                                        )
                                        dbm.systemTtsV2.insertGroup(newGroup)
                                        itemsToMove.forEach { item ->
                                            dbm.systemTtsV2.update(
                                                item.copy(
                                                    groupId = newGroup.id,
                                                    categoryPath = ""
                                                )
                                            )
                                        }
                                        showExtractSubGroup = null
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(path)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showExtractSubGroup = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (moveGroupDialog != null) {
        val targetGroup = moveGroupDialog!!
        val candidateParents = models
            .map { it.group }
            .filter { it.id != targetGroup.id }

        AlertDialog(
            onDismissRequest = { moveGroupDialog = null },
            title = { Text("选择目标父分组") },
            text = {
                Column {
                    if (candidateParents.isEmpty()) {
                        Text("没有其他可用分组")
                    } else {
                        Text("将 \"${targetGroup.name}\" 移动到：", modifier = Modifier.padding(bottom = 8.dp))
                        candidateParents.forEach { parent ->
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        withIO {
                                            vm.moveGroupToParent(targetGroup, parent.id)
                                        }
                                    }
                                    moveGroupDialog = null
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(parent.name.ifBlank { "未命名" })
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { moveGroupDialog = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    var showTagClearDialog by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (showTagClearDialog != null) {
        val systts = showTagClearDialog!!
        val config = systts.config as TtsConfigurationDTO
        TagDataClearConfirmDialog(
            tagData = config.speechRule.tagData.toString(),
            onDismissRequest = { showTagClearDialog = null },
            onConfirm = {
                dbm.systemTtsV2.update(
                    systts.copy(
                        config = config.copy(
                            speechRule = config.speechRule.copy(
                                target = SpeechTarget.ALL,
                            ).apply { resetTag() },
                        )
                    )
                )
                if (systts.isEnabled) SystemTtsService.notifyUpdateConfig()
                showTagClearDialog = null
            }
        )
    }

    fun switchSpeechTarget(systts: SystemTtsV2) {
        if (!hasShownTip) {
            hasShownTip = true
            context.longToast(R.string.systts_drag_tip_msg)
        }

        val config = systts.config as TtsConfigurationDTO
        if (config.speechRule.target == SpeechTarget.BGM) return
        val ruleData = config.speechRule.copy()

        if (config.speechRule.target == SpeechTarget.TAG) dbm.speechRuleDao.getByRuleId(
            config.speechRule.tagRuleId
        )?.let { speechRule ->
            val keys = speechRule.tags.keys.toList()
            val idx = keys.indexOf(config.speechRule.tag)

            val nextIndex = (idx + 1)
            val newTag = keys.getOrNull(nextIndex)
            if (newTag == null) {
                if (ruleData.isTagDataEmpty()) {
                    ruleData.target = SpeechTarget.ALL
                    ruleData.resetTag()
                } else {
                    showTagClearDialog = systts
                    return
                }
            } else {
                ruleData.tag = newTag
                // 切换标签时清除旧的 tagData，避免残留数据导致 getTagName 返回错误名称（如旁白）
                ruleData.tagData = emptyMap()
                ruleData.tagName = speechRule.tags[newTag] ?: ""
                runCatching {
                    val computed = SpeechRuleEngine.getTagName(context, speechRule, info = ruleData)
                    if (computed.isNotBlank()) ruleData.tagName = computed
                }.onFailure {
                    context.displayErrorDialog(it)
                }

            }
        }
        else {
            dbm.speechRuleDao.getByRuleId(ruleData.tagRuleId)?.let {
                val firstTag = it.tags.keys.first()
                ruleData.target = SpeechTarget.TAG
                ruleData.tag = firstTag
                // 从 ALL 切换到 TAG 时同步设置 tagName，避免列表标签名为空
                ruleData.tagName = it.tags[firstTag] ?: ""
                ruleData.tagData = emptyMap()
            }
        }

        dbm.systemTtsV2.update(systts.copy(config = systts.ttsConfig.copy(speechRule = ruleData)))
        if (systts.isEnabled) SystemTtsService.notifyUpdateConfig()
    }

    var deleteTts by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (deleteTts != null) {
        ConfigDeleteDialog(
            onDismissRequest = { deleteTts = null }, content = deleteTts?.displayName ?: ""
        ) {
            dbm.systemTtsV2.delete(deleteTts!!)
            deleteTts = null
        }
    }

    var groupAudioParamsDialog by remember { mutableStateOf<SystemTtsGroup?>(null) }
    if (groupAudioParamsDialog != null) {
        GroupAudioParamsDialog(onDismissRequest = { groupAudioParamsDialog = null },
            params = groupAudioParamsDialog!!.audioParams,
            onConfirm = {
                dbm.systemTtsV2.updateGroup(
                    groupAudioParamsDialog!!.copy(audioParams = it)
                )
                // 通知服务更新配置
                SystemTtsService.notifyUpdateConfig()
                groupAudioParamsDialog = null
            })
    }

    val listState = rememberLazyListState()
    LazyListIndexStateSaver(models = models, listState = listState)

    val reorderState = rememberReorderableLazyListState(
        listState = listState, onMove = vm::reorder
    )

    var addGroupDialog by remember { mutableStateOf(false) }
    if (addGroupDialog) {
        var name by remember { mutableStateOf("") }
        TextFieldDialog(title = stringResource(id = R.string.add_group),
            text = name,
            onTextChange = { name = it },
            onDismissRequest = {
                addGroupDialog = false
                addGroupParentId = 0L
            }) {
            addGroupDialog = false
            val parentId = addGroupParentId
            addGroupParentId = 0L
            val order = dbm.systemTtsV2.getGroupCountByParent(parentId)
            dbm.systemTtsV2.insertGroup(
                SystemTtsGroup(
                    name = name,
                    order = order,
                    parentGroupId = parentId
                )
            )
        }
    }

    var showGroupExportSheet by remember { mutableStateOf<List<GroupTreeNode>?>(null) }
    if (showGroupExportSheet != null) {
        val list = showGroupExportSheet!!.map { GroupWithSystemTts(group = it.group, list = it.allTts()) }
        ListExportBottomSheet(onDismissRequest = { showGroupExportSheet = null }, list = list)
    }

    var showExportSheet by remember { mutableStateOf<List<SystemTtsV2>?>(null) }
    if (showExportSheet != null) {
        val jStr = remember { AppConst.jsonBuilder.encodeToString(showExportSheet!!) }
        ConfigExportBottomSheet(json = jStr) { showExportSheet = null }
    }

    var addPluginDialog by remember { mutableStateOf(false) }
    if (addPluginDialog) {
        PluginSelectionDialog(onDismissRequest = { addPluginDialog = false }) {
            navigateToEdit(
                SystemTtsV2(
                    config = TtsConfigurationDTO(
                        source = PluginTtsSource(
                            pluginId = it.pluginId,
                            locale = AppLocale.current(context).toCode()
                        )
                    )
                )
            )
        }
    }

    var showAuditionDialog by remember { mutableStateOf<SystemTtsV2?>(null) }
    if (showAuditionDialog != null) AuditionDialog(systts = showAuditionDialog!!) {
        showAuditionDialog = null
    }

    // ===== 批量删除对话框 =====
    var showBatchDeleteDialog by remember { mutableStateOf(false) }
    if (showBatchDeleteDialog) {
        val selectedItems = models.flatMap { it.allTts() }.filter { selectedTtsIds.contains(it.id) }
        AlertDialog(
            onDismissRequest = { showBatchDeleteDialog = false },
            title = { Text(stringResource(R.string.batch_delete)) },
            text = { Text(stringResource(R.string.batch_delete_confirm, selectedItems.size)) },
            confirmButton = {
                TextButton(onClick = {
                    showBatchDeleteDialog = false
                    scope.launch {
                        withIO {
                            if (selectedItems.isNotEmpty()) {
                                dbm.systemTtsV2.delete(*selectedItems.toTypedArray())
                            }
                            // 若某些分组被完整选中，删除这些空分组（含子分组）
                            val fullySelectedGroups = vm.findFullySelectedGroups(selectedTtsIds)
                            fullySelectedGroups.forEach { node ->
                                vm.deleteGroupTree(node)
                            }
                        }
                        if (selectedItems.any { it.isEnabled }) {
                            SystemTtsService.notifyUpdateConfig()
                        }
                        context.longToast(context.getString(R.string.batch_delete_done, selectedItems.size))
                        isBatchEditMode = false
                        selectedTtsIds = emptySet()
                    }
                }) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBatchDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // ===== 批量切换插件对话框 =====
    var showBatchSwitchPluginDialog by remember { mutableStateOf(false) }
    if (showBatchSwitchPluginDialog) {
        val pluginList = remember { dbm.pluginDao.all }
        val selectedItems = models.flatMap { it.allTts() }.filter { selectedTtsIds.contains(it.id) }
        var targetPluginId by remember { mutableStateOf<String>("") }

        AlertDialog(
            onDismissRequest = { showBatchSwitchPluginDialog = false },
            title = { Text(stringResource(R.string.batch_switch_plugin)) },
            text = {
                Column {
                    Text(stringResource(R.string.batch_switch_plugin_desc, selectedItems.size))
                    Spacer(Modifier.height(12.dp))
                    if (pluginList.isNotEmpty()) {
                        com.github.jing332.compose.widgets.AppSpinner(
                            modifier = Modifier.fillMaxWidth(),
                            labelText = stringResource(R.string.target_plugin),
                            value = targetPluginId,
                            values = pluginList.map { it.pluginId },
                            entries = pluginList.map { it.name },
                            onSelectedChange = { id, _ -> targetPluginId = id as String }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBatchSwitchPluginDialog = false
                        scope.launch {
                            withIO {
                                val updatedList = selectedItems.mapNotNull { tts ->
                                    val config = tts.config as? TtsConfigurationDTO ?: return@mapNotNull null
                                    val source = config.source as? PluginTtsSource ?: return@mapNotNull null
                                    tts.copy(
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
                            if (selectedItems.any { it.isEnabled }) {
                                SystemTtsService.notifyUpdateConfig()
                            }
                            context.longToast(R.string.batch_switch_done)
                            isBatchEditMode = false
                            selectedTtsIds = emptySet()
                        }
                    },
                    enabled = targetPluginId.isNotEmpty()
                ) {
                    Text(stringResource(R.string.switch_plugin))
                }
            },
            dismissButton = {
                TextButton(onClick = { showBatchSwitchPluginDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    // ===== 合并分组对话框 =====
    var showMergeGroupsDialog by remember { mutableStateOf(false) }
    var mergeGroupsCandidates by remember { mutableStateOf<List<GroupTreeNode>>(emptyList()) }
    if (showMergeGroupsDialog) {
        var selectedTargetGroupId by remember { mutableStateOf<Long>(mergeGroupsCandidates.firstOrNull()?.group?.id ?: 0L) }
        AlertDialog(
            onDismissRequest = { showMergeGroupsDialog = false },
            title = { Text(stringResource(R.string.merge_groups)) },
            text = {
                Column {
                    Text("选择目标分组，其他选中分组的内容将合并到该分组：")
                    Spacer(Modifier.height(8.dp))
                    mergeGroupsCandidates.forEach { node ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedTargetGroupId = node.group.id
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = selectedTargetGroupId == node.group.id,
                                onClick = { selectedTargetGroupId = node.group.id }
                            )
                            Text(
                                text = node.group.name.ifBlank { stringResource(R.string.unnamed) },
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showMergeGroupsDialog = false
                        scope.launch {
                            val movedCount = withIO {
                                vm.mergeSelectedGroups(
                                    selectedGroupNodes = mergeGroupsCandidates,
                                    targetGroupId = selectedTargetGroupId
                                )
                            }
                            context.longToast(
                                if (movedCount > 0)
                                    "已合并 $movedCount 条发音人到目标分组"
                                else
                                    "没有可合并的内容"
                            )
                            isBatchEditMode = false
                            selectedTtsIds = emptySet()
                        }
                    },
                    enabled = selectedTargetGroupId != 0L
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showMergeGroupsDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    var showOptions by rememberSaveable { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NavTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    if (isSearchMode) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        ) {
                            SearchTextField(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                                value = searchKeyword,
                                onValueChange = { vm.setSearchKeyword(it) },
                                searchType = searchType,
                                onSearchTypeChange = { vm.setSearchType(it) }
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(id = R.string.system_tts),
                            modifier = Modifier.clickable {
                                val qaId = AppConfig.quickAccessTtsId.value
                                if (qaId == -1L) {
                                    context.toast("未设置快捷音色，请在列表中点击 ⋮ 设为快捷音色")
                                } else {
                                    val qaItem = dbm.systemTtsV2.all.find { it.id == qaId }
                                    if (qaItem == null) {
                                        context.toast("快捷音色已不存在，请重新设置")
                                        AppConfig.quickAccessTtsId.value = -1L
                                    } else {
                                        navigateToEdit(qaItem)
                                    }
                                }
                            }
                        )
                    }
                }, actions = {
                    if (isBatchEditMode) {
                        // 批量编辑模式下的操作按钮
                        val selectedCount = selectedTtsIds.size
                        Text(
                            text = stringResource(R.string.selected_count, selectedCount),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            enabled = selectedCount > 0,
                            onClick = { showBatchDeleteDialog = true }
                        ) {
                            Icon(Icons.Default.Delete, stringResource(id = R.string.batch_delete))
                        }
                        IconButton(
                            enabled = selectedCount > 0,
                            onClick = { 
                                val selectedItems = models.flatMap { it.allTts() }.filter { selectedTtsIds.contains(it.id) }
                                if (selectedItems.isNotEmpty()) {
                                    batchTagPreselected = selectedItems.toSet()
                                    showBatchTagDialog = selectedItems
                                }
                            }
                        ) {
                            Icon(Icons.Default.Label, stringResource(id = R.string.batch_assign_tags))
                        }
                        IconButton(
                            enabled = selectedCount > 0,
                            onClick = { showBatchSwitchPluginDialog = true }
                        ) {
                            Icon(Icons.Default.Extension, stringResource(id = R.string.batch_switch_plugin))
                        }
                        IconButton(
                            enabled = selectedCount > 0,
                            onClick = {
                                val fullySelectedGroups = vm.getFullySelectedGroups(selectedTtsIds)
                                when {
                                    fullySelectedGroups.size < 2 ->
                                        context.toast("请完整选择至少两个分组")
                                    fullySelectedGroups.map { it.group.parentGroupId }.distinct().size != 1 ->
                                        context.toast("选中的分组必须在同一层级")
                                    else -> {
                                        mergeGroupsCandidates = fullySelectedGroups
                                        showMergeGroupsDialog = true
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.CallMerge, stringResource(id = R.string.merge_groups))
                        }
                        TextButton(onClick = {
                            isBatchEditMode = false
                            selectedTtsIds = emptySet()
                        }) {
                            Text(stringResource(R.string.cancel))
                        }
                    } else if (isSearchMode) {
                        IconButton(onClick = { 
                            isSearchMode = false 
                            vm.setSearchKeyword("")
                        }) {
                            Icon(Icons.Default.Close, stringResource(id = R.string.close))
                        }
                    } else {
                        IconButton(onClick = {
                            // 完全重启应用，停止所有服务并重新启动
                            val intent = android.content.Intent(context, com.github.jing332.tts_server_android.compose.RestartActivity::class.java)
                            context.startActivity(intent)
                        }) {
                            Icon(Icons.Default.Refresh, stringResource(id = R.string.restart))
                        }
                        IconButton(onClick = { isSearchMode = true }) {
                            Icon(Icons.Default.Search, stringResource(id = R.string.search))
                        }
                        IconButton(onClick = {
                            aiRulePreviewExpanded = false
                            showAiGenerateConfigDialog = true
                        }) {
                            Icon(Icons.Default.SmartToy, stringResource(id = R.string.ai_generate_config))
                        }

                        IconButton(onClick = {
                            isBatchEditMode = true
                            selectedTtsIds = emptySet()
                        }) {
                            Icon(Icons.Default.EditNote, stringResource(id = R.string.batch_edit))
                        }
                        IconButton(onClick = { showOptions = true }) {
                            Icon(Icons.Default.MoreVert, stringResource(id = R.string.more_options))
                            MenuMoreOptions(
                                expanded = showOptions,
                                onDismissRequest = { showOptions = false },
                                onExportAll = { showGroupExportSheet = models }
                            )
                        }
                    }
                })
        },

    ) { paddingValues ->
        Box(Modifier.padding(top = paddingValues.calculateTopPadding())) {
            ControlBottomBarVisibility(listState, LocalBottomBarBehavior.current)
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .reorderable(state = reorderState),
                state = listState
            ) {
                fun LazyListScope.groupTreeNode(node: GroupTreeNode, level: Int = 0) {
                        val g = node.group
                        val allTts = node.allTts()
                        val checkState = allTts.count { it.isEnabled }.sizeToToggleableState(allTts.size)
                        val key = "g_${g.id}"
                        val groupDragModifier = if (searchKeyword.isNotEmpty()) Modifier
                        else Modifier.detectReorderAfterLongPress(reorderState)

                        stickyHeader(key = key) {
                            ShadowedDraggableItem(reorderableState = reorderState, key = key) {
                                if (isBatchEditMode) {
                                    val groupTtsIds = node.allTts().map { it.id }.toSet()
                                    val selectedInGroup = groupTtsIds.count { selectedTtsIds.contains(it) }
                                    val groupCheckedState = when {
                                        selectedInGroup == 0 -> androidx.compose.ui.state.ToggleableState.Off
                                        selectedInGroup == groupTtsIds.size -> androidx.compose.ui.state.ToggleableState.On
                                        else -> androidx.compose.ui.state.ToggleableState.Indeterminate
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = (level * 20).dp)
                                    ) {
                                        androidx.compose.material3.TriStateCheckbox(
                                            state = groupCheckedState,
                                            onClick = {
                                                selectedTtsIds = if (groupCheckedState == androidx.compose.ui.state.ToggleableState.On) {
                                                    selectedTtsIds - groupTtsIds
                                                } else {
                                                    selectedTtsIds + groupTtsIds
                                                }
                                            }
                                        )
                                        Group(
                                            modifier = Modifier.weight(1f),
                                            name = g.name,
                                            group = g,
                                            isExpanded = g.isExpanded,
                                            toggleableState = checkState,
                                            onToggleableStateChange = {},
                                            onClick = {
                                                vm.updateGroupExpanded(g, !g.isExpanded)
                                            },
                                            onDelete = {},
                                            onRename = {},
                                            onCopy = {},
                                            onEditAudioParams = {},
                                            onSort = {},
                                            onCreateSubGroup = {},
                                            onBatchAssignTags = {},
                                            onReleaseSubGroup = {},
                                            onConvertToSubGroup = {},
                                            onExtractSubGroup = {},
                                            onExport = {}
                                        )
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .detectReorderAfterLongPress(reorderState),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Group(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = (level * 20).dp),
                                            name = g.name,
                                            group = g,
                                            isExpanded = g.isExpanded,
                                            toggleableState = checkState,
                                            onToggleableStateChange = {
                                                vm.updateGroupEnable(node, it)
                                            },
                                            onClick = {
                                                vm.updateGroupExpanded(g, !g.isExpanded)
                                            },
                                            onDelete = {
                                                fun deleteNode(target: GroupTreeNode) {
                                                    target.children.forEach { deleteNode(it) }
                                                    dbm.systemTtsV2.delete(*target.list.toTypedArray())
                                                    dbm.systemTtsV2.deleteGroup(target.group)
                                                }
                                                deleteNode(node)
                                            },
                                            onRename = {
                                                dbm.systemTtsV2.updateGroup(g.copy(name = it))
                                            },
                                            onCopy = {
                                                scope.launch {
                                                    val group = g.copy(
                                                        id = System.currentTimeMillis(),
                                                        name = it.ifBlank { context.getString(R.string.unnamed) },
                                                        parentGroupId = g.parentGroupId
                                                    )
                                                    dbm.systemTtsV2.insertGroup(group)
                                                    dbm.systemTtsV2.getByGroup(g.id)
                                                        .forEachIndexed { index, tts ->
                                                            dbm.systemTtsV2.insert(
                                                                tts.copy(
                                                                    id = System.currentTimeMillis() + index,
                                                                    groupId = group.id
                                                                )
                                                            )
                                                        }
                                                }
                                            },
                                            onEditAudioParams = {
                                                groupAudioParamsDialog = g
                                            },
                                            onSort = {
                                                showSortDialog = node.list to null
                                            },
                                            onCreateSubGroup = {
                                                showCreateSubGroup = g.id
                                            },
                                            onBatchAssignTags = {
                                                showBatchTagDialog = node.list
                                            },
                                            onReleaseSubGroup = {
                                                showReleaseSubGroup = g
                                            },
                                            onConvertToSubGroup = {
                                                showConvertToSubGroup = g
                                            },
                                            onExtractSubGroup = {
                                                showExtractSubGroup = g
                                            },
                                            onAddChildGroup = if (level == 0) {
                                                {
                                                    addGroupParentId = g.id
                                                    addGroupDialog = true
                                                }
                                            } else null,
                                            onPromoteToRoot = if (g.parentGroupId != 0L) {
                                                {
                                                    scope.launch {
                                                        withIO { vm.moveGroupToRoot(g) }
                                                    }
                                                }
                                            } else null,
                                            onMoveToParent = if (g.parentGroupId == 0L && models.any { it.group.id != g.id }) {
                                                {
                                                    moveGroupDialog = g
                                                }
                                            } else null,
                                            onExport = {
                                                showGroupExportSheet = listOf(node)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        if (g.isExpanded) {
                            val hasSubGroups = node.list.any { it.categoryPath.isNotBlank() }

                            if (!hasSubGroups) {
                                itemsIndexed(node.list.sortedBy { it.order },
                                    key = { _, v -> "${g.id}_${v.id}" }) { _, item ->
                                    ShadowedDraggableItem(
                                        reorderableState = reorderState,
                                        key = "${g.id}_${item.id}"
                                    ) {
                                        val descriptor = remember(item) {
                                            ItemDescriptorFactory.from(context, item)
                                        }
                                        if (isBatchEditMode) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 8.dp)
                                            ) {
                                                Checkbox(
                                                    checked = selectedTtsIds.contains(item.id),
                                                    onCheckedChange = { checked ->
                                                        selectedTtsIds = if (checked) {
                                                            selectedTtsIds + item.id
                                                        } else {
                                                            selectedTtsIds - item.id
                                                        }
                                                    }
                                                )
                                                Item(
                                                    reorderState = reorderState,
                                                    modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                                                    name = item.displayName,
                                                    tagName = descriptor.tagName,
                                                    type = descriptor.type,
                                                    standby = descriptor.standby,
                                                    enabled = item.isEnabled,
                                                    onEnabledChange = {
                                                        vm.updateTtsEnabled(item, it)
                                                        if (it) SystemTtsService.notifyUpdateConfig()
                                                    },
                                                    desc = descriptor.desc,
                                                    params = descriptor.bottom,
                                                    onClick = {
                                                        selectedTtsIds = if (selectedTtsIds.contains(item.id)) {
                                                            selectedTtsIds - item.id
                                                        } else {
                                                            selectedTtsIds + item.id
                                                        }
                                                    },
                                                    onLongClick = {},
                                                    onCopy = {},
                                                    onDelete = {},
                                                    onEdit = {},
                                                    onAudition = {},
                                                    isInSubGroup = false,
                                                    onExport = {},
                                                    onMoveToSubGroup = {},
                                                    onSwitchTag = {},
                                                    onSetQuickAccess = {},
                                                    isQuickAccess = false
                                                )
                                            }
                                        } else {
                                            Item(reorderState = reorderState,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 4.dp
                                                ),
                                                name = item.displayName,
                                                tagName = descriptor.tagName,
                                                type = descriptor.type,
                                                standby = descriptor.standby,
                                                enabled = item.isEnabled,
                                                onEnabledChange = {
                                                    vm.updateTtsEnabled(item, it)
                                                    if (it) SystemTtsService.notifyUpdateConfig()
                                                },
                                                desc = descriptor.desc,
                                                params = descriptor.bottom,
                                                onClick = { showQuickEdit = item },
                                                onLongClick = {
                                                    if (AppConfig.quickAccessTtsId.value == item.id) {
                                                        AppConfig.quickAccessTtsId.value = -1L
                                                        context.toast("已取消快捷音色")
                                                    } else {
                                                        AppConfig.quickAccessTtsId.value = item.id
                                                        context.toast("已设为快捷音色: ${item.displayName}")
                                                    }
                                                },
                                                onCopy = {
                                                    navigateToEdit(item.copy(id = System.currentTimeMillis()))
                                                },
                                                onDelete = { deleteTts = item },
                                                onEdit = { navigateToEdit(item) },
                                                onAudition = {
                                                    if (item.config is TtsConfigurationDTO) {
                                                        showAuditionDialog = item.copy()
                                                    } else
                                                        context.toast(R.string.not_support_audition)
                                                },
                                                onExport = {
                                                    showExportSheet =
                                                        listOf(item.copy(groupId = AbstractListGroup.DEFAULT_GROUP_ID))
                                                },
                                                onMoveToSubGroup = {
                                                    showMoveToSubGroup = item
                                                },
                                                onSwitchTag = { switchSpeechTarget(item) },
                                                onSetQuickAccess = {
                                                    if (AppConfig.quickAccessTtsId.value == item.id) {
                                                        AppConfig.quickAccessTtsId.value = -1L
                                                        context.toast("已取消快捷音色")
                                                    } else {
                                                        AppConfig.quickAccessTtsId.value = item.id
                                                        context.toast("已设为快捷音色: ${item.displayName}")
                                                    }
                                                },
                                                isQuickAccess = AppConfig.quickAccessTtsId.value == item.id
                                            )
                                        }
                                    }
                                }
                            } else {
                                val tree = buildSubCategoryTree(node.list)
                                val flattened = flattenSubCategoryTree(tree)
                                val visibleItems = mutableListOf<FlattenedCategoryItem>()
                                var skipLevel = Int.MAX_VALUE
                                for (fItem in flattened) {
                                    when (fItem) {
                                        is FlattenedCategoryItem.SubGroupHeader -> {
                                            if (fItem.node.level <= skipLevel) {
                                                skipLevel = Int.MAX_VALUE
                                            }
                                            if (fItem.node.level > skipLevel) {
                                                continue
                                            }
                                            visibleItems.add(fItem)
                                            if (!expandedSubGroups.contains(fItem.node.fullPath)) {
                                                skipLevel = fItem.node.level
                                            }
                                        }
                                        is FlattenedCategoryItem.TtsItem -> {
                                            if (fItem.displayLevel <= skipLevel) {
                                                visibleItems.add(fItem)
                                            }
                                        }
                                    }
                                }

                                itemsIndexed(visibleItems,
                                    key = { _, v ->
                                        when (v) {
                                            is FlattenedCategoryItem.SubGroupHeader -> "sub_${g.id}_${v.node.fullPath}"
                                            is FlattenedCategoryItem.TtsItem -> "item_${g.id}_${v.categoryPath}_${v.item.id}"
                                        }
                                    }) { _, fItem ->
                                    when (fItem) {
                                        is FlattenedCategoryItem.SubGroupHeader -> {
                                            val subKey = "sub_${g.id}_${fItem.node.fullPath}"
                                            val subDragModifier = if (searchKeyword.isNotEmpty()) Modifier
                                                else Modifier.detectReorderAfterLongPress(reorderState)
                                            ShadowedDraggableItem(
                                                reorderableState = reorderState,
                                                key = subKey
                                            ) { _ ->
                                                val subItems = fItem.node.items
                                                if (isBatchEditMode) {
                                                    val subTtsIds = fItem.node.allItems().map { it.id }.toSet()
                                                    val selectedInSub = subTtsIds.count { selectedTtsIds.contains(it) }
                                                    val subCheckedState = when {
                                                        selectedInSub == 0 -> androidx.compose.ui.state.ToggleableState.Off
                                                        selectedInSub == subTtsIds.size -> androidx.compose.ui.state.ToggleableState.On
                                                        else -> androidx.compose.ui.state.ToggleableState.Indeterminate
                                                    }
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .background(
                                                                if (fItem.node.level == 0) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                                                else MaterialTheme.colorScheme.surface
                                                            )
                                                            .clickable {
                                                                expandedSubGroups = if (expandedSubGroups.contains(fItem.node.fullPath)) {
                                                                    expandedSubGroups - fItem.node.fullPath
                                                                } else {
                                                                    expandedSubGroups + fItem.node.fullPath
                                                                }
                                                            }
                                                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                                                    ) {
                                                        androidx.compose.material3.TriStateCheckbox(
                                                            state = subCheckedState,
                                                            onClick = {
                                                                selectedTtsIds = if (subCheckedState == androidx.compose.ui.state.ToggleableState.On) {
                                                                    selectedTtsIds - subTtsIds
                                                                } else {
                                                                    selectedTtsIds + subTtsIds
                                                                }
                                                            }
                                                        )
                                                        val rotationAngle by androidx.compose.animation.core.animateFloatAsState(
                                                            targetValue = if (expandedSubGroups.contains(fItem.node.fullPath)) 0f else -45f,
                                                            label = ""
                                                        )
                                                        Icon(
                                                            imageVector = Icons.Default.ExpandCircleDown,
                                                            contentDescription = if (expandedSubGroups.contains(fItem.node.fullPath)) "收起" else "展开",
                                                            modifier = Modifier
                                                                .size(20.dp)
                                                                .rotate(rotationAngle)
                                                                .graphicsLayer { rotationZ = rotationAngle },
                                                            tint = MaterialTheme.colorScheme.primary
                                                        )
                                                        Text(
                                                            text = fItem.node.name,
                                                            style = when (fItem.node.level) {
                                                                0 -> MaterialTheme.typography.titleMedium
                                                                1 -> MaterialTheme.typography.bodyLarge
                                                                else -> MaterialTheme.typography.bodyMedium
                                                            },
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                            modifier = Modifier
                                                                .padding(start = 8.dp)
                                                                .weight(1f)
                                                        )
                                                    }
                                                } else {
                                                    val subEnabled = subItems.isNotEmpty() && subItems.all { it.isEnabled }
                                                    SubGroupHeader(
                                                        modifier = subDragModifier,
                                                        name = fItem.node.name,
                                                        level = fItem.node.level,
                                                        isExpanded = expandedSubGroups.contains(fItem.node.fullPath),
                                                        enabled = subEnabled,
                                                        onEnabledChange = { enabled ->
                                                            scope.launch {
                                                                subItems.forEach { item ->
                                                                    if (item.isEnabled != enabled) {
                                                                        dbm.systemTtsV2.update(
                                                                            item.copy(isEnabled = enabled)
                                                                        )
                                                                    }
                                                                }
                                                                if (enabled) SystemTtsService.notifyUpdateConfig()
                                                            }
                                                        },
                                                        onClick = {
                                                            expandedSubGroups = if (expandedSubGroups.contains(fItem.node.fullPath)) {
                                                                expandedSubGroups - fItem.node.fullPath
                                                            } else {
                                                                expandedSubGroups + fItem.node.fullPath
                                                            }
                                                        },
                                                        onRename = {
                                                            showSubGroupRename = subItems to fItem.node.fullPath
                                                        },
                                                        onEditAudioParams = {
                                                            showSubGroupAudioParams = g to fItem.node.fullPath
                                                        },
                                                        onSort = {
                                                            showSortDialog = subItems to node.list
                                                        },
                                                        onBatchAssignTags = {
                                                            showSubGroupBatchTag = subItems
                                                        },
                                                        onDelete = {
                                                            scope.launch {
                                                                dbm.systemTtsV2.delete(*subItems.toTypedArray())
                                                            }
                                                        },
                                                        onExport = {
                                                            showExportSheet = subItems.map {
                                                                it.copy(groupId = AbstractListGroup.DEFAULT_GROUP_ID)
                                                            }
                                                        },
                                                        onExtractToGroup = {
                                                            showSubGroupExtractToGroup = g to fItem.node.fullPath
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                        is FlattenedCategoryItem.TtsItem -> {
                                            val item = fItem.item
                                            val itemKey = "item_${g.id}_${fItem.categoryPath}_${item.id}"
                                            val itemDragModifier = if (searchKeyword.isNotEmpty()) Modifier
                                                else Modifier.detectReorderAfterLongPress(reorderState)
                                            ShadowedDraggableItem(
                                                reorderableState = reorderState,
                                                key = itemKey
                                            ) { _ ->
                                                val descriptor = remember(item) {
                                                    ItemDescriptorFactory.from(context, item)
                                                }
                                                if (isBatchEditMode) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 8.dp)
                                                    ) {
                                                        Checkbox(
                                                            checked = selectedTtsIds.contains(item.id),
                                                            onCheckedChange = { checked ->
                                                                selectedTtsIds = if (checked) {
                                                                    selectedTtsIds + item.id
                                                                } else {
                                                                    selectedTtsIds - item.id
                                                                }
                                                            }
                                                        )
                                                        Item(
                                                            reorderState = reorderState,
                                                            modifier = Modifier.weight(1f).padding(vertical = 4.dp),
                                                            name = item.displayName,
                                                            tagName = descriptor.tagName,
                                                            type = descriptor.type,
                                                            standby = descriptor.standby,
                                                            enabled = item.isEnabled,
                                                            onEnabledChange = {
                                                                vm.updateTtsEnabled(item, it)
                                                                if (it) SystemTtsService.notifyUpdateConfig()
                                                            },
                                                            desc = descriptor.desc,
                                                            params = descriptor.bottom,
                                                            onClick = {
                                                                selectedTtsIds = if (selectedTtsIds.contains(item.id)) {
                                                                    selectedTtsIds - item.id
                                                                } else {
                                                                    selectedTtsIds + item.id
                                                                }
                                                            },
                                                            onLongClick = {},
                                                            onCopy = {},
                                                            onDelete = {},
                                                            onEdit = {},
                                                            onAudition = {},
                                                            isInSubGroup = fItem.displayLevel > 0,
                                                            onExport = {},
                                                            onMoveToSubGroup = {},
                                                            onSwitchTag = {},
                                                            onSetQuickAccess = {},
                                                            isQuickAccess = false
                                                        )
                                                    }
                                                } else {
                                                    Item(
                                                        reorderState = reorderState,
                                                        modifier = itemDragModifier.padding(
                                                            start = 8.dp,
                                                            end = 8.dp,
                                                            top = 4.dp,
                                                            bottom = 4.dp
                                                        ),
                                                        name = item.displayName,
                                                        tagName = descriptor.tagName,
                                                        type = descriptor.type,
                                                        standby = descriptor.standby,
                                                        enabled = item.isEnabled,
                                                        onEnabledChange = {
                                                            vm.updateTtsEnabled(item, it)
                                                            if (it) SystemTtsService.notifyUpdateConfig()
                                                        },
                                                        desc = descriptor.desc,
                                                        params = descriptor.bottom,
                                                        onClick = { showQuickEdit = item },
                                                        onLongClick = {
                                                            if (AppConfig.quickAccessTtsId.value == item.id) {
                                                                AppConfig.quickAccessTtsId.value = -1L
                                                                context.toast("已取消快捷音色")
                                                            } else {
                                                                AppConfig.quickAccessTtsId.value = item.id
                                                                context.toast("已设为快捷音色: ${item.displayName}")
                                                            }
                                                        },
                                                        onCopy = {
                                                            navigateToEdit(item.copy(id = System.currentTimeMillis()))
                                                        },
                                                        onDelete = { deleteTts = item },
                                                        onEdit = { navigateToEdit(item) },
                                                        onAudition = {
                                                            if (item.config is TtsConfigurationDTO) {
                                                                showAuditionDialog = item.copy()
                                                            } else
                                                                context.toast(R.string.not_support_audition)
                                                        },
                                                        onExport = {
                                                            showExportSheet =
                                                                listOf(item.copy(groupId = AbstractListGroup.DEFAULT_GROUP_ID))
                                                        },
                                                        onMoveToSubGroup = {
                                                            showMoveToSubGroup = item
                                                        },
                                                        onSwitchTag = { switchSpeechTarget(item) },
                                                        onSetQuickAccess = {
                                                            if (AppConfig.quickAccessTtsId.value == item.id) {
                                                                AppConfig.quickAccessTtsId.value = -1L
                                                                context.toast("已取消快捷音色")
                                                            } else {
                                                                AppConfig.quickAccessTtsId.value = item.id
                                                                context.toast("已设为快捷音色: ${item.displayName}")
                                                            }
                                                        },
                                                        isQuickAccess = AppConfig.quickAccessTtsId.value == item.id
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            node.children.forEach { child ->
                                groupTreeNode(child, level + 1)
                            }
                        }
                    }

                models.forEach { node ->
                    groupTreeNode(node)
                }

                item {
                    Spacer(Modifier.padding(bottom = AppDefaultProperties.LIST_END_PADDING))
                }
            }

            FloatingAddConfigButtonGroup(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                visible = true,
                addBgm = {
                    navigateToEdit(SystemTtsV2(config = BgmConfiguration()))
                },
                addLocal = {
                    navigateToEdit(
                        SystemTtsV2(
                            config = TtsConfigurationDTO(
                                source = LocalTtsSource(locale = AppConst.localeCode)
                            )
                        )
                    )
                },
                addPlugin = {
                    addPluginDialog = true
                },
                addGroup = {
                    addGroupParentId = 0L
                    addGroupDialog = true
                }
            )

            LaunchedEffect(key1 = Unit) {
                withIO {
                    vm.checkListData(context)
                }
            }

        }
    }
}


// ==================== AI生成配置 ====================

private const val AI_GENERATE_CONFIG_API_FILE_NAME = "ai_generate_config_api.txt"

private fun aiGenerateConfigApiFile(context: Context): File {
    return File(context.filesDir, AI_GENERATE_CONFIG_API_FILE_NAME)
}

private fun readAiGenerateConfigApiText(context: Context): String {
    val file = aiGenerateConfigApiFile(context)
    return if (file.exists() && file.isFile) {
        file.readText().trim()
    } else {
        ""
    }
}

private fun writeAiGenerateConfigApiText(context: Context, text: String) {
    val file = aiGenerateConfigApiFile(context)
    file.parentFile?.mkdirs()
    file.writeText(text.trim())
}

private data class AiRuleRoleTag(
    val groupName: String,
    val displayName: String,
    val tag: String,
    val tagName: String,
    val index: Int,
)

private fun normalizeAiTagName(raw: String): String {
    return raw
        .replace("【", "")
        .replace("】", "")
        .replace("[", "")
        .replace("]", "")
        .replace("（", "")
        .replace("）", "")
        .trim()
}

private fun parseAiRuleRoleTags(rule: SpeechRule): List<AiRuleRoleTag> {
    val result = mutableListOf<AiRuleRoleTag>()

    val roleGroups = listOf(
        "女童",
        "男童",
        "少女",
        "少年",
        "女青年",
        "男青年",
        "女中年",
        "男中年",
        "女老年",
        "男老年"
    )

    rule.tags.forEach { (tag, tagNameRaw) ->
        val tagName = normalizeAiTagName(tagNameRaw)
        val lowerTag = tag.lowercase()

        if (tagName.isBlank()) return@forEach
        if (lowerTag == "narration" || lowerTag == "narrator") return@forEach
        if (lowerTag == "duihua" || lowerTag == "duihuaa" || lowerTag == "duihuab") return@forEach
        if (lowerTag.startsWith("localsound")) return@forEach
        if (tagName.contains("旁白")) return@forEach
        if (tagName.contains("音效")) return@forEach
        if (tagName.contains("括号")) return@forEach

        val matchedGroup = roleGroups.firstOrNull { group ->
            tagName.contains(group)
        } ?: return@forEach

        val index = Regex("(\\d{1,3})")
            .find(tagName)
            ?.value
            ?.toIntOrNull()
            ?: 0

        val gender = when {
            matchedGroup.startsWith("女") || matchedGroup == "少女" -> "女"
            matchedGroup.startsWith("男") || matchedGroup == "少年" -> "男"
            else -> ""
        }

        val displayName = if (tagName.contains("/")) {
            tagName
        } else if (gender.isNotBlank()) {
            "$gender/$matchedGroup" + if (index > 0) "%02d".format(index) else ""
        } else {
            tagName
        }

        result += AiRuleRoleTag(
            groupName = matchedGroup,
            displayName = displayName,
            tag = tag,
            tagName = tagNameRaw,
            index = index
        )
    }

    return result.sortedWith(
        compareBy<AiRuleRoleTag> { it.groupName }
            .thenBy { it.index }
            .thenBy { it.displayName }
    )
}

private data class AiPluginVoicePreview(
    val voiceId: String,
    val voiceName: String,
    val icon: String? = null
)

private fun extractAiPluginVoicePreview(pluginCode: String): List<AiPluginVoicePreview> {
    val result = mutableListOf<AiPluginVoicePreview>()

    val cloneMarker = pluginCode.indexOf("CLONE_VOICE_NAMES")
    if (cloneMarker >= 0) {
        val openIndex = pluginCode.indexOf("[", cloneMarker)
        val closeIndex = pluginCode.indexOf("];", openIndex)
        if (openIndex >= 0 && closeIndex > openIndex) {
            val block = pluginCode.substring(openIndex + 1, closeIndex)

            Regex("\"((?:\\\\.|[^\"])*)\"|'((?:\\\\.|[^'])*)'")
                .findAll(block)
                .mapNotNull { match ->
                    val value = match.groups[1]?.value ?: match.groups[2]?.value
                    value
                        ?.replace("\\\"", "\"")
                        ?.replace("\\'", "'")
                        ?.trim()
                        ?.takeIf { it.isNotBlank() }
                }
                .forEachIndexed { index, name ->
                    val safeName = name
                        .replace(Regex("[()（）\\s/\\\\.:：，,;；#?&=\\[\\]{}]+"), "_")
                        .replace(Regex("_+"), "_")
                        .trim('_')
                        .let { if (it.length > 70) it.substring(0, 70) else it }

                    result += AiPluginVoicePreview(
                        voiceId = "ggref_${index}_$safeName",
                        voiceName = name
                    )
                }

            return result.distinctBy { it.voiceId }
        }
    }

    val voiceObjectMarker = pluginCode.indexOf("'VOICES'")
        .takeIf { it >= 0 }
        ?: pluginCode.indexOf("\"VOICES\"")
            .takeIf { it >= 0 }
        ?: pluginCode.indexOf("VOICES")

    if (voiceObjectMarker >= 0) {
        val area = pluginCode.substring(voiceObjectMarker, minOf(pluginCode.length, voiceObjectMarker + 12000))
        Regex("['\"]([^'\"]{2,120})['\"]\\s*:\\s*['\"]([^'\"]{2,160})['\"]")
            .findAll(area)
            .take(500)
            .forEach { m ->
                result += AiPluginVoicePreview(
                    voiceId = m.groupValues[1],
                    voiceName = m.groupValues[2]
                )
            }
    }

    return result.distinctBy { it.voiceId }
}

private fun classifyAiVoiceGroup(text: String): String? {
    val value = text.lowercase()

    return when {
        value.contains("女童") ||
            value.contains("幼女") ||
            value.contains("女孩") ||
            value.contains("儿童") && value.contains("女") -> "女童"

        value.contains("男童") ||
            value.contains("男孩") ||
            value.contains("正太") ||
            value.contains("儿童") && value.contains("男") -> "男童"

        value.contains("少女") ||
            value.contains("女少年") -> "少女"

        value.contains("少年") ||
            value.contains("男少年") -> "少年"

        value.contains("女青年") ||
            value.contains("女青") ||
            value.contains("御姐") ||
            value.contains("少御") -> "女青年"

        value.contains("男青年") ||
            value.contains("男青") ||
            value.contains("青年") && value.contains("男") -> "男青年"

        value.contains("女中年") ||
            value.contains("女中青") ||
            value.contains("中年") && value.contains("女") -> "女中年"

        value.contains("男中年") ||
            value.contains("男中青") ||
            value.contains("青叔") ||
            value.contains("大叔") ||
            value.contains("中年") && value.contains("男") -> "男中年"

        value.contains("女老年") ||
            value.contains("女中老") ||
            value.contains("老年") && value.contains("女") ||
            value.contains("老妇") ||
            value.contains("奶奶") -> "女老年"

        value.contains("男老年") ||
            value.contains("男中老") ||
            value.contains("老年") && value.contains("男") ||
            value.contains("老者") ||
            value.contains("老头") ||
            value.contains("爷爷") -> "男老年"

        else -> null
    }
}

private fun fetchAiPluginVoicesDynamic(
    context: Context,
    plugin: Plugin,
): List<AiPluginVoicePreview> {
    val source = PluginTtsSource(
        locale = "zh-CN",
        voice = "",
        pluginId = plugin.pluginId
    )

    val engine = TtsPluginUiEngineV2(context, plugin).apply {
        eval()
        this.source = source
        onLoadData()
    }

    val locales = runCatching {
        engine.getLocales().toList()
    }.getOrDefault(emptyList())

    val locale = locales.firstOrNull { it.first == "zh-CN" }?.first
        ?: locales.firstOrNull()?.first
        ?: "zh-CN"

    return engine.getVoices(locale)
        .map {
            AiPluginVoicePreview(
                voiceId = it.id,
                voiceName = it.name,
                icon = readVoiceIconByReflection(it)
            )
        }
        .distinctBy { it.voiceId }
}

private fun aiPluginShortName(pluginName: String): String {
    val name = pluginName.trim()

    return when {
        name.contains("呱呱", ignoreCase = true) -> "呱呱"
        name.contains("mimo", ignoreCase = true) -> "MiMo"
        name.contains("微软", ignoreCase = true) -> "微软"
        name.contains("火山", ignoreCase = true) -> "火山"
        name.contains("豆包", ignoreCase = true) -> "豆包"
        name.contains("猫箱", ignoreCase = true) -> "猫箱"
        name.contains("猫", ignoreCase = true) -> "猫箱"
        name.isNotBlank() -> name.take(4)
        else -> "插件"
    }
}

private fun resolvePluginSampleRate(targetPlugin: Plugin): Int {
    val code = targetPlugin.code.toString()
    val markerIndex = code.indexOf("getAudioSampleRate")
    if (markerIndex < 0) return 16000

    val area = code.substring(markerIndex, minOf(code.length, markerIndex + 800))
    return Regex("return\\s+(\\d{4,6})")
        .find(area)
        ?.groupValues
        ?.getOrNull(1)
        ?.toIntOrNull()
        ?.takeIf { it > 0 }
        ?: 16000
}

private fun resolvePluginNeedDecode(targetPlugin: Plugin): Boolean {
    val code = targetPlugin.code.toString()
    val markerIndex = code.indexOf("getAudioFormat")
    if (markerIndex < 0) return true

    val area = code.substring(markerIndex, minOf(code.length, markerIndex + 800))
    val format = Regex("return\\s+['\"]([^'\"]+)['\"]")
        .find(area)
        ?.groupValues
        ?.getOrNull(1)
        ?.trim()
        ?.lowercase()
        .orEmpty()

    return when {
        format.contains("raw") -> false
        format.contains("pcm") -> false
        else -> true
    }
}

private fun resolveVoiceAvatarUri(
    packageName: String,
    voiceName: String,
    voiceId: String,
    pluginIcon: String?
): String {
    val icon = pluginIcon.orEmpty().trim()
    if (icon.isNotBlank() && (
        icon.startsWith("http://") ||
        icon.startsWith("https://") ||
        icon.startsWith("file://") ||
        icon.startsWith("content://") ||
        icon.startsWith("/")
    )) {
        return icon
    }
    return ""
}

private fun readVoiceIconByReflection(value: Any?): String? {
    if (value == null) return null

    val methodNames = listOf(
        "getIcon",
        "getAvatarUrl",
        "getAvatar",
        "getIconUrl",
        "getImageUrl",
        "getImage",
        "getCoverUrl",
        "getPhoto",
        "getPic",
        "getPortrait",
        "getFace"
    )

    methodNames.forEach { methodName ->
        runCatching {
            val method = value.javaClass.methods.firstOrNull { it.name == methodName }
            val result = method?.invoke(value)?.toString()?.trim()
            if (!result.isNullOrBlank()) return result
        }
    }

    val fieldNames = listOf(
        "icon",
        "avatarUrl",
        "avatar",
        "iconUrl",
        "imageUrl",
        "image",
        "coverUrl",
        "photo",
        "pic",
        "portrait",
        "face"
    )

    fieldNames.forEach { fieldName ->
        runCatching {
            val field = value.javaClass.declaredFields.firstOrNull { it.name == fieldName }
            field?.isAccessible = true
            val result = field?.get(value)?.toString()?.trim()
            if (!result.isNullOrBlank()) return result
        }
    }

    return null
}

private data class AiGenerateConfigMatch(
    val role: AiRuleRoleTag,
    val voice: AiPluginVoicePreview?,
)

private fun buildAiGenerateConfigMatches(
    rule: SpeechRule?,
    voices: List<AiPluginVoicePreview>,
): List<AiGenerateConfigMatch> {
    if (rule == null || voices.isEmpty()) return emptyList()

    val groupOrder = listOf(
        "女童",
        "男童",
        "少女",
        "少年",
        "女青年",
        "男青年",
        "女中年",
        "男中年",
        "女老年",
        "男老年"
    )

    val roleGrouped = parseAiRuleRoleTags(rule)
        .groupBy { it.groupName }

    val voiceGrouped = voices
        .mapNotNull { voice ->
            val group = classifyAiVoiceGroup("${voice.voiceName} ${voice.voiceId}") ?: return@mapNotNull null
            group to voice
        }
        .groupBy({ it.first }, { it.second })

    val result = mutableListOf<AiGenerateConfigMatch>()

    groupOrder.forEach { group ->
        val roles = roleGrouped[group]
            .orEmpty()
            .sortedWith(
                compareBy<AiRuleRoleTag> { it.index }
                    .thenBy { it.displayName }
            )

        val candidates = voiceGrouped[group].orEmpty()

        if (roles.isEmpty() || candidates.isEmpty()) return@forEach

        val count = minOf(roles.size, candidates.size)

        for (i in 0 until count) {
            result += AiGenerateConfigMatch(
                role = roles[i],
                voice = candidates[i]
            )
        }
    }

    return result
}

private fun deleteAiGenerateGroupTreeByName(rootName: String) {
    val root = dbm.systemTtsV2.allGroup.firstOrNull {
        it.parentGroupId == 0L && it.name == rootName
    } ?: return

    fun deleteGroupTree(group: SystemTtsGroup) {
        val children = dbm.systemTtsV2.allGroup.filter { it.parentGroupId == group.id }
        children.forEach { child ->
            deleteGroupTree(child)
        }

        val ttsList = dbm.systemTtsV2.getTtsListByGroupId(group.id)
        if (ttsList.isNotEmpty()) {
            dbm.systemTtsV2.delete(*ttsList.toTypedArray())
        }

        dbm.systemTtsV2.deleteGroup(group)
    }

    deleteGroupTree(root)
}

private fun findOrCreateAiGenerateGroup(
    name: String,
    parentGroupId: Long,
    order: Int,
): SystemTtsGroup {
    val exists = dbm.systemTtsV2.allGroup.firstOrNull {
        it.parentGroupId == parentGroupId && it.name == name
    }

    if (exists != null) return exists

    val group = SystemTtsGroup(
        id = System.currentTimeMillis() + order,
        name = name,
        order = order,
        parentGroupId = parentGroupId,
        isExpanded = true
    )

    dbm.systemTtsV2.insertGroup(group)
    return group
}

private fun buildAiVoiceClassifyPrompt(
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): String {
    val voiceText = voices
        .take(500)
        .joinToString("\n") { voice ->
            "${voice.voiceId} | ${voice.voiceName}"
        }

    return """
你是中文有声书 TTS 声音分类助手。
请根据 voiceName 的语义判断每个声音属于哪一种角色类型。

只允许使用这些 group：
女童、男童、少女、少年、女青年、男青年、女中年、男中年、女老年、男老年、unknown。

分类语义参考：
- 女童：女童、幼女、小女孩、儿童女声、稚嫩女声、萝莉、童声女。
- 男童：男童、男孩、正太、儿童男声、稚嫩男声、童声男。
- 少女：少女、女少年、青春少女、灵动少女、元气少女、甜美少女。
- 少年：少年、男少年、青涩少年、清亮少年、热血少年。
- 女青年：女青年、青年女声、御姐、少御、女主、女配、年轻女性、清冷女声、甜美女声、成熟女声但未到中年。
- 男青年：男青年、青年男声、男主、公子、书生、年轻男性、磁性男声、清朗男声、阳光男声。
- 女中年：女中年、熟女、母亲、夫人、中年女性、端庄女声、成熟女性、慈爱女声。
- 男中年：男中年、大叔、青叔、成熟男性、父亲、将军、老板、稳重男声、厚重男声。
- 女老年：女老年、老妇、奶奶、老妪、慈祥老人女声、苍老女声。
- 男老年：男老年、老者、爷爷、老头、长者、苍老男声、老年男性。
- unknown：完全无法判断性别年龄的声音。

要求：
1. 必须只输出 JSON。
2. JSON key 必须是 voiceId 原文。
3. group 必须是允许列表之一。
4. 不要解释，不要 Markdown。

插件名称：
$pluginName

声音列表：
$voiceText

输出格式：
{
  "voice_id_1": {"group": "女青年"},
  "voice_id_2": {"group": "男中年"},
  "voice_id_3": {"group": "unknown"}
}
""".trimIndent()
}

private fun callAiClassifyVoiceGroupsApi(
    context: Context,
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): Map<String, String> {
    val cfg = parseAiGenerateApiConfig(context) ?: return emptyMap()
    if (voices.isEmpty()) return emptyMap()

    val prompt = buildAiVoiceClassifyPrompt(pluginName, voices)

    return runCatching {
        val body = JSONObject()
            .put("model", cfg.model)
            .put(
                "messages",
                JSONArray()
                    .put(
                        JSONObject()
                            .put("role", "system")
                            .put("content", "你只输出合法 JSON。")
                    )
                    .put(
                        JSONObject()
                            .put("role", "user")
                            .put("content", prompt)
                    )
            )
            .put("temperature", 0.1)

        val conn = URL(cfg.endpoint).openConnection() as java.net.HttpURLConnection
        conn.requestMethod = "POST"
        conn.connectTimeout = 15000
        conn.readTimeout = 30000
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Authorization", "Bearer ${cfg.key}")

        OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use {
            it.write(body.toString())
        }

        val responseText = if (conn.responseCode in 200..299) {
            conn.inputStream.bufferedReader().use { it.readText() }
        } else {
            conn.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        }

        if (conn.responseCode !in 200..299) {
            aiGenerateMatchLastError = "AI声音分类 HTTP ${conn.responseCode}: " + responseText.take(300)
            return@runCatching emptyMap<String, String>()
        }

        val root = JSONObject(responseText)
        val content = root
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")

        val jsonStart = content.indexOf("{")
        val jsonEnd = content.lastIndexOf("}")

        if (jsonStart < 0 || jsonEnd <= jsonStart) {
            aiGenerateMatchLastError = "AI声音分类返回内容不是 JSON：" + content.take(300)
            return@runCatching emptyMap<String, String>()
        }

        val obj = JSONObject(content.substring(jsonStart, jsonEnd + 1))
        val allowed = setOf(
            "女童",
            "男童",
            "少女",
            "少年",
            "女青年",
            "男青年",
            "女中年",
            "男中年",
            "女老年",
            "男老年"
        )

        val result = mutableMapOf<String, String>()

        obj.keys().forEach { voiceId ->
            val item = obj.optJSONObject(voiceId)
            val group = item?.optString("group").orEmpty().trim()

            if (group in allowed) {
                result[voiceId] = group
            }
        }

        result
    }.getOrElse {
        aiGenerateMatchLastError = it.message ?: "AI声音分类异常"
        emptyMap()
    }
}

private fun callAiClassifyVoiceGroupsApiBatched(
    context: Context,
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
    batchSize: Int = 30,
): Map<String, String> {
    if (voices.isEmpty()) return emptyMap()

    val result = mutableMapOf<String, String>()
    val chunks = voices.chunked(batchSize)

    chunks.forEachIndexed { index, chunk ->
        val one = callAiClassifyVoiceGroupsApi(
            context = context,
            pluginName = "$pluginName 批次${index + 1}/${chunks.size}",
            voices = chunk
        )

        result.putAll(one)
    }

    return result
}

private fun buildAiVoiceGroupMapWithAi(
    context: Context,
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): Map<String, String> {
    val cacheKey = pluginName + "::" + voices.joinToString("|") { it.voiceId + "#" + it.voiceName }

    if (aiGenerateVoiceGroupCacheKey == cacheKey && aiGenerateVoiceGroupCache.isNotEmpty()) {
        return aiGenerateVoiceGroupCache
    }

    val local = mutableMapOf<String, String>()
    val unclassified = mutableListOf<AiPluginVoicePreview>()

    voices.forEach { voice ->
        val group = classifyAiVoiceGroup("${voice.voiceName} ${voice.voiceId}")
        if (group != null) {
            local[voice.voiceId] = group
        } else {
            unclassified += voice
        }
    }

    if (unclassified.isEmpty()) {
        aiGenerateVoiceGroupCacheKey = cacheKey
        aiGenerateVoiceGroupCache = local
        return local
    }

    val aiGroups = callAiClassifyVoiceGroupsApiBatched(
        context = context,
        pluginName = pluginName,
        voices = unclassified,
        batchSize = 15
    )

    val merged = local + aiGroups

    aiGenerateVoiceGroupCacheKey = cacheKey
    aiGenerateVoiceGroupCache = merged

    return merged
}

private fun buildAiGenerateConfigMatchesWithVoiceGroupMap(
    rule: SpeechRule?,
    voices: List<AiPluginVoicePreview>,
    voiceGroupMap: Map<String, String>,
): List<AiGenerateConfigMatch> {
    if (rule == null || voices.isEmpty()) return emptyList()

    val groupOrder = listOf(
        "女童",
        "男童",
        "少女",
        "少年",
        "女青年",
        "男青年",
        "女中年",
        "男中年",
        "女老年",
        "男老年"
    )

    val roleGrouped = parseAiRuleRoleTags(rule).groupBy { it.groupName }

    val voiceGrouped = voices
        .mapNotNull { voice ->
            val group = voiceGroupMap[voice.voiceId] ?: return@mapNotNull null
            group to voice
        }
        .groupBy({ it.first }, { it.second })

    val result = mutableListOf<AiGenerateConfigMatch>()

    groupOrder.forEach { group ->
        val roles = roleGrouped[group]
            .orEmpty()
            .sortedWith(
                compareBy<AiRuleRoleTag> { it.index }
                    .thenBy { it.displayName }
            )

        val candidates = voiceGrouped[group].orEmpty()
        if (roles.isEmpty() || candidates.isEmpty()) return@forEach

        val count = minOf(roles.size, candidates.size)

        for (i in 0 until count) {
            result += AiGenerateConfigMatch(
                role = roles[i],
                voice = candidates[i]
            )
        }
    }

    return result
}

private fun buildAiGenerateConfigMatchesWithAi(
    context: Context,
    rule: SpeechRule?,
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): List<AiGenerateConfigMatch> {
    if (rule == null || voices.isEmpty()) {
        return emptyList()
    }

    val voiceGroupMap = buildAiVoiceGroupMapWithAi(
        context = context,
        pluginName = pluginName,
        voices = voices
    )

    val semanticResult = buildAiGenerateConfigMatchesWithVoiceGroupMap(
        rule = rule,
        voices = voices,
        voiceGroupMap = voiceGroupMap
    )

    return if (semanticResult.isNotEmpty()) {
        aiGenerateMatchLastError = ""
        semanticResult
    } else {
        buildAiGenerateConfigMatches(rule, voices)
    }
}

private fun generateAiConfigListFromPreview(
    context: Context,
    rule: SpeechRule?,
    plugin: Plugin?,
    voices: List<AiPluginVoicePreview>,
): Int {
    if (rule == null || plugin == null || voices.isEmpty()) return 0

    val matches = buildAiGenerateConfigMatchesWithAi(
        context = context,
        rule = rule,
        pluginName = plugin.name,
        voices = voices
    ).filter { it.voice != null }

    if (matches.isEmpty()) return 0

    val pluginShort = aiPluginShortName(plugin.name)
    val rootGroupName = "${pluginShort}声音组"

    deleteAiGenerateGroupTreeByName(rootGroupName)

    val creatableMatches = matches
    if (creatableMatches.isEmpty()) return 0

    val rootGroup = findOrCreateAiGenerateGroup(
        name = rootGroupName,
        parentGroupId = 0L,
        order = dbm.systemTtsV2.allGroup.size + 1
    )

    val groupOrder = listOf(
        "女童",
        "男童",
        "少女",
        "少年",
        "女青年",
        "男青年",
        "女中年",
        "男中年",
        "女老年",
        "男老年"
    )

    val childGroups = groupOrder.mapIndexed { index, groupName ->
        groupName to findOrCreateAiGenerateGroup(
            name = "$pluginShort$groupName",
            parentGroupId = rootGroup.id,
            order = index
        )
    }.toMap()

    val fallbackAiSampleRate = resolvePluginSampleRate(plugin)
    val fallbackAiNeedDecode = resolvePluginNeedDecode(plugin)

    val aiPluginEngine = runCatching {
        TtsPluginUiEngineV2(context, plugin).apply {
            eval()
        }
    }.getOrNull()

    var created = 0
    val now = System.currentTimeMillis()

    val groupCurrentOrder = mutableMapOf<Long, Int>()

    creatableMatches.forEachIndexed { idx, match ->
        val role = match.role
        val voice = match.voice ?: return@forEachIndexed
        val group = childGroups[role.groupName] ?: return@forEachIndexed

        val order = groupCurrentOrder[group.id] ?: dbm.systemTtsV2.getTtsListByGroupId(group.id).size
        groupCurrentOrder[group.id] = order + 1

        val display = voice.voiceName

        val aiLocale = if (plugin.pluginId.startsWith("maoxiang.tts.gj.normalemotion.emobridge")) {
            "emotion"
        } else {
            "zh-CN"
        }
        val aiVoiceId = voice.voiceId
        val aiVoiceIcon = voice.icon

        val aiAvatarUrl = resolveVoiceAvatarUri(
            packageName = context.packageName,
            voiceName = display,
            voiceId = aiVoiceId,
            pluginIcon = aiVoiceIcon
        )

        val aiVoiceData = mapOf(
            "voiceName" to display,
            "avatarUrl" to aiAvatarUrl,
            "icon" to aiAvatarUrl
        )

        val realAiSampleRate = runCatching {
            aiPluginEngine?.getSampleRate(aiLocale, aiVoiceId)
        }.getOrNull() ?: fallbackAiSampleRate

        val realAiNeedDecode = runCatching {
            aiPluginEngine?.isNeedDecode(aiLocale, aiVoiceId)
        }.getOrNull() ?: fallbackAiNeedDecode

        val item = SystemTtsV2(
            id = now + idx,
            displayName = display,
            groupId = group.id,
            isEnabled = true,
            order = order,
            config = TtsConfigurationDTO(
                speechRule = SpeechRuleInfo(
                    tag = role.tag,
                    tagRuleId = rule.ruleId,
                    tagName = role.tagName
                ),
                audioFormat = BasicAudioFormat(
                    sampleRate = realAiSampleRate,
                    isNeedDecode = realAiNeedDecode
                ),
                source = PluginTtsSource(
                    locale = aiLocale,
                    voice = aiVoiceId,
                    pluginId = plugin.pluginId
                )
            )
        )

        dbm.systemTtsV2.insert(item)
        created++
    }

    return created
}

private var aiGenerateMatchLastError: String = ""
private var aiGenerateVoiceGroupCacheKey: String = ""
private var aiGenerateVoiceGroupCache: Map<String, String> = emptyMap()

private data class AiGenerateApiConfig(
    val endpoint: String,
    val model: String,
    val key: String,
)

private fun parseAiGenerateApiConfig(context: Context): AiGenerateApiConfig? {
    val text = readAiGenerateConfigApiText(context).trim()
    if (text.isBlank()) return null

    val parts = text.split("@@")
    if (parts.size >= 3) {
        var endpoint = parts[0].trim()
        if (!endpoint.endsWith("/chat/completions")) {
            endpoint = endpoint.trimEnd('/') + "/chat/completions"
        }

        return AiGenerateApiConfig(
            endpoint = endpoint,
            model = parts[1].trim(),
            key = parts[2].trim()
        )
    }

    return null
}

private fun buildAiGenerateMatchPrompt(
    rule: SpeechRule?,
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): String {
    if (rule == null) return ""

    val roleTags = parseAiRuleRoleTags(rule)
    if (roleTags.isEmpty() || voices.isEmpty()) return ""

    val roleText = roleTags
        .joinToString("\n") { role ->
            "${role.displayName} | group=${role.groupName} | tag=${role.tag}"
        }

    val voiceText = voices
        .take(500)
        .joinToString("\n") { voice ->
            "${voice.voiceId} | ${voice.voiceName}"
        }

    return """
你是中文有声书 TTS 声音匹配助手。

你的任务分两步：
第一步：根据 voiceName 的语义判断每个声音属于什么角色类型。
第二步：把朗读规则角色标签匹配到最合适的 voiceId。

允许的角色类型只有：
女童、男童、少女、少年、女青年、男青年、女中年、男中年、女老年、男老年。

角色类型语义参考：
- 女童：女童、幼女、小女孩、儿童女声、稚嫩女声、萝莉、童声女。
- 男童：男童、男孩、正太、儿童男声、稚嫩男声、童声男。
- 少女：少女、女少年、青春少女、灵动少女、元气少女、甜美少女、清脆少女。
- 少年：少年、男少年、青涩少年、清亮少年、热血少年、阳光少年、清爽少年。
- 女青年：女青年、青年女声、御姐、少御、女主、女配、年轻女性、清冷女声、甜美女声、成熟女声但未到中年。
- 男青年：男青年、青年男声、男主、公子、书生、年轻男性、磁性男声、清朗男声、阳光男声、青叔偏年轻。
- 女中年：女中年、熟女、母亲、夫人、中年女性、端庄女声、成熟女性、慈爱女声。
- 男中年：男中年、大叔、青叔、成熟男性、父亲、将军、老板、稳重男声、厚重男声。
- 女老年：女老年、老妇、奶奶、老妪、慈祥老人女声、苍老女声。
- 男老年：男老年、老者、爷爷、老头、长者、苍老男声、老年男性。

规则：
1. 不要为旁白、narration、duihua、localSound、音效、括号发音人匹配声音。
2. 每个角色标签必须选择一个 voiceId。
3. 优先匹配性别和年龄段。
4. 同一分组内尽量按顺序分配不同声音。
5. 如果同组声音不够，可以复用最相近的同组声音。
6. 如果声音名没有明确性别年龄，也要根据语义尽量判断，不要留空。
7. 必须只输出 JSON，不要解释，不要 Markdown。
8. JSON key 必须使用角色 displayName 原文。
9. voiceId 必须来自"可用声音"列表，不能编造。

插件名称：
$pluginName

角色标签：
$roleText

可用声音：
$voiceText

输出格式：
{
  "女/女童01": {"voiceId": "xxx", "group": "女童", "reason": "小女孩/童声语义匹配"},
  "男/男青年01": {"voiceId": "yyy", "group": "男青年", "reason": "男青年/男主语义匹配"}
}
""".trimIndent()
}

private fun callAiGenerateMatchApi(
    context: Context,
    rule: SpeechRule?,
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): Map<String, String> {
    val cfg = parseAiGenerateApiConfig(context)
        ?: run {
            aiGenerateMatchLastError = "未设置 AI生成配置模型。请点击模型设置，填写：接口地址@@模型名@@API_KEY"
            return emptyMap()
        }
    val prompt = buildAiGenerateMatchPrompt(rule, pluginName, voices)
    if (prompt.isBlank()) {
        aiGenerateMatchLastError = "构建 AI 匹配提示词失败：规则为空或声音列表为空"
        return emptyMap()
    }

    return runCatching {
        val body = JSONObject()
            .put("model", cfg.model)
            .put(
                "messages",
                JSONArray()
                    .put(
                        JSONObject()
                            .put("role", "system")
                            .put("content", "你只输出合法 JSON。")
                    )
                    .put(
                        JSONObject()
                            .put("role", "user")
                            .put("content", prompt)
                    )
            )
            .put("temperature", 0.1)

        val conn = URL(cfg.endpoint).openConnection() as java.net.HttpURLConnection
        conn.requestMethod = "POST"
        conn.connectTimeout = 15000
        conn.readTimeout = 30000
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Authorization", "Bearer ${cfg.key}")

        OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use {
            it.write(body.toString())
        }

        val responseText = if (conn.responseCode in 200..299) {
            conn.inputStream.bufferedReader().use { it.readText() }
        } else {
            conn.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        }

        if (conn.responseCode !in 200..299) {
            aiGenerateMatchLastError = "HTTP ${conn.responseCode}: " + responseText.take(300)
            return@runCatching emptyMap<String, String>()
        }

        val root = JSONObject(responseText)
        val content = root
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content")

        val jsonStart = content.indexOf("{")
        val jsonEnd = content.lastIndexOf("}")
        if (jsonStart < 0 || jsonEnd <= jsonStart) {
            aiGenerateMatchLastError = "AI 返回内容不是 JSON：" + content.take(300)
            return@runCatching emptyMap<String, String>()
        }

        val obj = JSONObject(content.substring(jsonStart, jsonEnd + 1))
        val result = mutableMapOf<String, String>()

        obj.keys().forEach { key ->
            val item = obj.optJSONObject(key)
            val voiceId = item?.optString("voiceId").orEmpty()
            if (voiceId.isNotBlank()) {
                result[key] = voiceId
            }
        }

        aiGenerateMatchLastError = ""
        result
    }.getOrElse {
        aiGenerateMatchLastError = it.message ?: "AI 匹配异常"
        emptyMap()
    }
}

private fun buildAiGenerateConfigMatchPreviewTextWithAi(
    context: Context,
    rule: SpeechRule?,
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): String {
    if (rule == null) {
        return "未找到已启用的完整朗读规则。"
    }

    if (pluginName.isBlank()) {
        return "请先选择 TTS 插件。"
    }

    if (voices.isEmpty()) {
        return "当前插件还没有可用声音列表。请先点击📡 动态获取声音列表，或换一个可解析声音列表的插件。"
    }

    val localVoiceGroupMap = voices.mapNotNull { voice ->
        val group = classifyAiVoiceGroup("${voice.voiceName} ${voice.voiceId}") ?: return@mapNotNull null
        voice.voiceId to group
    }.toMap()

    val localCount = localVoiceGroupMap.size

    val voiceGroupMap = buildAiVoiceGroupMapWithAi(
        context = context,
        pluginName = pluginName,
        voices = voices
    )

    val aiAddedCount = (voiceGroupMap.size - localCount).coerceAtLeast(0)

    val matches = buildAiGenerateConfigMatchesWithVoiceGroupMap(
        rule = rule,
        voices = voices,
        voiceGroupMap = voiceGroupMap
    )

    if (matches.isEmpty()) {
        return buildAiGenerateConfigMatchPreviewText(
            rule = rule,
            pluginName = pluginName,
            voices = voices
        ) + "\n\n⚠️ AI 分类未生成有效匹配，已回退本地规则匹配。\n原因：$aiGenerateMatchLastError"
    }

    val pluginShortName = aiPluginShortName(pluginName)

    val groupOrder = listOf(
        "女童",
        "男童",
        "少女",
        "少年",
        "女青年",
        "男青年",
        "女中年",
        "男中年",
        "女老年",
        "男老年"
    )

    val roleTags = parseAiRuleRoleTags(rule)

    val sb = StringBuilder()

    sb.append(pluginShortName).append("声音组").append("\n")
    sb.append("规则角色池：").append(roleTags.size).append(" 条").append("\n")
    sb.append("声音总数：").append(voices.size).append(" 个").append("\n")
    sb.append("本地可识别：").append(localCount).append(" 个").append("\n")
    if (aiAddedCount > 0) {
        sb.append("AI 补充归类：").append(aiAddedCount).append(" 个").append("\n")
    }
    sb.append("本次将生成：").append(matches.size).append(" 条标准 TTS 配置").append("\n")
    sb.append("说明：每个分组从 01 开始，只按插件候选声音数量生成。").append("\n\n")

    val grouped = matches.groupBy { it.role.groupName }

    groupOrder.forEach { group ->
        val items = grouped[group].orEmpty()
        if (items.isEmpty()) return@forEach

        sb.append("  ").append(pluginShortName).append(group).append("\n")

        items.take(12).forEach { item ->
            sb.append("    ")
                .append(item.role.displayName)
                .append(" → ")
                .append(item.voice?.voiceName ?: "未匹配到候选声音")
                .append("\n")
        }

        if (items.size > 12) {
            sb.append("    ... 还有 ").append(items.size - 12).append(" 条").append("\n")
        }

        sb.append("\n")
    }

    sb.append("说明：这是预览，还没有写入配置列表。确认后会创建插件声音组和各角色分组。")
    return sb.toString()
}

private fun buildAiGenerateConfigMatchPreviewText(
    rule: SpeechRule?,
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): String {
    if (rule == null) {
        return "未找到已启用的完整朗读规则。"
    }

    val roleTags = parseAiRuleRoleTags(rule)
    if (roleTags.isEmpty()) {
        return "当前朗读规则没有可生成的角色标签。"
    }

    if (pluginName.isBlank()) {
        return "请先选择 TTS 插件。"
    }

    if (voices.isEmpty()) {
        return "当前插件还没有可用声音列表。请先点击📡 动态获取声音列表，或换一个可解析声音列表的插件。"
    }

    val matches = buildAiGenerateConfigMatches(rule, voices)

    if (matches.isEmpty()) {
        return "没有匹配到可生成的角色配置。请检查插件声音名称是否包含女童、男童、女青年等类型。"
    }

    val pluginShortName = aiPluginShortName(pluginName)

    val groupOrder = listOf(
        "女童",
        "男童",
        "少女",
        "少年",
        "女青年",
        "男青年",
        "女中年",
        "男中年",
        "女老年",
        "男老年"
    )

    val grouped = matches.groupBy { it.role.groupName }

    val sb = StringBuilder()

    sb.append(pluginShortName).append("声音组").append("\n")
    sb.append("规则角色池：").append(roleTags.size).append(" 条").append("\n")
    sb.append("本次将生成：").append(matches.size).append(" 条标准 TTS 配置").append("\n")
    sb.append("说明：每个分组从 01 开始，只按插件候选声音数量生成。").append("\n\n")

    groupOrder.forEach { group ->
        val items = grouped[group].orEmpty()
        if (items.isEmpty()) return@forEach

        sb.append("  ").append(pluginShortName).append(group).append("\n")

        items.take(12).forEach { item ->
            sb.append("    ")
                .append(item.role.displayName)
                .append(" → ")
                .append(item.voice?.voiceName ?: "未匹配到候选声音")
                .append("\n")
        }

        if (items.size > 12) {
            sb.append("    ... 还有 ").append(items.size - 12).append(" 条").append("\n")
        }

        sb.append("\n")
    }

    sb.append("说明：这是预览，还没有写入配置列表。确认后会创建插件声音组和各角色分组。")
    return sb.toString()
}

private fun buildAiPluginVoiceGroupPreviewText(
    pluginName: String,
    voices: List<AiPluginVoicePreview>,
): String {
    if (pluginName.isBlank()) {
        return "未选择 TTS 插件。"
    }

    if (voices.isEmpty()) {
        return "已选择插件：$pluginName\n未能从插件代码中预览到声音列表。动态插件后续会通过插件接口获取声音。"
    }

    val groupOrder = listOf(
        "女童",
        "男童",
        "少女",
        "少年",
        "女青年",
        "男青年",
        "女中年",
        "男中年",
        "女老年",
        "男老年"
    )

    val grouped = voices
        .mapNotNull { voice ->
            val group = classifyAiVoiceGroup("${voice.voiceName} ${voice.voiceId}") ?: return@mapNotNull null
            group to voice
        }
        .groupBy({ it.first }, { it.second })

    val unmatchedCount = voices.count { voice ->
        classifyAiVoiceGroup("${voice.voiceName} ${voice.voiceId}") == null
    }

    val sb = StringBuilder()
    sb.append("已选择插件：").append(pluginName).append("\n")
    sb.append("声音总数：").append(voices.size).append(" 个\n")
    sb.append("可识别角色候选：").append(grouped.values.sumOf { it.size }).append(" 个\n")
    if (unmatchedCount > 0) {
        sb.append("未归类声音：").append(unmatchedCount).append(" 个\n")
    }
    sb.append("\n")

    groupOrder.forEach { group ->
        val items = grouped[group].orEmpty()
        if (items.isEmpty()) return@forEach

        sb.append(group).append("：").append(items.size).append(" 个候选\n")
        items.take(5).forEach { voice ->
            sb.append("  ").append(voice.voiceName).append("\n")
        }
        if (items.size > 5) {
            sb.append("  ... 还有 ").append(items.size - 5).append(" 个\n")
        }
        sb.append("\n")
    }

    if (grouped.isEmpty()) {
        sb.append("没有识别到女童、男童、女青年等角色候选声音。")
    }

    return sb.toString()
}

private fun buildAiRuleRolePreviewText(rule: SpeechRule?): String {
    if (rule == null) {
        return "未找到已启用的完整朗读规则。"
    }

    val tags = parseAiRuleRoleTags(rule)
    if (tags.isEmpty()) {
        return "当前朗读规则没有解析到可生成的角色标签。\n\n旁白、音效、括号标签会被跳过。"
    }

    val groupOrder = listOf(
        "女童",
        "男童",
        "少女",
        "少年",
        "女青年",
        "男青年",
        "女中年",
        "男中年",
        "女老年",
        "男老年"
    )

    val grouped = tags.groupBy { it.groupName }

    val sb = StringBuilder()
    sb.append("当前规则：").append(rule.name).append("\n")
    sb.append("可生成角色配置：").append(tags.size).append(" 条").append("\n\n")
    sb.append("预览只显示每组前 5 条。").append("\n\n")

    groupOrder.forEach { group ->
        val items = grouped[group].orEmpty()
        if (items.isEmpty()) return@forEach

        sb.append(group).append("：共 ").append(items.size).append(" 条").append("\n")

        items.take(5).forEach { item ->
            sb.append("  ").append(item.displayName).append("\n")
        }

        if (items.size > 5) {
            sb.append("  ... 还有 ").append(items.size - 5).append(" 条").append("\n")
        }

        sb.append("\n")
    }

    sb.append("旁白、音效、括号标签不会生成。")
    return sb.toString()
}
