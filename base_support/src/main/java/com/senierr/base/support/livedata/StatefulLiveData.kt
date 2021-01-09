package com.senierr.base.support.livedata

import androidx.lifecycle.MutableLiveData

/**
 *
 * @author zhouchunjie
 * @date 2021/1/9 19:40
 */
class StatefulLiveData<T> : MutableLiveData<StatefulData<T>>() {

    fun setSuccess(value: T?) {
        setValue(StatefulData.success(value))
    }

    fun setError(throwable: Throwable?) {
        value = StatefulData.error(throwable)
    }
}