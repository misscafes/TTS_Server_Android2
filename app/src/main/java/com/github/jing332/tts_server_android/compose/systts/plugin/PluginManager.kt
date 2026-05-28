package com.github.jing332.tts_server_android.compose.systts.plugin

import com.github.jing332.database.entities.plugin.Plugin
import com.github.jing332.tts_server_android.constant.AppConst
import splitties.init.appCtx
import java.io.File

class PluginManager(private val pluginId: String) {
    constructor(plugin: Plugin) : this(plugin.pluginId)

    companion object {
        // 缓存目录：/storage/emulated/0/Download/chajian
        const val CACHE_BASE_DIR = "/storage/emulated/0/Download/chajian"
    }

    // 存储路径：/storage/emulated/0/Download/chajian/<pluginId>
    private val cacheDir = File(CACHE_BASE_DIR, pluginId)

    // 旧的存储路径：ExternalCacheDir (用于清理残留)
    private val legacyCacheDir = File(appCtx.getExternalFilesDir("plugin_cache"), pluginId)

    fun hasCache(): Boolean {
        return try {
            (cacheDir.list()?.isNotEmpty() == true) || (legacyCacheDir.list()?.isNotEmpty() == true)
        } catch (e: Exception) {
            false
        }
    }

    fun clearCache() {
        try {
            cacheDir.deleteRecursively()
            legacyCacheDir.deleteRecursively()
        } catch (_: Exception) {
        }
    }
}
