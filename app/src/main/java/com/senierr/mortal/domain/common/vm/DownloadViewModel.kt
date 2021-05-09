package com.senierr.mortal.domain.common.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.arch.StatefulData
import com.senierr.base.support.arch.ext.emitFailure
import com.senierr.base.support.arch.ext.emitSuccess
import com.senierr.repository.Repository
import com.senierr.repository.entity.DataSource
import com.senierr.repository.service.api.ICommonService
import kotlinx.coroutines.flow.*
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
            commonService.downloadFile(url, destName, "")
                .onEach {
                    if (it is DataSource.Success) {
                        _downloadCompleted.emitSuccess(it.value)
                    }
                }
                .catch { _downloadCompleted.emitFailure(it) }
                .collect()
        }
    }
}