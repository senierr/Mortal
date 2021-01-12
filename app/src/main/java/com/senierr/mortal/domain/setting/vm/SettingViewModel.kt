package com.senierr.mortal.domain.setting.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.livedata.StatefulLiveData
import com.senierr.mortal.notification.NotificationManager
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.Feedback
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
    val apkDownloadCompleted = StatefulLiveData<File>()
    val feedbackResult = StatefulLiveData<Feedback>()

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
                cacheSize.setFailure(e)
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
                cacheSize.setFailure(e)
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
                newVersionInfo.setFailure(e)
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
                            // 发送进度通知
                            NotificationManager.sendUpdateNotification(getApplication(), progress.percent)
                        }
                    }
                )
                // 移除下载通知
                NotificationManager.cancel(getApplication(), NotificationManager.NOTIFY_ID_UPDATE)
                apkDownloadCompleted.setSuccess(apkFile)
            } catch (e: Exception) {
                apkDownloadCompleted.setFailure(e)
            }
        }
    }

    /**
     * 意见反馈
     */
    fun feedback(content: String, userId: String) {
        viewModelScope.launch {
            try {
                val feedback = settingService.feedback(content, userId)
                feedbackResult.setSuccess(feedback)
            } catch (e: Exception) {
                feedbackResult.setFailure(e)
            }
        }
    }
}