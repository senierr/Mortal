package com.senierr.mortal.repository.remote.progress

/**
 * 进度实体
 *
 * @author zhouchunjie
 * @date 2020/6/19
 */
data class Progress(
    val tag: String,        // 标签
    val totalSize: Long,    // 文件总大小
    val currentSize: Long,  // 当前下载大小
    val percent: Int        // 当前进度
)