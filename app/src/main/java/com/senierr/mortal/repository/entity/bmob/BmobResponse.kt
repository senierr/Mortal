package com.senierr.mortal.repository.entity.bmob

/**
 * 返回结构
 */
data class BmobResponse(
    val msg: String = "ok"
) {
    fun isSuccessful(): Boolean = msg == "ok"
}