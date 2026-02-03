package com.github.jing332.tts_server_android.compose

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.compose.theme.AppTheme
import com.github.jing332.tts_server_android.service.systts.SystemTtsService
import kotlinx.coroutines.delay

/**
 * 重启过渡界面
 * 点击首页重启键后先跳转到此界面，显示加载动画，然后执行重启
 */
class RestartActivity : AppCompatActivity() {
    
    companion object {
        private const val PREFS_NAME = "restart_state"
        private const val KEY_FORWARDER_WAS_RUNNING = "forwarder_was_running"
        
        /**
         * 保存重启前的状态
         */
        fun saveState(context: Context, forwarderWasRunning: Boolean) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_FORWARDER_WAS_RUNNING, forwarderWasRunning)
                .apply()
        }
        
        /**
         * 获取并清除转发器状态
         */
        fun getAndClearForwarderState(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val wasRunning = prefs.getBoolean(KEY_FORWARDER_WAS_RUNNING, false)
            prefs.edit().remove(KEY_FORWARDER_WAS_RUNNING).apply()
            return wasRunning
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            AppTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.restarting),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                
                LaunchedEffect(Unit) {
                    // 延迟一点时间让用户看到动画
                    delay(300)
                    // 执行真正的重启
                    SystemTtsService.doRestartApp(this@RestartActivity)
                }
            }
        }
    }
}
