package com.senierr.mortal.domain.common.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.repository.Repository
import com.senierr.repository.remote.progress.OnProgressListener
import com.senierr.repository.remote.progress.Progress
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

    val downloadResult = StatefulLiveData<File>()

    private val commonService = Repository.getService<ICommonService>()

    fun download(url: String, destName: String) {
        viewModelScope.launch {
            try {
                val destFile = commonService.downloadFile(url, destName, "", object : OnProgressListener {
                    override fun onProgress(progress: Progress) {
                    }
                })
                downloadResult.setValue(destFile)
            } catch (e: Exception) {
                downloadResult.setException(e)
            }
        }
    }
}