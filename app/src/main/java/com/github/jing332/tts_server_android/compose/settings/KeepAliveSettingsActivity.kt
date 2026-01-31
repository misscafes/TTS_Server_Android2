package com.github.jing332.tts_server_android.compose.settings

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.MobileFriendly
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.jing332.common.utils.longToast
import com.github.jing332.common.utils.toast
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.compose.ComposeActivity
import com.github.jing332.tts_server_android.compose.theme.AppTheme
import com.github.jing332.tts_server_android.conf.SystemTtsConfig
import com.github.jing332.tts_server_android.service.forwarder.system.SysTtsForwarderService
import com.github.jing332.tts_server_android.service.keepalive.KeepAliveJobService
import com.github.jing332.tts_server_android.service.keepalive.KeepAliveService
import com.github.jing332.tts_server_android.utils.BackgroundWhitelistUtils
import com.github.jing332.tts_server_android.utils.MyTools.isIgnoringBatteryOptimizations
import com.github.jing332.tts_server_android.utils.MyTools.killBattery

/**
 * 后台保活设置 Activity
 * 与备份恢复页面保持统一的 Activity 风格
 */
class KeepAliveSettingsActivity : ComposeActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                KeepAliveSettingsContent()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun KeepAliveSettingsContent() {
        val context = LocalContext.current
        val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
        val manufacturer = remember { Build.MANUFACTURER }

        // 厂商白名单引导对话框状态
        var showManufacturerWhitelistDialog by remember { mutableStateOf(false) }

        // 帮助详情页面状态
        var showHelpDetail by remember { mutableStateOf(false) }

        // 保活配置状态
        var isKeepAliveEnabled by remember { SystemTtsConfig.isKeepAliveEnabled }
        var isAudioFocusEnabled by remember { SystemTtsConfig.isKeepAliveAudioFocusEnabled }
        var isSilentAudioEnabled by remember { SystemTtsConfig.isKeepAliveSilentAudioEnabled }
        var isAutoStartEnabled by remember { SystemTtsConfig.isAutoStartEnabled }

        // 显示帮助详情页面
        if (showHelpDetail) {
            KeepAliveHelpContent(
                onNavigateBack = { showHelpDetail = false }
            )
            return
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0),
            modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.keep_alive_settings)) },
                    scrollBehavior = scrollBehaviour,
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.navigate_back))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // 电池优化白名单
                val isInBatteryWhitelist = remember { context.isIgnoringBatteryOptimizations() }
                BasePreferenceWidget(
                    onClick = { context.killBattery() },
                    title = { Text(stringResource(id = R.string.battery_optimization_whitelist)) },
                    subTitle = {
                        Text(
                            if (isInBatteryWhitelist)
                                stringResource(R.string.added_battery_optimization_whitelist)
                            else
                                stringResource(R.string.battery_optimization_whitelist_desc)
                        )
                    },
                    icon = { Icon(Icons.Default.BatteryChargingFull, null) }
                )

                // 厂商后台设置
                BasePreferenceWidget(
                    onClick = {
                        showManufacturerWhitelistDialog = true
                    },
                    title = { Text(stringResource(R.string.manufacturer_whitelist)) },
                    subTitle = {
                        Text(
                            stringResource(R.string.manufacturer_whitelist_summary, manufacturer)
                        )
                    },
                    icon = { Icon(Icons.Default.MobileFriendly, null) }
                )

                DividerPreference { Text(stringResource(R.string.keep_alive_settings)) }

                // 启用后台保活
                val isForwarderRunning = SysTtsForwarderService.isRunning
                SwitchPreference(
                    title = { Text(stringResource(R.string.enable_keep_alive)) },
                    subTitle = {
                        Text(
                            if (isForwarderRunning)
                                stringResource(R.string.keep_alive_merged_with_forwarder)
                            else
                                stringResource(R.string.enable_keep_alive_summary)
                        )
                    },
                    checked = isKeepAliveEnabled,
                    onCheckedChange = { enabled ->
                        isKeepAliveEnabled = enabled
                        if (enabled) {
                            if (!isForwarderRunning) {
                                KeepAliveService.start(context)
                            }
                            KeepAliveJobService.schedule(context)
                            context.longToast(R.string.keep_alive_service_title)
                        } else {
                            KeepAliveService.stop(context)
                            KeepAliveJobService.cancel(context)
                        }
                    },
                    icon = { Icon(Icons.Default.PowerSettingsNew, null) }
                )

                // 音频焦点保活
                SwitchPreference(
                    title = { Text(stringResource(R.string.keep_alive_audio_focus)) },
                    subTitle = { Text(stringResource(R.string.keep_alive_audio_focus_summary)) },
                    checked = isAudioFocusEnabled,
                    onCheckedChange = { isAudioFocusEnabled = it },
                    icon = { Icon(Icons.Default.SurroundSound, null) }
                )

                // 静音音频保活
                SwitchPreference(
                    title = { Text(stringResource(R.string.keep_alive_silent_audio)) },
                    subTitle = { Text(stringResource(R.string.keep_alive_silent_audio_summary)) },
                    checked = isSilentAudioEnabled,
                    onCheckedChange = { isSilentAudioEnabled = it },
                    icon = { Icon(Icons.Default.SurroundSound, null) }
                )

                // 开机自启动
                SwitchPreference(
                    title = { Text(stringResource(R.string.enable_auto_start)) },
                    subTitle = { Text(stringResource(R.string.enable_auto_start_summary)) },
                    checked = isAutoStartEnabled,
                    onCheckedChange = { isAutoStartEnabled = it },
                    icon = { Icon(Icons.Default.Refresh, null) }
                )

                DividerPreference { Text(stringResource(R.string.help)) }

                // 帮助入口 - 点击进入三级页面
                BasePreferenceWidget(
                    onClick = { showHelpDetail = true },
                    title = { Text(stringResource(R.string.keep_alive_help_title)) },
                    subTitle = { Text(stringResource(R.string.keep_alive_help_summary)) },
                    icon = { Icon(Icons.AutoMirrored.Filled.Help, null) }
                )

                Spacer(Modifier.navigationBarsPadding())
            }
        }

        // 厂商白名单引导对话框
        if (showManufacturerWhitelistDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showManufacturerWhitelistDialog = false },
                title = { Text(stringResource(R.string.manufacturer_whitelist_title)) },
                text = { Text(stringResource(R.string.manufacturer_whitelist_message)) },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            showManufacturerWhitelistDialog = false
                            val success = BackgroundWhitelistUtils.openManufacturerBatterySettings(context)
                            if (!success) {
                                context.toast(R.string.system_not_support_please_manual_set)
                            }
                        }
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { showManufacturerWhitelistDialog = false }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun KeepAliveHelpContent(
        onNavigateBack: () -> Unit = {}
    ) {
        val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            contentWindowInsets = WindowInsets(0),
            modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.keep_alive_help_title)) },
                    scrollBehavior = scrollBehaviour,
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.navigate_back))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                val helpText = stringResource(R.string.keep_alive_help_message)
                val textColor = MaterialTheme.colorScheme.onSurfaceVariant.hashCode()
                AndroidView(
                    factory = { context ->
                        TextView(context).apply {
                            text = Html.fromHtml(helpText, Html.FROM_HTML_MODE_COMPACT)
                            movementMethod = LinkMovementMethod.getInstance()
                            setTextAppearance(android.R.style.TextAppearance_Medium)
                            setTextColor(textColor)
                            setPadding(32.dp.value.toInt(), 24.dp.value.toInt(), 32.dp.value.toInt(), 32.dp.value.toInt())
                            setLineSpacing(8.dp.value, 1.2f)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}
