package com.github.jing332.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.jing332.database.entities.plugin.Plugin
import com.github.jing332.database.entities.plugin.PluginIdVersion
import kotlinx.coroutines.flow.Flow

@Dao
interface PluginDao {
    @get:Query("SELECT * FROM plugin ORDER BY `order` ASC")
    val all: List<Plugin>

    @get:Query("SELECT * FROM plugin WHERE isEnabled = '1' ORDER BY `order` ASC")
    val allEnabled: List<Plugin>

    /**
     * 轻量查询：不包含 code 字段，用于列表展示等无需执行脚本的场景
     * 避免大 code 字段导致 CursorWindow 溢出
     */
    @get:Query("SELECT id, isEnabled, version, name, pluginId, author, iconUrl, '' as code, defVars, userVars, `order`, audioParams FROM plugin ORDER BY `order` ASC")
    val allLite: List<Plugin>

    @get:Query("SELECT id, isEnabled, version, name, pluginId, author, iconUrl, '' as code, defVars, userVars, `order`, audioParams FROM plugin WHERE isEnabled = '1' ORDER BY `order` ASC")
    val allEnabledLite: List<Plugin>

    @Query("SELECT * FROM plugin ORDER BY `order` ASC")
    fun flowAll(): Flow<List<Plugin>>

    @Query("SELECT id, isEnabled, version, name, pluginId, author, iconUrl, '' as code, defVars, userVars, `order`, audioParams FROM plugin ORDER BY `order` ASC")
    fun flowAllLite(): Flow<List<Plugin>>

    @get:Query("SELECT count(*) FROM plugin")
    val count: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: Plugin)

    @Delete
    fun delete(vararg data: Plugin)

    @Update
    fun update(vararg data: Plugin)

    @Query("SELECT * FROM plugin WHERE pluginId = :pluginId ")
    fun getByPluginId(pluginId: String): Plugin?

    @Query("SELECT * FROM plugin WHERE id = :id LIMIT 1")
    fun getById(id: Long): Plugin?

    /** 轻量查询：只取 id + version，避免 insertOrUpdate 时加载大 code 字段 */
    @Query("SELECT id, version FROM plugin WHERE pluginId = :pluginId LIMIT 1")
    fun getIdVersionByPluginId(pluginId: String): PluginIdVersion?

    @Query("SELECT * FROM plugin WHERE pluginId = :pluginId AND isEnabled")
    fun getEnabled(pluginId: String): Plugin?

    /** 字段级更新：避免用轻量实体覆盖 code */
    @Query("UPDATE plugin SET isEnabled = :isEnabled WHERE id = :id")
    fun updateEnabled(id: Long, isEnabled: Boolean)

    @Query("UPDATE plugin SET `order` = :order WHERE id = :id")
    fun updateOrder(id: Long, order: Int)

    fun insertOrUpdate(vararg args: Plugin) {
        for (v in args) {
            val old = getIdVersionByPluginId(v.pluginId)
            if (old == null) {
                insert(v)
                continue
            }

            if (v.version > old.version)
                update(v.copy(id = old.id))
        }
    }
}