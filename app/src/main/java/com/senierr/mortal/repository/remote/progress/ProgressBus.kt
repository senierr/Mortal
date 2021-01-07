package com.senierr.mortal.repository.remote.progress

import androidx.lifecycle.MutableLiveData

/**
 *
 * @author zhouchunjie
 * @date 2020/6/19
 */
object ProgressBus {
    val uploadProgress = MutableLiveData<Progress>()
    val downloadProgress = MutableLiveData<Progress>()
}