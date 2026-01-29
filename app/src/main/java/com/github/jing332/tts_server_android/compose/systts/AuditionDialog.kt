package com.github.jing332.tts_server_android.compose.systts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.drake.net.utils.withMain
import com.github.jing332.common.audio.AudioPlayer
import com.github.jing332.common.utils.messageChain
import com.github.jing332.common.utils.sizeToReadable
import com.github.jing332.compose.widgets.AppDialog
import com.github.jing332.compose.widgets.LoadingContent
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.TtsConfigurationDTO
import com.github.jing332.database.entities.systts.source.TextToSpeechSource
import com.github.jing332.tts.CachedEngineManager
import com.github.jing332.tts.speech.EngineState
import com.github.jing332.tts.speech.TextToSpeechProvider
import com.github.jing332.tts.synthesizer.SystemParams
import com.github.jing332.tts.synthesizer.TtsConfiguration
import com.github.jing332.tts.synthesizer.TtsConfiguration.Companion.toVO
import com.github.jing332.tts_server_android.R
import com.github.jing332.tts_server_android.conf.AppConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import splitties.init.appCtx


private val logger = KotlinLogging.logger("AuditionDialog")

@Composable
fun AuditionDialog(
    systts: SystemTtsV2,
    text: String = AppConfig.testSampleText.value,
    engine: TextToSpeechProvider<TextToSpeechSource>? = null,
    onDismissRequest: () -> Unit,
) {
    // 关键修复：使用音频参数作为key，确保参数变化时重新创建对话框和Effect
    val audioParams = (systts.config as TtsConfigurationDTO).audioParams
    key(audioParams.speed, audioParams.volume, audioParams.pitch) {
        val context = LocalContext.current
        var error by remember { mutableStateOf("") }
        var info by remember { mutableStateOf("") }
        val audioPlayer = remember { AudioPlayer(context) }
        var job by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

        DisposableEffect(systts) {
            onDispose {
                job?.cancel()
                audioPlayer.stop()
                audioPlayer.release()
            }
        }

        LaunchedEffect(systts) {
            job = launch(Dispatchers.IO) {
                try {
                    // 每次都重新计算 config，确保使用最新的 audioParams
                    val configDto = systts.config as TtsConfigurationDTO
                    logger.debug { "AuditionDialog: systts.config.audioParams=${configDto.audioParams}" }
                    val config = configDto.toVO()
                    logger.debug { "AuditionDialog: config.audioParams.volume=${config.audioParams.volume}, speed=${config.audioParams.speed}, pitch=${config.audioParams.pitch}" }
                    val e = engine ?: CachedEngineManager.getEngine(appCtx, config.source)
                    ?: throw IllegalStateException("engine is null")

                    if (e.state is EngineState.Uninitialized) e.onInit()
                    // 二者合一：优先从 source 读取音频参数（兼容 5ad82463），其次从 audioParams 读取
                    val source = config.source
                    val (speed, volume, pitch) = if (source is com.github.jing332.database.entities.systts.source.PluginTtsSource) {
                        Triple(
                            source.speed.takeIf { it > 0 } ?: config.audioParams.speed.takeIf { it > 0 } ?: 1f,
                            source.volume.takeIf { it > 0 } ?: config.audioParams.volume.takeIf { it > 0 } ?: 1f,
                            source.pitch.takeIf { it > 0 } ?: config.audioParams.pitch.takeIf { it > 0 } ?: 1f
                        )
                    } else {
                        Triple(
                            config.audioParams.speed.takeIf { it > 0 } ?: 1f,
                            config.audioParams.volume.takeIf { it > 0 } ?: 1f,
                            config.audioParams.pitch.takeIf { it > 0 } ?: 1f
                        )
                    }
                    val params = SystemParams(
                        text = text,
                        speed = speed,
                        volume = volume,
                        pitch = pitch
                    )
                    if (e.isSyncPlay(config.source)) {
                        e.syncPlay(params, config.source)
                    } else {
                        val stream = e.getStream(params, config.source)
                        val audio = stream.readBytes()
                        val rateAndMime =
                            com.github.jing332.common.audio.AudioDecoder.getSampleRateAndMime(audio)
                        
                        // 检查协程是否仍然活跃
                        if (!isActive) return@launch
                        
                        withMain {
                            if (isActive) {
                                info = context.getString(
                                    R.string.systts_test_success_info, audio.size.toLong().sizeToReadable(),
                                    rateAndMime.first, rateAndMime.second
                                )
                            }
                        }

                        if (config.shouldDecode())
                            audioPlayer.play(audio)
                        else
                            audioPlayer.play(audio, config.audioFormat.sampleRate)
                    }
                    
                    // 检查协程是否仍然活跃再关闭对话框
                    if (isActive) {
                        withContext(Dispatchers.Main) {
                            if (isActive) {
                                onDismissRequest()
                            }
                        }
                    }
                } catch (e: IOException) {
                    if (isActive) error = e.cause.toString()
                } catch (e: Exception) {
                    if (isActive) {
                        error = e.messageChain
                        logger.warn { e.stackTraceToString() }
                    }
                }
            }
        }

        AppDialog(onDismissRequest = onDismissRequest,
            title = { Text(stringResource(id = R.string.audition)) },
            content = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    SelectionContainer {
                        Text(
                            error.ifEmpty { text },
                            color = if (error.isEmpty()) Color.Unspecified else MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (error.isEmpty())
                        LoadingContent(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            isLoading = info.isEmpty()
                        ) {
                            SelectionContainer {
                                Text(info, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                }
            },
            buttons = {
                TextButton(onClick = onDismissRequest) { Text(stringResource(id = R.string.cancel)) }
            }
        )
    }
}