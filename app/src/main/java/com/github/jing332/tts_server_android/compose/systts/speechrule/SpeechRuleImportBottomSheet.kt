package com.github.jing332.tts_server_android.compose.systts.speechrule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.jing332.tts_server_android.compose.systts.ConfigImportBottomSheet
import com.github.jing332.tts_server_android.compose.systts.ConfigModel
import com.github.jing332.tts_server_android.compose.systts.SelectImportConfigDialog
import com.github.jing332.tts_server_android.constant.AppConst
import com.github.jing332.database.dbm
import com.github.jing332.database.entities.SpeechRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SpeechRuleImportBottomSheet(onDismissRequest: () -> Unit) {
    var showSelectDialog by remember { mutableStateOf<List<SpeechRule>?>(null) }
    if (showSelectDialog != null) {
        val list = showSelectDialog!!
        SelectImportConfigDialog(
            onDismissRequest = { showSelectDialog = null },
            models = list.map { ConfigModel(true, it.name, "${it.author} - v${it.version}", it) },
            onSelectedList = {
                withContext(Dispatchers.IO) {
                    it.forEach { speechRule ->
                        dbm.speechRuleDao.insert(speechRule as SpeechRule)
                    }
                }
                it.size
            }
        )
    }

    ConfigImportBottomSheet(onDismissRequest = onDismissRequest, onImport = {
        val list = withContext(Dispatchers.IO) {
            AppConst.jsonBuilder.decodeFromString<List<SpeechRule>>(it)
        }
        showSelectDialog = list
    })
}
