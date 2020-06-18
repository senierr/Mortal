package com.senierr.repository.entity.dto

/**
 * 返回结构
 */
data class HttpResponse<T>(
    val data: T,
    val errorCode: Int = HttpException.CODE_FAILURE,
    val errorMsg: String = "unknown"
) {
    /**
     * 是否成功
     */
    fun isSuccessful(): Boolean = errorCode == HttpException.CODE_SUCCESS

    /**
     * 获取请求异常
     */
    fun getException(): HttpException = HttpException(errorCode, errorMsg)
}