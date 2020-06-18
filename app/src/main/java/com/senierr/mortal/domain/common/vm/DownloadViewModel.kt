package com.senierr.mortal.domain.common.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.repository.Repository
import kotlinx.coroutines.launch
import java.io.File

/**
 * 下载
 *
 * @author zhouchunjie
 * @date 2020/5/25
 */
class DownloadViewModel : ViewModel() {

    data class Progress(
        val totalSize: Long,    // 文件总大小
        val currentSize: Long,  // 当前下载大小
        val percent: Int        // 当前进度
    )

    val downloadProgress = MutableLiveData<Progress>()
    val downloadSuccess = MutableLiveData<File>()
    val downloadFailure = MutableLiveData<Exception>()

//    private val commonService = Repository.getService<ICommonService>()

    fun download(url: String, destName: String) {
//        viewModelScope.launch {
//            try {
//                val destFile = commonService.downloadFile(url, destName) { totalSize, currentSize, percent ->
//                    // 此回调线程为异步线程，注意转为主线程
//                    downloadProgress.postValue(Progress(totalSize, currentSize, percent))
//                }
//                downloadSuccess.value = destFile
//            } catch (e: Exception) {
//                downloadFailure.value = e
//            }
//        }
    }
}