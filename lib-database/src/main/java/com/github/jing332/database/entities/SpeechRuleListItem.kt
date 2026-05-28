package com.github.jing332.database.entities

data class SpeechRuleListItem(
    val id: Long,
    val isEnabled: Boolean,
    val name: String,
    val version: Int,
    val ruleId: String,
    val author: String,
    val order: Int
)
