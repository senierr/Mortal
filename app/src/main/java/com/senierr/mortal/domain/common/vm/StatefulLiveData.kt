package com.senierr.mortal.domain.common.vm

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.senierr.base.support.utils.LogUtil

/**
 * 带状态的LiveData
 *
 * @author zhouchunjie
 * @date 2020/6/5
 */
class StatefulLiveData<T> {

    data class StatefulData<T>(
        val state: Int = 0,
        val data: T? = null,
        val exception: Exception? = null
    )

    private val liveData = MutableLiveData<StatefulData<T>>()

    @MainThread
    fun setState(value: Int) {
        liveData.value = StatefulData(state = value)
    }

    fun postState(value: Int) {
        liveData.postValue(StatefulData(state = value))
    }

    @MainThread
    fun setValue(value: T) {
        liveData.value = StatefulData(data = value)
    }

    fun postValue(value: T) {
        liveData.postValue(StatefulData(data = value))
    }

    @MainThread
    fun setException(value: Exception) {
        liveData.value = StatefulData(exception = value)
    }

    fun postException(value: Exception) {
        liveData.postValue(StatefulData(exception = value))
    }

    @MainThread
    fun observe(
        owner: LifecycleOwner,
        onSuccess: (T) -> Unit,
        onError: (Exception) -> Unit
    ) {
        liveData.observe(owner, Observer {
            if (it.exception != null) {
                onError.invoke(it.exception)
            } else if (it.data != null) {
                onSuccess.invoke(it.data)
            } else {
                LogUtil.logE("StatefulLiveData's data must not be null.")
            }
        })
    }

    @MainThread
    fun observe(
        owner: LifecycleOwner,
        onSuccess: (T) -> Unit
    ) {
        observe(owner, onSuccess, {})
    }
}