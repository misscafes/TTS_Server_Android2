package com.github.jing332.tts

import com.github.jing332.tts.error.RequesterError
import com.github.jing332.tts.synthesizer.ITtsRequester
import com.github.jing332.tts.synthesizer.SystemParams
import com.github.jing332.tts.synthesizer.TtsConfiguration
import com.github.jing332.tts.speech.EngineState
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

class DefaultTtsRequester(
    var context: SynthesizerContext,
) : ITtsRequester {
    override suspend fun request(
        params: SystemParams, tts: TtsConfiguration,
    ): Result<ITtsRequester.Response, RequesterError> {
        val engine =
            CachedEngineManager.getEngine(context.androidContext, tts.source) ?: return Err(
                RequesterError.StateError("engine ${tts.source} not found")
            )

        if (engine.state != EngineState.Initialized) {
            try {
                engine.onInit()
            } catch (e: Exception) {
                return Err(RequesterError.RequestError(e))
            }
        }

        // 合并 TtsConfiguration.audioParams 到 SystemParams
        // 确保 TtsRepository 中计算的最终音频参数被使用
        val mergedParams = params.copy(
            speed = tts.audioParams.speed,
            volume = tts.audioParams.volume,
            pitch = tts.audioParams.pitch
        )

        return if (engine.isSyncPlay(tts.source)) {
            Ok(
                ITtsRequester.Response(
                    callback = ITtsRequester.ISyncPlayCallback {
                        engine.syncPlay(mergedParams, tts.source)
                    }
                )
            )
        } else {
            try {
                // 【核心修改】确保超时时长足够长。
                // 如果配置里没拿到底层 UI 的值，默认给 5 分钟 (300,000ms)
                val timeout = (context.cfg.requestTimeout() ?: 300000).toLong()
                withTimeout(timeout) {
                    Ok(
                        ITtsRequester.Response(stream = engine.getStream(mergedParams, tts.source))
                    )
                }
            } catch (e: TimeoutCancellationException) {
                // 超时后强制销毁引擎并从缓存移除，确保下次重试是新连接
                engine.onDestroy()
                CachedEngineManager.removeEngine(tts.source)
                throw e
            } catch (e: CancellationException) {
                // 如果是协程主动取消，继续抛出
                throw e
            } catch (e: Exception) {
                engine.onDestroy()
                CachedEngineManager.removeEngine(tts.source)
                Err(RequesterError.RequestError(e))
            }
        }
    }

    override fun destroy() {
    }
}
