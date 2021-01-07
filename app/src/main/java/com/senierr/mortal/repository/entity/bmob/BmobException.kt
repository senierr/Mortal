package com.senierr.mortal.repository.entity.bmob

import java.io.IOException

/**
 * 异常返回
 */
data class BmobException(
    val code: Int = CODE_UNKNOWN,
    val error: String = "unknown"
) : IOException(error) {
    companion object {
        const val CODE_UNKNOWN = -1
    }
}