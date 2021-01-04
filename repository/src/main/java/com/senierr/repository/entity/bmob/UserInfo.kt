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
    var nickname: String = "",
    var avatar: String = "",
    var email: String = "",
    var logged: Boolean = false, // 是否已登录
    var createdAt: String = "",
    var updatedAt: String = ""
)