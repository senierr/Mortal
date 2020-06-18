package com.senierr.repository.entity.dto

/**
 * 用户
 */
data class UserInfo(
    val id: Long = 0,
    val username: String = "",
    val nickname: String = "",
    val publicName: String = "",
    val email: String = "",
    val icon: String = ""
)