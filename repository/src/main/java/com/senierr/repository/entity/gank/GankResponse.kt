package com.senierr.repository.entity.gank

/**
 * 返回结构
 */
data class GankResponse<T>(
    val status: Int = GankException.CODE_FAILURE,
    val data: T
) {
    /**
     * 是否成功
     */
    fun isSuccessful(): Boolean = status == GankException.CODE_SUCCESS

    /**
     * 获取请求异常
     */
    fun getException(): GankException = GankException(status)
}