package com.senierr.base.support.arch.ext

import androidx.lifecycle.LifecycleCoroutineScope
import com.senierr.base.support.arch.StatefulData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Flow扩展函数
 *
 * @author chunjiezhou
 * @date 2021/01/13
 */

/**
 * 发送成功数据
 */
suspend inline fun <reified T> MutableSharedFlow<StatefulData<T>>.emitSuccess(value: T) {
    emit(StatefulData.Success(value))
}

/**
 * 发送失败数据
 */
suspend inline fun <reified T> MutableSharedFlow<StatefulData<T>>.emitFailure(throwable: Throwable?) {
    emit(StatefulData.Failure(throwable))
}

/**
 * 成功数据时执行
 */
fun <T> Flow<StatefulData<T>>.onSuccess(
        collector: suspend (T) -> Unit
) = onEach {
    if (it is StatefulData.Success) {
        collector.invoke(it.value)
    }
}

/**
 * 失败数据时执行
 */
fun <T> Flow<StatefulData<T>>.onFailure(
        collector: suspend (Throwable?) -> Unit
) = onEach {
    if (it is StatefulData.Failure) {
        collector.invoke(it.throwable)
    }
}

/**
 * 订阅
 */
fun <T> Flow<T>.launchWhenStartedIn(scope: LifecycleCoroutineScope): Job = scope.launchWhenStarted {
    collect()
}