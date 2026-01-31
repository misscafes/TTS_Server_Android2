package com.github.jing332.tts_server_android.compose.forwarder.systts

import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.compose.forwarder.BasicConfigScreen
import com.github.jing332.tts_server_android.compose.forwarder.BasicForwarderScreen
import com.github.jing332.tts_server_android.compose.forwarder.ConfigViewModel
import com.github.jing332.tts_server_android.compose.forwarder.ForwarderTopAppBar
import com.github.jing332.tts_server_android.conf.SystemTtsForwarderConfig
import com.github.jing332.tts_server_android.service.forwarder.ForwarderServiceManager.switchSysTtsForwarder
import com.github.jing332.tts_server_android.service.forwarder.system.SysTtsForwarderService
import com.github.jing332.tts_server_android.ui.forwarder.SystemForwarderSwitchActivity
import com.github.jing332.tts_server_android.utils.MyTools
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemTtsForwarderScreen(cfgVM: ConfigViewModel = viewModel()) {
    val context = LocalContext.current
    var port by remember { SystemTtsForwarderConfig.port }
    BasicForwarderScreen(
        topBar = {
            var wakeLockEnabled by remember { SystemTtsForwarderConfig.isWakeLockEnabled }
            ForwarderTopAppBar(
                title = { Text(text = stringResource(id = R.string.forwarder_systts)) },
                wakeLockEnabled = wakeLockEnabled,
                onWakeLockEnabledChange = { wakeLockEnabled = it },
                onOpenWeb = { "http://localhost:${port}" }
            ) {
                MyTools.addShortcut(
                    ctx = context,
                    name = context.getString(R.string.forwarder_systts),
                    id = "switch_systts_forwarder",
                    iconResId = R.mipmap.ic_app_launcher_round,
                    launcherIntent = Intent(context, SystemForwarderSwitchActivity::class.java)
                )
            }
        },
        configScreen = {
            var isRunning by remember { mutableStateOf(SysTtsForwarderService.isRunning) }

            // 定期同步服务状态，确保UI与实际服务状态一致
            // 修复：从首页重启服务后，转发器界面开关状态不同步的问题
            LaunchedEffect(Unit) {
                while (true) {
                    delay(500) // 每500ms检查一次
                    val actualRunningState = SysTtsForwarderService.isRunning
                    if (isRunning != actualRunningState) {
                        isRunning = actualRunningState
                    }
                }
            }

            BasicConfigScreen(
                modifier = Modifier.fillMaxSize(),
                vm = cfgVM,
                intentFilter = IntentFilter().apply {
                    addAction(SysTtsForwarderService.ACTION_ON_LOG)
                    addAction(SysTtsForwarderService.ACTION_ON_CLOSED)
                    addAction(SysTtsForwarderService.ACTION_ON_STARTED)
                },
                actionOnLog = SysTtsForwarderService.ACTION_ON_LOG,
                actionOnClosed = SysTtsForwarderService.ACTION_ON_CLOSED,
                actionOnStarted = SysTtsForwarderService.ACTION_ON_STARTED,
                isRunning = isRunning,
                onRunningChange = { isRunning = it },
                switch = {
                    SystemTtsForwarderConfig.isAutoStart.value = !SysTtsForwarderService.isRunning
                    context.switchSysTtsForwarder()
                },
                port = port,
                onPortChange = { port = it }
            )
        }) {
        "http://localhost:${port}"
    }
}
