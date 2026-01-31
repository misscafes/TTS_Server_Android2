@file:Suppress("OVERRIDE_DEPRECATION")

package com.github.jing332.tts_server_android.service.forwarder.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.speech.tts.TextToSpeech
import android.util.Log
import com.github.jing332.database.entities.systts.AudioParams
import com.github.jing332.database.entities.systts.source.LocalTtsParameter
import com.github.jing332.server.forwarder.Engine
import com.github.jing332.server.forwarder.SystemTtsForwardServer
import com.github.jing332.server.forwarder.TtsParams
import com.github.jing332.server.forwarder.Voice
import com.github.jing332.tts.speech.local.AndroidTtsEngine
import com.github.jing332.tts.speech.local.LocalTtsProvider
import com.github.jing332.tts_server_android.App
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.conf.SystemTtsConfig
import com.github.jing332.tts_server_android.conf.SystemTtsForwarderConfig
import com.github.jing332.tts_server_android.help.LocalTtsEngineHelper
import com.github.jing332.tts_server_android.service.forwarder.AbsForwarderService
import com.github.jing332.tts_server_android.service.keepalive.KeepAliveJobService
import com.github.jing332.tts_server_android.service.systts.SystemTtsService
import com.github.michaelbull.result.onFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File

class SysTtsForwarderService(
    override val port: Int = SystemTtsForwarderConfig.port.value,
    override val isWakeLockEnabled: Boolean = SystemTtsForwarderConfig.isWakeLockEnabled.value,
) : AbsForwarderService(
    "SysTtsForwarderService",
    id = 1221,
    actionLog = ACTION_ON_LOG,
    actionStarted = ACTION_ON_STARTED,
    actionClosed = ACTION_ON_CLOSED,
    notificationChanId = "systts_forwarder_status",
    notificationChanTitle = R.string.forwarder_systts,
    notificationIcon = R.drawable.ic_baseline_compare_arrows_24,
    notificationTitle = R.string.forwarder_systts,
) {
    companion object {
        const val TAG = "SysTtsServerService"
        const val ACTION_ON_CLOSED = "ACTION_ON_CLOSED"
        const val ACTION_ON_STARTED = "ACTION_ON_STARTED"
        const val ACTION_ON_LOG = "ACTION_ON_LOG"
        // 保活检查间隔（毫秒）
        const val CHECK_INTERVAL_MS = 5000L

        val isRunning: Boolean
            get() = instance?.isRunning == true

        var instance: SysTtsForwarderService? = null
    }

    private var mServer: SystemTtsForwardServer? = null
    private var mLocalTTS: LocalTtsProvider? = null
    private val mLocalTtsHelper by lazy { LocalTtsEngineHelper(this) }
    private val androidTts by lazy { AndroidTtsEngine(this) }

    // 保活相关
    private val keepAliveScope = CoroutineScope(Dispatchers.Default + Job())
    private var keepAliveJob: Job? = null
    private var audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private val handler = Handler(Looper.getMainLooper())

    // 屏幕状态监听
    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    if (SystemTtsConfig.isKeepAliveSilentAudioEnabled.value) {
                        playSilentAudio()
                    }
                }
                Intent.ACTION_SCREEN_ON -> {
                    stopSilentAudio()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // 如果启用了保活，启动保活机制
        if (SystemTtsConfig.isKeepAliveEnabled.value) {
            startKeepAlive()
        }
    }

    override fun onDestroy() {
        // 停止保活
        stopKeepAlive()
        super.onDestroy()
    }

    /**
     * 启动保活机制
     */
    private fun startKeepAlive() {
        // 注册屏幕状态监听
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenStateReceiver, filter)

        // 启动保活任务
        keepAliveJob = keepAliveScope.launch {
            while (isActive) {
                performKeepAliveActions()
                delay(CHECK_INTERVAL_MS)
            }
        }

        // 调度 JobScheduler 作为后备保活
        KeepAliveJobService.schedule(this)
    }

    /**
     * 停止保活机制
     */
    private fun stopKeepAlive() {
        keepAliveJob?.cancel()
        stopSilentAudio()
        try {
            unregisterReceiver(screenStateReceiver)
        } catch (_: Exception) {}
    }

    /**
     * 执行保活操作
     */
    private fun performKeepAliveActions() {
        // 1. 调用系统闹钟API保持CPU活跃
        SystemClock.sleep(1)

        // 2. 定期请求音频焦点（保持音频服务活跃）
        if (SystemTtsConfig.isKeepAliveAudioFocusEnabled.value) {
            requestAudioFocus()
        }
    }

    /**
     * 播放静音音频（防止CPU休眠）
     */
    private fun playSilentAudio() {
        requestAudioFocus()
    }

    /**
     * 停止静音音频
     */
    private fun stopSilentAudio() {
        abandonAudioFocus()
    }

    /**
     * 请求音频焦点
     */
    private fun requestAudioFocus() {
        audioManager = audioManager ?: getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT).apply {
                setAudioAttributes(AudioAttributes.Builder().apply {
                    setUsage(AudioAttributes.USAGE_MEDIA)
                    setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                }.build())
                setAcceptsDelayedFocusGain(true)
                setOnAudioFocusChangeListener { }
            }.build()
            audioManager?.requestAudioFocus(audioFocusRequest!!)
        } else {
            @Suppress("DEPRECATION")
            audioManager?.requestAudioFocus(
                { },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    /**
     * 放弃音频焦点
     */
    private fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let {
                audioManager?.abandonAudioFocusRequest(it)
            }
        } else {
            @Suppress("DEPRECATION")
            audioManager?.abandonAudioFocus { }
        }
        audioFocusRequest = null
    }

    override fun initServer() {
    }

    override fun startServer() {
        mServer = SystemTtsForwardServer(port, object : SystemTtsForwardServer.Callback {
            override fun log(level: Int, message: String) {
                sendLog(level, message)
            }

            override suspend fun tts(params: TtsParams): File? {
                val speed = (params.speed + 100) / 100f
                val pitch = params.pitch / 100f

                return withContext(NonCancellable) {
                    withTimeoutOrNull(130000L) {
                        Log.d(TAG, "android tts init: ${params.engine}")
                        sendLog(com.github.jing332.common.LogLevel.DEBUG, "初始化引擎: ${params.engine}")
                        androidTts.init(params.engine)

                        Log.d(TAG, "android tts get file...")
                        sendLog(com.github.jing332.common.LogLevel.DEBUG, "获取音频文件...")
                        val result = androidTts.getFile(
                            params.text,
                            params.locale,
                            voice = params.voice,
                            extraParams = listOf(
                                LocalTtsParameter(
                                    type = LocalTtsParameter.TYPE_BOOL,
                                    key = SystemTtsService.PARAM_BGM_ENABLED,
                                    value = false.toString()
                                )
                            ),
                            params = AudioParams(speed = speed, pitch = pitch)
                        )

                        result.onFailure {
                            // 🛠️ 修正：直接打印 it，解决 Unresolved reference 'message'
                            Log.e(TAG, "获取音频失败: $it")
                            sendLog(com.github.jing332.common.LogLevel.ERROR, "获取音频失败: $it")
                            return@withTimeoutOrNull null
                        }.value
                    }
                }
            }

            override suspend fun voices(engine: String): List<Voice> {
                val ok = mLocalTtsHelper.setEngine(engine)
                if (!ok) throw IllegalStateException(getString(R.string.systts_engine_init_failed_timeout))

                return mLocalTtsHelper.voices.map {
                    Voice(
                        name = it.name,
                        locale = it.locale.toLanguageTag(),
                        localeName = it.locale.getDisplayName(it.locale),
                        features = it.features?.toList()
                    )
                }
            }

            override suspend fun engines(): List<Engine> =
                getSysTtsEngines().map { Engine(name = it.name, it.label) }


        })
        mServer?.start(true,
            onStarted = {
                notifiStarted()
            }, onStopped = {
                notifiClosed()
            }
        )
    }

    override fun closeServer() {
        mServer?.let {
            it.stop()
            mLocalTTS?.onDestroy()
            mLocalTTS = null
        }
    }

    private fun getSysTtsEngines(): List<TextToSpeech.EngineInfo> {
        val tts = TextToSpeech(App.context, null)
        val engines = tts.engines
        tts.shutdown()
        return engines
    }

}
