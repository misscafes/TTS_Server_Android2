package com.github.jing332.tts_server_android.compose.systts.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.jing332.common.utils.StringUtils
import com.github.jing332.common.utils.toJsonListString
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.systts.GroupWithSystemTts
import com.github.jing332.database.entities.systts.SystemTtsGroup
import com.github.jing332.database.entities.systts.SystemTtsMigration
import com.github.jing332.database.entities.systts.SystemTtsV2
import com.github.jing332.database.entities.systts.jread.JReadVoiceConfigBundle
import com.github.jing332.database.entities.systts.jread.JReadVoiceConfigConverter
import com.github.jing332.database.entities.systts.v1.GroupWithV1TTS
import com.github.jing332.tts_server_android.compose.systts.ConfigImportBottomSheet
import com.github.jing332.tts_server_android.compose.systts.ConfigModel
import com.github.jing332.tts_server_android.compose.systts.SelectImportConfigDialog
import com.github.jing332.tts_server_android.constant.AppConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun ListImportBottomSheet(onDismissRequest: () -> Unit) {
    var selectDialog by remember { mutableStateOf<List<ConfigModel>?>(null) }
    if (selectDialog != null) {
        SelectImportConfigDialog(
            onDismissRequest = { selectDialog = null },
            models = selectDialog!!,
            onSelectedList = { list ->
                withContext(Dispatchers.IO) {
                    list.map {
                        @Suppress("UNCHECKED_CAST")
                        it as Pair<SystemTtsGroup, SystemTtsV2>
                    }
                        .forEach {
                            val group = it.first
                            val tts = it.second
                            dbm.systemTtsV2.insertGroup(group)
                            dbm.systemTtsV2.insert(tts)
                        }
                }
                list.size
            }
        )
    }

    ConfigImportBottomSheet(
        onDismissRequest = onDismissRequest,
        autoWrapJsonList = false,
        onImport = { json ->
            val allList = withContext(Dispatchers.IO) {
                val result = mutableListOf<ConfigModel>()
                val importList = getImportList(json, false) ?: return@withContext result

                // 先插入所有分组（包括没有直接条目的父级分组），确保导入后树形结构完整
                val allGroups = importList.map { it.group }
                insertGroupsPreservingTree(allGroups)

                importList.forEach { groupWithTts ->
                    val group = groupWithTts.group
                    groupWithTts.list.forEach { sysTts ->
                        result.add(
                            ConfigModel(
                                true, sysTts.displayName.toString(),
                                group.name, group to sysTts
                            )
                        )
                    }
                }
                result
            }
            selectDialog = allList
        }
    )
}

/**
 * 按树形层级顺序插入分组：先插入 parentGroupId 为 0 的根分组，再插入子分组，
 * 确保外键/树形关系正确，保留完整的父级目录结构。
 */
private fun insertGroupsPreservingTree(groups: List<SystemTtsGroup>) {
    val groupMap = groups.associateBy { it.id }
    // 按层级排序：parentGroupId 为 0 的排在前面，子分组按 id/父id 顺序排
    val sorted = groups.sortedWith(compareBy({ it.parentGroupId }, { it.id }))
    sorted.forEach { group ->
        // 如果父分组不存在于当前导入列表且 parentGroupId != 0，
        // 保留原 parentGroupId（数据库中可能已经存在该父分组）
        dbm.systemTtsV2.insertGroup(group)
    }
}

private fun getImportList(
    json: String,
    fromLegado: Boolean,
): List<GroupWithSystemTts>? {
    val groupName = StringUtils.formattedDate()
    val groupId = System.currentTimeMillis()
    val groupCount = dbm.systemTtsV2.groupCount

    // JRead 音色配置包
    val trimmed = json.trim()
    val element = runCatching { AppConst.jsonBuilder.parseToJsonElement(trimmed) }.getOrNull()
    val format = element?.jsonObject?.get("format")?.jsonPrimitive?.content
    if (format == JReadVoiceConfigBundle.FORMAT) {
        val bundle = AppConst.jsonBuilder.decodeFromJsonElement(
            JReadVoiceConfigBundle.serializer(),
            element
        )
        return JReadVoiceConfigConverter.convert(bundle)
    }

    val normalizedJson = if (trimmed.startsWith("[")) trimmed else trimmed.toJsonListString()

    if (fromLegado) {
        /*AppConst.jsonBuilder.decodeFromString<List<LegadoHttpTts>>(json).ifEmpty { return null }
            .let { list ->
                return listOf(GroupWithSystemTts(
                    group = SystemTtsGroup(
                        id = groupId,
                        name = groupName,
                        order = groupCount
                    ),
                    list = list.map {
                        SystemTtsV2(
                            groupId = groupId,
                            id = it.id,
                            displayName = it.name,
//                            tts = HttpTTS(
//                                url = it.url,
//                                header = it.header,
//                                audioFormat = BaseAudioFormat(isNeedDecode = true)
//                            )
                        )
                    }

                ))
            }*/
        return null
    } else {
        return if (normalizedJson.contains("\"group\"")) { // 新版数据结构
            if (normalizedJson.contains("\"config\"") && normalizedJson.contains("\"source\"")) {
                AppConst.jsonBuilder.decodeFromString<List<GroupWithSystemTts>>(normalizedJson)
            } else {
                val old = AppConst.jsonBuilder.decodeFromString<List<GroupWithV1TTS>>(normalizedJson)
                old.map {
                    GroupWithSystemTts(
                        it.group,
                        it.list.map { tts -> SystemTtsMigration.v1Tov2(tts) }.filterNotNull()
                    )
                }
            }

        } else {
//            val list = AppConst.jsonBuilder.decodeFromString<List<CompatSystemTts>>(json)
            listOf(
//                com.github.jing332.database.entities.systts.GroupWithSystemTts(
//                    group = dbm.systemTtsV2.getGroup()!!,
//                    list = list.mapIndexed { index, value ->
//                        SystemTtsV2(
//                            id = System.currentTimeMillis() + index,
//                            displayName = value.displayName,
//                            tts = value.tts
//                        )
//                    }
//                )
            )
        }
    }
}
