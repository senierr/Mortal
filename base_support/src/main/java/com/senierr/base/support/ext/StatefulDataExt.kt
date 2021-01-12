package com.senierr.base.support.ext

import com.senierr.base.support.livedata.StatefulData

/**
 * StatefulData扩展函数
 *
 * @author chunjiezhou
 * @date 2021/01/12
 */

inline fun <reified T> StatefulData<T>.doSuccess(success: (T?) -> Unit) {
    if (this is StatefulData.Success) {
        success(value)
    }
}

inline fun <reified T> StatefulData<T>.doFailure(failure: (Throwable?) -> Unit) {
    if (this is StatefulData.Failure) {
        failure(throwable)
    }
}