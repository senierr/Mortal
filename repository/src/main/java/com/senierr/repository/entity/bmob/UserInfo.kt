package com.senierr.repository.entity.bmob

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户
 */
@Entity(tableName = "UserInfo")
data class UserInfo(
    @PrimaryKey
    var objectId: String = "",
    var username: String = "",
    var password: String = "",
    var sessionToken: String = "",
    var createdAt: String = "",
    var updatedAt: String = ""
)