package com.senierr.repository.entity.bmob

/**
 * 版本信息
 */
data class VersionInfo(
    val changeLog: String = "",
    val fileName: String = "",
    val url: String = "",
    val versionCode: Int = 0,
    val versionName: String = ""
)