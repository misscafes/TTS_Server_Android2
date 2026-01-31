package com.github.jing332.tts_server_android.service.keepalive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.github.jing332.tts_server_android.conf.SysTtsConfig
import com.github.jing332.tts_server_android.conf.SystemTtsForwarderConfig
import com.github.jing332.tts_server_android.service.forwarder.ForwarderServiceManager.startSysTtsForwarder

/**
 * 开机启动广播接收器
 * 设备重启后自动启动相关服务
 */
class BootCompleteReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "BootCompleteReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_REBOOT,
            "android.intent.action.QUICKBOOT_POWERON" -> {
                android.util.Log.d(TAG, "Boot completed received")

                // 如果启用了自启动
                if (SysTtsConfig.isAutoStartEnabled) {
                    android.util.Log.d(TAG, "Auto start enabled, starting services...")

                    // 启动保活服务
                    if (SysTtsConfig.isKeepAliveEnabled) {
                        KeepAliveService.start(context)
                    }

                    // 如果之前转发器是开启状态，也启动它
                    if (SystemTtsForwarderConfig.isAutoStart.value) {
                        context.startSysTtsForwarder()
                    }

                    // 调度 JobScheduler 保活任务
                    KeepAliveJobService.schedule(context)
                }
            }
        }
    }
}
