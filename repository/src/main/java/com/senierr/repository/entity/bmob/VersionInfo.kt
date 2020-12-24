package com.senierr.repository.entity.bmob

/**
 * 版本信息
 */
data class VersionInfo(
    var objectId: String = "",
    var applicationId: String = "",
    var versionCode: Int = 0,
    var versionName: String = "",
    var url: String = "",
    var fileName: String = "",
    var changeLog: String = "",
    var createdAt: String = "",
    var updatedAt: String = ""
)