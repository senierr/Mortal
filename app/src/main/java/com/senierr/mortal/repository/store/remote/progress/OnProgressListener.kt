package com.senierr.mortal.repository.store.remote.progress

/**
 * 进度回调
 *
 * @author zhouchunjie
 * @date 2017/3/28
 */
interface OnProgressListener {

    fun onProgress(totalSize: Long, currentSize: Long, percent: Int)
}