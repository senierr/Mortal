package com.senierr.repository.entity.bmob

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 版本信息
 */
@Parcelize
data class VersionInfo(
    var objectId: String = "",
    var platform: String = "",  // android/ios
    var applicationId: String = "",
    var versionName: String = "",
    var url: String = "",
    var fileName: String = "",
    var md5: String = "",
    var changeLog: String = "",
    var createdAt: String = "",
    var updatedAt: String = ""
) : Parcelable