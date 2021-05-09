package com.senierr.repository.entity

/**
 * 数据源
 *
 * @author zhouchunjie
 * @date 2021/5/9 17:15
 */
sealed class DataSource<out T> {

    /**
     * 进度
     *
     * @param totalSize 总大小
     * @param currentSize 当前大小
     */
    data class Progress(val totalSize: Long, val currentSize: Long): DataSource<Nothing>()

    /**
     * 成功
     *
     * @param value 数据
     */
    data class Success<out T>(val value: T): DataSource<T>()

    /**
     * 失败
     *
     * @param throwable 异常
     */
    data class Failure(val throwable: Throwable?): DataSource<Nothing>()
}