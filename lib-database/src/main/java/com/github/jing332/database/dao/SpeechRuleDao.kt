package com.github.jing332.database.dao

import androidx.room.*
import com.github.jing332.database.entities.SpeechRule
import com.github.jing332.database.entities.SpeechRuleListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeechRuleDao {
    @get:Query("SELECT * FROM speech_rules ORDER BY `order` ASC")
    val all: List<SpeechRule>

    @get:Query("SELECT * FROM speech_rules WHERE isEnabled = '1'")
    val allEnabled: List<SpeechRule>

    @Query("SELECT * FROM speech_rules ORDER BY `order` ASC")
    fun flowAll(): Flow<List<SpeechRule>>

    @Query("SELECT id, isEnabled, name, version, ruleId, author, `order` FROM speech_rules ORDER BY `order` ASC")
    fun flowAllListItems(): Flow<List<SpeechRuleListItem>>

    @get:Query("SELECT count(*) FROM speech_rules")
    val count: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: SpeechRule)

    @Delete
    fun delete(vararg data: SpeechRule)

    @Query("DELETE FROM speech_rules WHERE id = :id")
    fun deleteById(id: Long)

    @Update
    fun update(vararg data: SpeechRule)

    @Query("UPDATE speech_rules SET `order` = :order WHERE id = :id")
    fun updateOrder(id: Long, order: Int)

    @Query("UPDATE speech_rules SET isEnabled = :isEnabled WHERE id = :id")
    fun updateEnabled(id: Long, isEnabled: Boolean)

    @Query("SELECT * FROM speech_rules WHERE id = :id LIMIT 1")
    fun getById(id: Long): SpeechRule?

    @Query("SELECT * FROM speech_rules WHERE ruleId = :ruleId AND isEnabled = :isEnabled LIMIT 1")
    fun getByRuleId(ruleId: String, isEnabled: Boolean = true): SpeechRule?

//    @Query("SELECT * FROM speech_rules WHERE ruleId = :ruleId")
//    fun getByRuleId(ruleId: String): SpeechRule?

    fun insertOrUpdate(vararg args: SpeechRule) {
        for (v in args) {
            val old = getByRuleId(v.ruleId)
            if (old == null) {
                insert(v)
                continue
            }

            if (v.ruleId == old.ruleId && v.version > old.version)
                update(v.copy(id = old.id))
        }
    }
}