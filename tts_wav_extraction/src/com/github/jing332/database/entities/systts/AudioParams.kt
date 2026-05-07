package com.github.jing332.database.entities.systts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * 音频参数配置
 *
 * 包含语速、音量和音高的配置
 */
@Parcelize
@Serializable
data class AudioParams(
    /** 语速，默认跟随系统 */
    var speed: Float = FOLLOW,

    /** 音量，默认跟随系统 */
    var volume: Float = FOLLOW,

    /** 音高，默认跟随系统 */
    var pitch: Float = FOLLOW,
) : Parcelable {

    companion object {
        /** 跟随系统值 */
        const val FOLLOW = 0f
    }

    /**
     * 是否为默认值
     */
    val isDefaultValue: Boolean
        get() = speed == 1f && volume == 1f && pitch == 1f

    /**
     * 如果当前值为 FOLLOW，则使用传入的值
     *
     * @param followSpeed 跟随的语速
     * @param followVolume 跟随的音量
     * @param followPitch 跟随的音高
     * @return 新的 AudioParams
     */
    fun copyIfFollow(followSpeed: Float, followVolume: Float, followPitch: Float): AudioParams {
        return AudioParams(
            if (speed == FOLLOW) followSpeed else speed,
            if (volume == FOLLOW) followVolume else volume,
            if (pitch == FOLLOW) followPitch else pitch
        )
    }

    /**
     * 如果当前值为 FOLLOW，则使用传入的 AudioParams 的值
     *
     * @param params 另一个 AudioParams
     * @return 新的 AudioParams
     */
    fun copyIfFollow(params: AudioParams): AudioParams {
        return AudioParams(
            if (speed == FOLLOW) params.speed else speed,
            if (volume == FOLLOW) params.volume else volume,
            if (pitch == FOLLOW) params.pitch else pitch
        )
    }

    /**
     * 重置所有值为指定值
     *
     * @param v 要设置的值
     */
    fun reset(v: Float) {
        speed = v
        volume = v
        pitch = v
    }
}
