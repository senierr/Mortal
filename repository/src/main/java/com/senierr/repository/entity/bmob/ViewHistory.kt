package com.senierr.repository.entity.bmob

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 浏览记录
 */
@Parcelize
data class ViewHistory(
    var objectId: String = "",
    var articleId: String = "",
    var articleTitle: String = "",
    var articleUrl: String = "",
    var userId: String = "",
    var createdAt: String = "",
    var updatedAt: String = ""
) : Parcelable