package com.senierr.mortal.domain.common.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.arch.StatefulData
import com.senierr.base.support.arch.ext.emitFailure
import com.senierr.base.support.arch.ext.emitSuccess
import com.senierr.repository.Repository
import com.senierr.repository.service.api.ICommonService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * 下载
 *
 * @author zhouchunjie
 * @date 2020/5/25
 */
class DownloadViewModel : ViewModel() {

    private val _downloadCompleted = MutableSharedFlow<StatefulData<File>>()
    val downloadCompleted: SharedFlow<StatefulData<File>> = _downloadCompleted

    private val commonService = Repository.getService<ICommonService>()

    fun download(url: String, destName: String) {
        viewModelScope.launch {
            try {
                val destFile = commonService.downloadFile(url, destName, "")
                _downloadCompleted.emitSuccess(destFile)
            } catch (e: Exception) {
                _downloadCompleted.emitFailure(e)
            }
        }
    }
}