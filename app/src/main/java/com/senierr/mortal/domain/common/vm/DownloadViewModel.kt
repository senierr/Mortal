package com.senierr.mortal.domain.common.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.repository.Repository
import com.senierr.repository.service.api.ICommonService
import kotlinx.coroutines.launch
import java.io.File


/**
 * 下载
 *
 * @author zhouchunjie
 * @date 2020/5/25
 */
class DownloadViewModel : ViewModel() {

    val downloadProgress = Repository.getProgressBus().downloadProgress
    val downloadResult = StatefulLiveData<File>()

    private val commonService = Repository.getService<ICommonService>()

    fun download(url: String, destName: String) {
        viewModelScope.launch {
            try {
                val destFile = commonService.downloadFile(url, url, destName)
                downloadResult.setValue(destFile)
            } catch (e: Exception) {
                downloadResult.setException(e)
            }
        }
    }
}