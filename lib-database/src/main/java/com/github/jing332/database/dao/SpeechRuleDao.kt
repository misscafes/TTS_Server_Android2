package com.github.jing332.database.dao

import androidx.room.*
import com.github.jing332.database.entities.SpeechRule
import com.github.jing332.database.entities.SpeechRuleIdVersion
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeechRuleDao {
    @get:Query("SELECT * FROM speech_rules ORDER BY `order` ASC")
    val all: List<SpeechRule>

    @get:Query("SELECT * FROM speech_rules WHERE isEnabled = '1'")
    val allEnabled: List<SpeechRule>

    /**
     * 轻量查询：不包含 code 字段，用于列表展示等无需执行脚本的场景
     * 避免大 code 字段导致 CursorWindow 溢出
     */
    @get:Query("SELECT id, isEnabled, name, version, ruleId, author, '' as code, tags, tagsData, `order` FROM speech_rules ORDER BY `order` ASC")
    val allLite: List<SpeechRule>

    @get:Query("SELECT id, isEnabled, name, version, ruleId, author, '' as code, tags, tagsData, `order` FROM speech_rules WHERE isEnabled = '1'")
    val allEnabledLite: List<SpeechRule>

    @Query("SELECT * FROM speech_rules ORDER BY `order` ASC")
    fun flowAll(): Flow<List<SpeechRule>>

    @Query("SELECT id, isEnabled, name, version, ruleId, author, '' as code, tags, tagsData, `order` FROM speech_rules ORDER BY `order` ASC")
    fun flowAllLite(): Flow<List<SpeechRule>>

    @get:Query("SELECT count(*) FROM speech_rules")
    val count: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: SpeechRule)

    @Delete
    fun delete(vararg data: SpeechRule)

    @Update
    fun update(vararg data: SpeechRule)

    @Query("SELECT * FROM speech_rules WHERE ruleId = :ruleId AND isEnabled = :isEnabled LIMIT 1")
    fun getByRuleId(ruleId: String, isEnabled: Boolean = true): SpeechRule?

    /** 轻量查询：只取 id + version，避免 insertOrUpdate 时加载大 code 字段 */
    @Query("SELECT id, version FROM speech_rules WHERE ruleId = :ruleId LIMIT 1")
    fun getIdVersionByRuleId(ruleId: String): SpeechRuleIdVersion?

    @Query("SELECT * FROM speech_rules WHERE id = :id LIMIT 1")
    fun getById(id: Long): SpeechRule?

//    @Query("SELECT * FROM speech_rules WHERE ruleId = :ruleId")
//    fun getByRuleId(ruleId: String): SpeechRule?

    /** 字段级更新：避免用轻量实体覆盖 code */
    @Query("UPDATE speech_rules SET isEnabled = :isEnabled WHERE id = :id")
    fun updateEnabled(id: Long, isEnabled: Boolean)

    @Query("UPDATE speech_rules SET `order` = :order WHERE id = :id")
    fun updateOrder(id: Long, order: Int)

    fun insertOrUpdate(vararg args: SpeechRule) {
        for (v in args) {
            val old = getIdVersionByRuleId(v.ruleId)
            if (old == null) {
                insert(v)
                continue
            }

            if (v.version > old.version)
                update(v.copy(id = old.id))
        }
    }
}