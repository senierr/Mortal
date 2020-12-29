package com.senierr.mortal.domain.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.service.api.ICommonService
import com.senierr.repository.service.api.ISettingService
import kotlinx.coroutines.launch
import java.io.File

/**
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class MainViewModel : ViewModel() {

    val newVersionResult = StatefulLiveData<VersionInfo>()
    val apkDownloadResult = StatefulLiveData<File>()

    private val settingService = Repository.getService<ISettingService>()
    private val commonService = Repository.getService<ICommonService>()

    /**
     * 检查新版本
     */
    fun checkNewVersion() {
        viewModelScope.launch {
            try {
                val versionInfo = settingService.checkNewVersion()
                versionInfo?.let {
                    newVersionResult.setValue(it)
                }
            } catch (e: Exception) {
                newVersionResult.setException(e)
            }
        }
    }

    /**
     * 下载APK
     */
    fun downloadApk(versionInfo: VersionInfo, downloadTag: String) {
        viewModelScope.launch {
            try {
                val apkFile = commonService.downloadFile(
                    downloadTag,
                    versionInfo.url,
                    versionInfo.fileName,
                    versionInfo.md5
                )
                apkDownloadResult.setValue(apkFile)
            } catch (e: Exception) {
                apkDownloadResult.setException(e)
            }
        }
    }

    /**
     * 忽略此版本
     */
    fun ignoreThisVersion(versionInfo: VersionInfo) {
        viewModelScope.launch {
            try {
                settingService.ignoreUpdateVersion(versionInfo.versionName)
            } catch (e: Exception) {
                // ignore
            }
        }
    }
}