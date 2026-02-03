package com.github.jing332.tts_server_android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Process 
import com.github.jing332.compose.widgets.AsyncCircleImageSettings
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.tts_server_android.conf.SystemTtsForwarderConfig
import com.github.jing332.tts_server_android.constant.AppConst
import com.github.jing332.tts_server_android.model.hanlp.HanlpManager
import com.github.jing332.tts_server_android.service.forwarder.ForwarderServiceManager
import com.github.jing332.tts_server_android.service.forwarder.ForwarderServiceManager.switchSysTtsForwarder
import com.github.jing332.tts_server_android.service.forwarder.system.SysTtsForwarderService
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.request.crossfade
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
// 👇 新增：NetConfig 配置所需的包
import com.drake.net.NetConfig
import java.util.concurrent.TimeUnit

val app: App
    inline get() = App.instance

@Suppress("DEPRECATION")
class App : Application() {
    companion object {
        const val TAG = "App"
        lateinit var instance: App
            private set

        val context: Context by lazy { instance }
    }

    override fun attachBaseContext(base: Context) {
        instance = this
        super.attachBaseContext(base.apply { AppLocale.setLocale(base) })
    }

    @SuppressLint("SdCardPath")
    @OptIn(DelicateCoroutinesApi::class, DelicateCoilApi::class)
    override fun onCreate() {
        super.onCreate()
        
        // 🛠️ 拔掉引线：暂时关闭 CrashHandler，它会触发崩溃的日志初始化
        // CrashHandler(this) 

        // 👇 新增：初始化 NetConfig 并设置全局超时时间为 180秒
        // 这将覆盖默认的 10秒 限制，适用于所有使用 Net 库的请求
        NetConfig.initialize("", this) {
            connectTimeout(180, TimeUnit.SECONDS)
            readTimeout(180, TimeUnit.SECONDS)
            writeTimeout(180, TimeUnit.SECONDS)
        }

        SystemTtsV2.Converters.json = AppConst.jsonBuilder
        AsyncCircleImageSettings.interceptor = AsyncImageInterceptor

        SingletonImageLoader.setUnsafe(
            ImageLoader
                .Builder(context)
                .crossfade(true)
                .build()
        )

        GlobalScope.launch {
            HanlpManager.initDir(
                context.getExternalFilesDir("hanlp")?.absolutePath
                    ?: "/data/data/$packageName/files/hanlp"
            )

            // 检查是否是重启后恢复转发器状态
            val forwarderWasRunning = com.github.jing332.tts_server_android.compose.RestartActivity.getAndClearForwarderState(context)
            if (forwarderWasRunning && !SysTtsForwarderService.isRunning) {
                // 延迟一点确保应用完全启动
                kotlinx.coroutines.delay(500)
                ForwarderServiceManager.run { context.startSysTtsForwarder() }
            } else if (SystemTtsForwarderConfig.isAutoStart.value && !SysTtsForwarderService.isRunning) {
                switchSysTtsForwarder()
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun restart() {
        // 保存转发器状态（备份恢复后的重启也需要恢复转发器）
        val forwarderWasRunning = SysTtsForwarderService.isRunning
        com.github.jing332.tts_server_android.compose.RestartActivity.saveState(this, forwarderWasRunning)
        
        val intent = packageManager.getLaunchIntentForPackage(packageName)!!
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        Process.killProcess(Process.myPid())
    }
}
