package com.github.jing332.tts_server_android.compose.systts.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.github.jing332.database.entities.systts.SystemTtsGroup
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.database.entities.systts.source.LocalTtsSource
import com.github.jing332.database.entities.systts.source.PluginTtsSource
import com.github.jing332.tts_server_android.AppLocale
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.compose.AppDefaultProperties
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
import com.github.jing332.tts_server_android.ui.view.AppDialogs.displayErrorDialog
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable


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

    // 子分组折叠状态：存储折叠的子分组完整路径
    var collapsedSubGroups by rememberSaveable { mutableStateOf<Set<String>>(emptySet()) }

    BackHandler(enabled = isSearchMode) {
        isSearchMode = false
        vm.setSearchKeyword("")
    }

    var showSortDialog by remember { mutableStateOf<List<SystemTtsV2>?>(null) }
    if (showSortDialog != null) SortDialog(
        onDismissRequest = { showSortDialog = null },
        list = showSortDialog!!
    )

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
    if (showBatchTagDialog != null) {
        BatchTagDialog(
            groupItems = showBatchTagDialog!!,
            onDismissRequest = { showBatchTagDialog = null }
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
        val otherGroups = remember(models, targetGroup.id) {
            models.filter { it.group.id != targetGroup.id }.map { it.group }
        }

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
                    Column {
                        Text("选择目标分组，当前分组将作为其子分组：", modifier = Modifier.padding(bottom = 8.dp))
                        otherGroups.forEach { otherGroup ->
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        currentGroupWithTts?.list?.forEach { item ->
                                            dbm.systemTtsV2.update(
                                                item.copy(
                                                    groupId = otherGroup.id,
                                                    categoryPath = targetGroup.name
                                                )
                                            )
                                        }
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
                                        val groupName = path.substringAfterLast('/')
                                        val newGroup = SystemTtsGroup(
                                            id = System.currentTimeMillis(),
                                            name = groupName
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
                runCatching {
                    ruleData.tagName =
                        SpeechRuleEngine.getTagName(context, speechRule, info = ruleData)
                }.onFailure {
                    ruleData.tagName = ""
                    context.displayErrorDialog(it)
                }

            }
        }
        else {
            dbm.speechRuleDao.getByRuleId(ruleData.tagRuleId)?.let {
                ruleData.target = SpeechTarget.TAG
                ruleData.tag = it.tags.keys.first()
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
            onDismissRequest = { addGroupDialog = false }) {
            addGroupDialog = false
            dbm.systemTtsV2.insertGroup(SystemTtsGroup(name = name))
        }
    }

    var showGroupExportSheet by remember { mutableStateOf<List<GroupWithSystemTts>?>(null) }
    if (showGroupExportSheet != null) {
        val list = showGroupExportSheet!!
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
                        Text(stringResource(id = R.string.system_tts))
                    }
                }, actions = {
                    if (isSearchMode) {
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
                        IconButton(onClick = { showOptions = true }) {
                            Icon(Icons.Default.MoreVert, stringResource(id = R.string.more_options))
                            MenuMoreOptions(
                                expanded = showOptions,
                                onDismissRequest = { showOptions = false },
                                onExportAll = { showGroupExportSheet = models },
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
                models.forEachIndexed { _, groupWithSystemTts ->
                    val g = groupWithSystemTts.group
                    val key = "g_${g.id}"
                    
                    val groupDragModifier = if (searchKeyword.isNotEmpty()) Modifier 
                                            else Modifier.detectReorderAfterLongPress(reorderState)

                    stickyHeader(key = key) {
                        val checkState =
                            groupWithSystemTts.list.filter { it.isEnabled }.size.sizeToToggleableState(
                                groupWithSystemTts.list.size
                            )
                        
                        ShadowedDraggableItem(reorderableState = reorderState, key = key) {
                            Group(modifier = groupDragModifier,
                                name = g.name,
                                group = g,
                                isExpanded = g.isExpanded,
                                toggleableState = checkState,
                                onToggleableStateChange = {
                                    vm.updateGroupEnable(groupWithSystemTts, it)
                                },
                                onClick = {
                                    dbm.systemTtsV2.updateGroup(g.copy(isExpanded = !g.isExpanded))
                                },
                                onDelete = {
                                    dbm.systemTtsV2.delete(*groupWithSystemTts.list.toTypedArray())
                                    dbm.systemTtsV2.deleteGroup(g)
                                },
                                onRename = {
                                    dbm.systemTtsV2.updateGroup(g.copy(name = it))
                                },
                                onCopy = {
                                    scope.launch {
                                        val group = g.copy(id = System.currentTimeMillis(),
                                            name = it.ifBlank { context.getString(R.string.unnamed) })
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
                                onExport = {
                                    showGroupExportSheet = listOf(groupWithSystemTts)
                                },
                                onSort = {
                                    showSortDialog = groupWithSystemTts.list
                                },
                                onCreateSubGroup = {
                                    showCreateSubGroup = g.id
                                },
                                onBatchAssignTags = {
                                    showBatchTagDialog = groupWithSystemTts.list
                                },
                                onReleaseSubGroup = {
                                    showReleaseSubGroup = g
                                },
                                onConvertToSubGroup = {
                                    showConvertToSubGroup = g
                                },
                                onExtractSubGroup = {
                                    showExtractSubGroup = g
                                }
                            )
                        }
                    }

                    if (g.isExpanded) {
                        val hasSubGroups = groupWithSystemTts.list.any { it.categoryPath.isNotBlank() }

                        if (!hasSubGroups) {
                            // 无子分组时保持原有扁平渲染（支持拖拽排序）
                            itemsIndexed(groupWithSystemTts.list.sortedBy { it.order },
                                key = { _, v -> "${g.id}_${v.id}" }) { _, item ->
                                if (g.id == 1L) println(item.displayName + ", " + item.order)

                                ShadowedDraggableItem(
                                    reorderableState = reorderState,
                                    key = "${g.id}_${item.id}"
                                ) {
                                    val descriptor = remember(item) {
                                        ItemDescriptorFactory.from(context, item)
                                    }
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
                                        onLongClick = { switchSpeechTarget(item) },
                                        onCopy = {
                                            navigateToEdit(item.copy(id = System.currentTimeMillis()))
                                        },
                                        onDelete = { deleteTts = item },
                                        onEdit = { navigateToEdit(item) },
                                        onAudition = {
                                            if (item.config is TtsConfigurationDTO) {
                                                // 强制创建新的对象副本，确保 Compose 检测到变化并重新触发试听
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
                                        }
                                    )
                                }
                            }
                        } else {
                            // 有子分组时使用树形渲染
                            val tree = buildSubCategoryTree(groupWithSystemTts.list)
                            val flattened = flattenSubCategoryTree(tree)

                            // 过滤掉折叠的子分组内容
                            val visibleItems = mutableListOf<FlattenedCategoryItem>()
                            var skipLevel = Int.MAX_VALUE
                            for (fItem in flattened) {
                                when (fItem) {
                                    is FlattenedCategoryItem.SubGroupHeader -> {
                                        // 遇到同级或上级的 header 时重置跳过状态
                                        if (fItem.node.level <= skipLevel) {
                                            skipLevel = Int.MAX_VALUE
                                        }
                                        // 被跳过的子分组 header 不显示
                                        if (fItem.node.level > skipLevel) {
                                            continue
                                        }
                                        visibleItems.add(fItem)
                                        if (collapsedSubGroups.contains(fItem.node.fullPath)) {
                                            skipLevel = fItem.node.level
                                        }
                                    }
                                    is FlattenedCategoryItem.TtsItem -> {
                                        // 修复：displayLevel 必须 <= skipLevel 才显示，
                                        // 之前 <= skipLevel + 1 导致折叠子分组后其直接内容仍然显示
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
                                        is FlattenedCategoryItem.TtsItem -> "${g.id}_${v.item.id}"
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
                                            SubGroupHeader(
                                                modifier = subDragModifier,
                                                name = fItem.node.name,
                                                level = fItem.node.level,
                                                isExpanded = !collapsedSubGroups.contains(fItem.node.fullPath),
                                                onClick = {
                                                    collapsedSubGroups = if (collapsedSubGroups.contains(fItem.node.fullPath)) {
                                                        collapsedSubGroups - fItem.node.fullPath
                                                    } else {
                                                        collapsedSubGroups + fItem.node.fullPath
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    is FlattenedCategoryItem.TtsItem -> {
                                        val item = fItem.item
                                        val descriptor = remember(item) {
                                            ItemDescriptorFactory.from(context, item)
                                        }
                                        Item(
                                            reorderState = reorderState,
                                            modifier = Modifier.padding(
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
                                            onLongClick = { switchSpeechTarget(item) },
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
                                            isInSubGroup = fItem.displayLevel > 0,
                                            onExport = {
                                                showExportSheet =
                                                    listOf(item.copy(groupId = AbstractListGroup.DEFAULT_GROUP_ID))
                                            },
                                            onMoveToSubGroup = {
                                                showMoveToSubGroup = item
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
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
