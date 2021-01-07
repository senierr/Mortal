package com.senierr.mortal.domain.ui.common.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.repository.Repository
import com.senierr.mortal.repository.service.api.ICommonService
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

    fun download(tag: String, url: String, destName: String) {
        viewModelScope.launch {
            try {
                val destFile = commonService.downloadFile(tag, url, destName)
                downloadResult.setValue(destFile)
            } catch (e: Exception) {
                downloadResult.setException(e)
            }
        }
    }
}