package com.github.jing332.tts_server_android.compose.systts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.github.jing332.tts_server_android.R

@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TtsLogScreen(vm: TtsLogViewModel = viewModel()) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var isSearchActive by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = {
                    // 使用淡入淡出 + 水平滑动的动画效果
                    (fadeIn(animationSpec = androidx.compose.animation.core.tween(150)) +
                            slideInHorizontally { it / 2 })
                        .togetherWith(
                            fadeOut(animationSpec = androidx.compose.animation.core.tween(150)) +
                                    slideOutHorizontally { it / 2 }
                        )
                },
                label = "TopAppBarState"
            ) { searchActive ->
                if (searchActive) {
                    // 搜索状态：显示搜索输入框
                    TopAppBar(
                        title = {
                            TextField(
                                value = vm.searchQuery.value,
                                onValueChange = { vm.searchQuery.value = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text(stringResource(R.string.search)) },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                trailingIcon = {
                                    AnimatedVisibility(
                                        visible = vm.searchQuery.value.isNotEmpty(),
                                        enter = fadeIn() + expandHorizontally(),
                                        exit = shrinkHorizontally() + fadeOut()
                                    ) {
                                        IconButton(onClick = { vm.searchQuery.value = "" }) {
                                            Icon(Icons.Default.Clear, contentDescription = null)
                                        }
                                    }
                                }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                isSearchActive = false
                                vm.searchQuery.value = ""
                            }) {
                                Icon(Icons.Default.ArrowBack, stringResource(R.string.nav_back))
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
                            // 清理日志图标（在左侧）
                            IconButton(onClick = { vm.clear() }) {
                                Icon(Icons.Default.DeleteOutline, stringResource(id = R.string.clear_log))
                            }
                            // 搜索图标（在右侧，即清理图标的右侧）
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
            LogScreen(
                modifier = Modifier.fillMaxSize(),
                list = vm.filteredLogs
            )
        }
    }
}
