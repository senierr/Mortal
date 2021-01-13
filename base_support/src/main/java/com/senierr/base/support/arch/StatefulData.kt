package com.senierr.base.support.arch

/**
 * 带状态的数据
 *
 * @author zhouchunjie
 * @date 2021/1/9 20:12
 */
sealed class StatefulData<out T> {

    /**
     * 加载成功
     *
     * @param value 返回数据
     */
    data class Success<out T>(val value: T): StatefulData<T>()

    /**
     * 加载失败
     *
     * @param throwable 异常
     */
    data class Failure(val throwable: Throwable?): StatefulData<Nothing>()
}