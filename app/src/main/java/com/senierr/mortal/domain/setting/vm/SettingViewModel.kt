package com.senierr.mortal.domain.setting.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.livedata.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.remote.progress.OnProgressListener
import com.senierr.repository.remote.progress.Progress
import com.senierr.repository.service.api.ICommonService
import com.senierr.repository.service.api.ISettingService
import kotlinx.coroutines.launch
import java.io.File

/**
 * 设置
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SettingViewModel(application: Application) : AndroidViewModel(application) {

    val cacheSize = StatefulLiveData<Long>()
    val newVersionInfo = StatefulLiveData<VersionInfo>()
    val apkDownloadProgress = StatefulLiveData<Progress>()
    val apkDownloadCompleted = StatefulLiveData<File>()

    private val settingService = Repository.getService<ISettingService>()
    private val commonService = Repository.getService<ICommonService>()

    /**
     * 获取缓存大小
     */
    fun getCacheSize() {
        viewModelScope.launch {
            try {
                cacheSize.setSuccess(settingService.getLocalCacheSize())
            } catch (e: Exception) {
                cacheSize.setError(e)
            }
        }
    }

    /**
     * 清除缓存
     */
    fun clearCache() {
        viewModelScope.launch {
            try {
                settingService.clearLocalCache()
                cacheSize.setSuccess(settingService.getLocalCacheSize())
            } catch (e: Exception) {
                cacheSize.setError(e)
            }
        }
    }

    /**
     * 检查新版本
     */
    fun checkNewVersion() {
        viewModelScope.launch {
            try {
                val versionInfo = settingService.checkNewVersion()
                newVersionInfo.setSuccess(versionInfo)
            } catch (e: Exception) {
                newVersionInfo.setError(e)
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

    /**
     * 下载APK
     */
    fun downloadApk(versionInfo: VersionInfo) {
        viewModelScope.launch {
            try {
                val apkFile = commonService.downloadFile(
                    versionInfo.url, versionInfo.fileName, versionInfo.md5,
                    object : OnProgressListener {
                        override fun onProgress(progress: Progress) {
                            launch {
                                apkDownloadProgress.setSuccess(progress)
                            }
                        }
                    }
                )
                apkDownloadCompleted.setSuccess(apkFile)
            } catch (e: Exception) {
                apkDownloadCompleted.setError(e)
            }
        }
    }
}