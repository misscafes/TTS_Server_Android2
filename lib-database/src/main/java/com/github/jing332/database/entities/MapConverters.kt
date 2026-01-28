package com.github.jing332.database.entities

import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object MapConverters {
    @OptIn(ExperimentalSerializationApi::class)
    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            allowStructuredMapKeys = true
            explicitNulls = false
            isLenient = true
            coerceInputValues = true
        }
    }

    @TypeConverter
    fun toMap(s: String): Map<String, String> {
        return try {
            if (s.isBlank()) return emptyMap()
            json.decodeFromString(s)
        } catch (e: SerializationException) {
            // 反序列化失败时返回空 Map，避免 Crash
            emptyMap()
        } catch (e: IllegalArgumentException) {
            emptyMap()
        }
    }

    @TypeConverter
    fun fromMap(tags: Map<String, String>): String {
        return try {
            json.encodeToString(tags)
        } catch (e: SerializationException) {
            "{}"
        }
    }

    @TypeConverter
    fun toNestMap(s: String): Map<String, Map<String, String>> {
        return try {
            if (s.isBlank()) return emptyMap()
            json.decodeFromString(s)
        } catch (e: SerializationException) {
            // 反序列化失败时返回空 Map，避免 Crash
            emptyMap()
        } catch (e: IllegalArgumentException) {
            emptyMap()
        }
    }

    @TypeConverter
    fun fromNestMap(map: Map<String, Map<String, String>>): String {
        return try {
            json.encodeToString(map)
        } catch (e: SerializationException) {
            "{}"
        }
    }

    @TypeConverter
    fun toMapList(s: String): Map<String, List<Map<String, String>>> {
        return try {
            if (s.isBlank()) return emptyMap()
            json.decodeFromString(s)
        } catch (e: SerializationException) {
            // 反序列化失败时返回空 Map，避免 Crash
            emptyMap()
        } catch (e: IllegalArgumentException) {
            emptyMap()
        }
    }

    @TypeConverter
    fun fromMapList(tags: Map<String, List<Map<String, String>>>): String {
        return try {
            json.encodeToString(tags)
        } catch (e: SerializationException) {
            "{}"
        }
    }
}