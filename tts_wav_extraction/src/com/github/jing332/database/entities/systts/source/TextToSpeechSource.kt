package com.github.jing332.database.entities.systts.source

import android.os.Parcelable
import com.github.jing332.database.entities.systts.BasicAudioFormat
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator


/**
 * 文本转语音数据源基类
 *
 * 这是一个密封类，定义了所有 TTS 数据源的通用接口
 */
@Parcelize
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("#type")
sealed class TextToSpeechSource : Parcelable {

    /**
     * 是否为同步播放模式
     *
     * 子类可以重写此方法
     *
     * @return true 直接播放，false 中转
     */
    open fun isSyncPlayMode(): Boolean = false

    /**
     * 是否需要解码
     *
     * @param format 音频格式
     * @return true 需要解码，false 不需要
     */
    open fun shouldDecode(format: BasicAudioFormat): Boolean = format.isNeedDecode

    /**
     * 获取唯一标识键
     *
     * @return 唯一标识
     */
    open fun getKey(): String {
        return javaClass.simpleName
    }

    /**
     * 语言标签
     */
    abstract val locale: String

    /**
     * 声音名称
     */
    abstract val voice: String
}
