package com.senierr.repository.entity.dto

/**
 * 异常返回
 */
data class HttpException(
    val errorCode: Int = CODE_FAILURE,
    val errorMsg: String = "unknown"
) : Exception(errorMsg) {
    companion object {
        const val CODE_SUCCESS = 0          // 成功
        const val CODE_FAILURE = -1         // 失败
        const val CODE_UN_LOGGED = -1001    // 未登录
    }
}