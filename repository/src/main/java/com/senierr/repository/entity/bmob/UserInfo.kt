package com.senierr.repository.entity.bmob

/**
 * 用户
 */
data class UserInfo(
    var objectId: String = "",
    var username: String = "",
    var password: String = "",
    var sessionToken: String = "",
    var createdAt: String = "",
    var updatedAt: String = ""
)