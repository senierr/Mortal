package com.senierr.repository.entity.gank

import java.io.IOException

/**
 * 异常返回
 */
data class GankException(
    val status: Int = CODE_FAILURE
) : IOException() {
    companion object {
        const val CODE_SUCCESS = 100    // 成功
        const val CODE_FAILURE = -1     // 失败
    }
}