package com.github.jing332.database.entities.plugin

/**
 * 轻量查询结果：仅包含 id 和 version，用于 insertOrUpdate 存在性检查
 * 避免加载大 code 字段导致 SQLiteBlobTooBigException
 */
data class PluginIdVersion(
    val id: Long,
    val version: Int
)
