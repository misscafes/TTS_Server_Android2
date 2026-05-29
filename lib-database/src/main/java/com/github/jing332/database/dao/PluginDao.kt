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

    @Query("SELECT * FROM plugin ORDER BY `order` ASC")
    fun flowAll(): Flow<List<Plugin>>

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

    /** 轻量查询：只取 id + version，避免 insertOrUpdate 时加载大 code 字段 */
    @Query("SELECT id, version FROM plugin WHERE pluginId = :pluginId LIMIT 1")
    fun getIdVersionByPluginId(pluginId: String): PluginIdVersion?

    @Query("SELECT * FROM plugin WHERE pluginId = :pluginId AND isEnabled")
    fun getEnabled(pluginId: String): Plugin?

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