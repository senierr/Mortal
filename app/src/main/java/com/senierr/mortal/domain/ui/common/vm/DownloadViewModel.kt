package com.senierr.mortal.domain.ui.common.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.repository.store.disk.DiskManager
import com.senierr.mortal.repository.store.remote.RemoteManager
import com.senierr.mortal.repository.store.remote.api.CommonApi
import com.senierr.mortal.support.utils.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resumeWithException

/**
 * 下载
 *
 * @author zhouchunjie
 * @date 2020/5/25
 */
class DownloadViewModel : ViewModel() {

    val downloadResult = StatefulLiveData<File>()

    private val commonApi by lazy { RemoteManager.getGankHttp().create(CommonApi::class.java) }

    fun download(tag: String, url: String, destName: String) {
        viewModelScope.launch {
            try {
                val destFile = withContext(Dispatchers.IO) {
                    suspendCancellableCoroutine<File> { continuation ->
                        try {
                            val call = commonApi.downloadFile(url, tag)
                            continuation.invokeOnCancellation {
                                call.cancel()
                            }
                            FileUtil.saveFile(call, DiskManager.getDownloadDir(), destName)
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                }
                downloadResult.setValue(destFile)
            } catch (e: Exception) {
                downloadResult.setException(e)
            }
        }
    }
}