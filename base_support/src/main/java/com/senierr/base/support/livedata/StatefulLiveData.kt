package com.senierr.base.support.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 带状态的LiveData
 *
 * @author zhouchunjie
 * @date 2021/1/9 19:40
 */
class StatefulLiveData<T> : MutableLiveData<StatefulData<T>>() {

    fun setSuccess(value: T?) {
        setValue(StatefulData.Success(value))
    }

    fun setFailure(throwable: Throwable?) {
        value = StatefulData.Failure(throwable)
    }
}