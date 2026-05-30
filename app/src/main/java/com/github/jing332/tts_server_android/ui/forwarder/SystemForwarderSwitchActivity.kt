package com.github.jing332.tts_server_android.ui.forwarder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.github.jing332.common.utils.startForegroundServiceCompat
import com.github.jing332.tts_server_android.service.forwarder.system.SysTtsForwarderService

class SystemForwarderSwitchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SysTtsForwarderService.isRunning)
            SysTtsForwarderService.instance?.close()
        else
            startForegroundServiceCompat(Intent(this, SysTtsForwarderService::class.java))

        finish()
    }
}