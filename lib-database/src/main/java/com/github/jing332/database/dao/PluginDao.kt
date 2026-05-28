package com.github.jing332.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.jing332.database.entities.plugin.Plugin
import com.github.jing332.database.entities.plugin.PluginListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PluginDao {
    @get:Query("SELECT * FROM plugin ORDER BY `order` ASC")
    val all: List<Plugin>

    @get:Query("SELECT * FROM plugin WHERE isEnabled = '1' ORDER BY `order` ASC")
    val allEnabled: List<Plugin>

    @Query("SELECT * FROM plugin ORDER BY `order` ASC")
    fun flowAll(): Flow<List<Plugin>>

    @Query("""
        SELECT id, isEnabled, name, version, pluginId, author, iconUrl, `order`,
            (defVars <> '{}' AND defVars <> '') as hasDefVars,
            ((defVars <> '{}' AND defVars <> '') AND (userVars = '{}' OR userVars = '')) as needSetVars
        FROM plugin ORDER BY `order` ASC
    """)
    fun flowAllListItems(): Flow<List<PluginListItem>>

    @get:Query("SELECT count(*) FROM plugin")
    val count: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: Plugin)

    @Delete
    fun delete(vararg data: Plugin)

    @Query("DELETE FROM plugin WHERE id = :id")
    fun deleteById(id: Long)

    @Update
    fun update(vararg data: Plugin)

    @Query("UPDATE plugin SET `order` = :order WHERE id = :id")
    fun updateOrder(id: Long, order: Int)

    @Query("UPDATE plugin SET isEnabled = :isEnabled WHERE id = :id")
    fun updateEnabled(id: Long, isEnabled: Boolean)

    @Query("SELECT * FROM plugin WHERE id = :id LIMIT 1")
    fun getById(id: Long): Plugin?

    @Query("SELECT * FROM plugin WHERE pluginId = :pluginId ")
    fun getByPluginId(pluginId: String): Plugin?

    @Query("SELECT * FROM plugin WHERE pluginId = :pluginId AND isEnabled")
    fun getEnabled(pluginId: String): Plugin?

    fun insertOrUpdate(vararg args: Plugin) {
        for (v in args) {
            val old = getByPluginId(v.pluginId)
            if (old == null) {
                insert(v)
                continue
            }

            if (v.pluginId == old.pluginId && v.version > old.version)
                update(v.copy(id = old.id))
        }
    }
}