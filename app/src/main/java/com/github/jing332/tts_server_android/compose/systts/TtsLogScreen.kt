package com.github.jing332.tts_server_android.compose.systts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jing332.common.LogLevel
import com.github.jing332.tts_server_android.R

@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TtsLogScreen(vm: TtsLogViewModel = viewModel()) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    val showFilter by remember { vm.showFilterDialog }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = {
                    (fadeIn(animationSpec = androidx.compose.animation.core.tween(200)) +
                            slideInHorizontally { it / 3 })
                        .togetherWith(
                            fadeOut(animationSpec = androidx.compose.animation.core.tween(200)) +
                                    slideOutHorizontally { it / 3 }
                        )
                },
                label = "TopAppBarState"
            ) { searchActive ->
                if (searchActive) {
                    // 搜索状态：显示简洁的TopAppBar + DockedSearchBar
                    TopAppBar(
                        title = {
                            // MD3 风格的 DockedSearchBar
                            DockedSearchBar(
                                modifier = Modifier.fillMaxWidth(),
                                inputField = {
                                    SearchBarDefaults.InputField(
                                        query = vm.searchQuery.value,
                                        onQueryChange = { vm.searchQuery.value = it },
                                        onSearch = { /* 搜索已实时进行 */ },
                                        expanded = false,
                                        onExpandedChange = { },
                                        placeholder = { Text(stringResource(R.string.search)) },
                                        leadingIcon = {
                                            Icon(Icons.Default.Search, contentDescription = null)
                                        },
                                        trailingIcon = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                // 清除按钮 - 放在搜索框内部右侧
                                                AnimatedVisibility(
                                                    visible = vm.searchQuery.value.isNotEmpty(),
                                                    enter = fadeIn(),
                                                    exit = fadeOut()
                                                ) {
                                                    IconButton(
                                                        onClick = { vm.searchQuery.value = "" },
                                                        modifier = Modifier.padding(end = 4.dp)
                                                    ) {
                                                        Icon(Icons.Default.Clear, contentDescription = null)
                                                    }
                                                }
                                            }
                                        },
                                    )
                                },
                                expanded = false,
                                onExpandedChange = { },
                                content = { }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                isSearchActive = false
                                vm.searchQuery.value = ""
                            }) {
                                Icon(Icons.AutoMirrored.Default.ArrowBack, stringResource(R.string.nav_back))
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors()
                    )
                } else {
                    // 默认状态：显示标题和图标按钮
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(id = R.string.log),
                                    textAlign = TextAlign.Center
                                )
                                SelectionContainer {
                                    Text(
                                        modifier = Modifier
                                            .verticalScroll(rememberScrollState())
                                            .padding(2.dp),
                                        text = vm.logDir(),
                                        style = MaterialTheme.typography.bodySmall,
                                        overflow = TextOverflow.Visible
                                    )
                                }
                            }
                        },
                        actions = {
                            // 筛选图标
                            IconButton(onClick = { vm.showFilterDialog.value = true }) {
                                Icon(Icons.Default.FilterList, stringResource(R.string.filter))
                            }
                            // 清理日志图标
                            IconButton(onClick = { vm.clear() }) {
                                Icon(Icons.Default.DeleteOutline, stringResource(id = R.string.clear_log))
                            }
                            // 搜索图标（最右侧）
                            IconButton(onClick = { isSearchActive = true }) {
                                Icon(Icons.Default.Search, stringResource(R.string.search))
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors()
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // 日志级别筛选栏
            AnimatedVisibility(
                visible = vm.selectedLevels.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.filter),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        vm.selectedLevels.forEach { level ->
                            FilterChip(
                                selected = true,
                                onClick = { vm.toggleLevel(level) },
                                label = { Text(getLevelName(level)) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = null,
                                        modifier = Modifier.height(16.dp).width(16.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = getLevelColor(level)
                                )
                            )
                        }
                        // 清除所有筛选
                        if (vm.selectedLevels.isNotEmpty()) {
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { vm.clearFilter() }) {
                                Icon(Icons.Default.Clear, stringResource(R.string.clear))
                            }
                        }
                    }
                    HorizontalDivider()
                }
            }

            LogScreen(
                modifier = Modifier.fillMaxSize(),
                list = vm.filteredLogs
            )
        }
    }

    // 筛选对话框
    if (showFilter) {
        LogFilterDialog(
            selectedLevels = vm.selectedLevels,
            onLevelToggle = { vm.toggleLevel(it) },
            onDismiss = { vm.showFilterDialog.value = false }
        )
    }
}

@Composable
private fun getLevelName(level: Int): String {
    return when (level) {
        LogLevel.ERROR -> "ERROR"
        LogLevel.WARN -> "WARN"
        LogLevel.INFO -> "INFO"
        LogLevel.DEBUG -> "DEBUG"
        LogLevel.TRACE -> "VERBOSE"
        else -> "UNKNOWN"
    }
}

@Composable
private fun getLevelColor(level: Int): Color {
    return when (level) {
        LogLevel.ERROR -> MaterialTheme.colorScheme.errorContainer
        LogLevel.WARN -> Color(0xFFFFF3E0)
        LogLevel.INFO -> MaterialTheme.colorScheme.secondaryContainer
        LogLevel.DEBUG -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}
