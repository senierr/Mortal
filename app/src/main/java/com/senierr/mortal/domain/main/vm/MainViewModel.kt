package com.senierr.mortal.domain.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.utils.AppUtil
import com.senierr.mortal.app.SessionApplication
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
class MainViewModel(application: Application) : AndroidViewModel(application) {

    val newVersionResult = StatefulLiveData<VersionInfo>()
    val apkDownloadResult = StatefulLiveData<File>()

    private val application = getApplication<SessionApplication>()
    private val settingService = Repository.getService<ISettingService>()
    private val commonService = Repository.getService<ICommonService>()

    /**
     * 检查新版本
     */
    fun checkNewVersion() {
        viewModelScope.launch {
            try {
                val versionInfo = settingService.checkNewVersion(application.packageName)
                if (versionInfo != null && versionInfo.versionCode > AppUtil.getVersionCode(application, application.packageName)) {
                    newVersionResult.setValue(versionInfo)
                }
            } catch (e: Exception) {
                newVersionResult.setException(e)
            }
        }
    }

    /**
     * 下载APK
     */
    fun downloadApk(versionInfo: VersionInfo) {
        viewModelScope.launch {
            try {
                val apkFile = commonService.downloadFile("apk", versionInfo.url, versionInfo.fileName)
                apkDownloadResult.setValue(apkFile)
            } catch (e: Exception) {
                apkDownloadResult.setException(e)
            }
        }
    }
}