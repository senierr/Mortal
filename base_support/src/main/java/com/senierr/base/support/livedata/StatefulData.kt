package com.senierr.base.support.livedata

/**
 * 带异常状态的数据
 *
 * @author zhouchunjie
 * @date 2021/1/9 20:12
 */
data class StatefulData<T>(
    val isSuccess: Boolean = true,
    val data: T? = null,
    val throwable: Throwable? = null
) {
    companion object {
        fun <T> success(data: T?): StatefulData<T> {
            return StatefulData(true, data, null)
        }

        fun <T> error(throwable: Throwable?): StatefulData<T> {
            return StatefulData(false, null, throwable)
        }
    }
}