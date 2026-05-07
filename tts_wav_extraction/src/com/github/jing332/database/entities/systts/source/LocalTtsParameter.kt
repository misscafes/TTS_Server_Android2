package com.github.jing332.database.entities.systts.source

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * 本地 TTS 参数配置
 *
 * 用于存储额外的 TTS 引擎参数
 * 这些参数会被转换为 Bundle 并传递给 TTS 引擎
 */
@Parcelize
@Serializable
data class LocalTtsParameter(
    /** 参数类型：Boolean, Int, Float, String */
    val type: String,

    /** 参数键名 */
    val key: String,

    /** 参数值（字符串形式） */
    val value: String
) : Parcelable {

    companion object {
        /** Boolean 类型 */
        const val TYPE_BOOL: String = "Boolean"

        /** Int 类型 */
        const val TYPE_INT: String = "Int"

        /** Float 类型 */
        const val TYPE_FLOAT: String = "Float"

        /** String 类型 */
        const val TYPE_STRING: String = "String"

        /** 所有支持的类型列表 */
        val typeList = listOf(
            TYPE_BOOL,
            TYPE_INT,
            TYPE_FLOAT,
            TYPE_STRING
        )
    }

    /**
     * 将参数值放入 Bundle
     *
     * 根据 type 类型将 value 转换为对应的类型并放入 Bundle
     *
     * @param b Bundle 对象
     */
    fun putValueFromBundle(b: Bundle) {
        when (type) {
            TYPE_BOOL -> b.putBoolean(key, value.toBoolean())
            TYPE_INT -> b.putInt(key, value.toInt())
            TYPE_FLOAT -> b.putFloat(key, value.toFloat())
            else -> b.putString(key, value)
        }
    }
}
