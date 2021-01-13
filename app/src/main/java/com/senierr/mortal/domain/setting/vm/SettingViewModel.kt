package com.senierr.mortal.domain.setting.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.arch.StatefulData
import com.senierr.base.support.arch.ext.emitFailure
import com.senierr.base.support.arch.ext.emitSuccess
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.notification.NotificationManager
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.Feedback
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.service.api.ICommonService
import com.senierr.repository.service.api.ISettingService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * 设置
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val _cacheSize = MutableSharedFlow<StatefulData<Long>>()
    val cacheSize: SharedFlow<StatefulData<Long>> = _cacheSize

    private val _newVersionInfo = MutableSharedFlow<StatefulData<VersionInfo>>()
    val newVersionInfo: SharedFlow<StatefulData<VersionInfo>> = _newVersionInfo

    private val _noNewVersionInfo = MutableSharedFlow<StatefulData<Unit>>()
    val noNewVersionInfo: SharedFlow<StatefulData<Unit>> = _noNewVersionInfo

    private val _apkDownloadCompleted = MutableSharedFlow<StatefulData<File>>()
    val apkDownloadCompleted: SharedFlow<StatefulData<File>> = _apkDownloadCompleted

    private val _feedbackResult = MutableSharedFlow<StatefulData<Feedback>>()
    val feedbackResult: SharedFlow<StatefulData<Feedback>> = _feedbackResult

    private val settingService = Repository.getService<ISettingService>()
    private val commonService = Repository.getService<ICommonService>()

    /**
     * 获取缓存大小
     */
    fun getCacheSize() {
        viewModelScope.launch {
            try {
                _cacheSize.emitSuccess(settingService.getLocalCacheSize())
            } catch (e: Exception) {
                _cacheSize.emitFailure(e)
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
                _cacheSize.emitSuccess(settingService.getLocalCacheSize())
            } catch (e: Exception) {
                _cacheSize.emitFailure(e)
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
                if (versionInfo == null) {
                    _noNewVersionInfo.emitSuccess(Unit)
                } else {
                    _newVersionInfo.emitSuccess(versionInfo)
                }
            } catch (e: Exception) {
                LogUtil.logE("_newVersionInfo: ${Log.getStackTraceString(e)}")
                _newVersionInfo.emitFailure(e)
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
                        versionInfo.url, versionInfo.fileName, versionInfo.md5, "downloadApk"
                )
                // 移除下载通知
                NotificationManager.cancel(getApplication(), NotificationManager.NOTIFY_ID_UPDATE)
                _apkDownloadCompleted.emitSuccess(apkFile)
            } catch (e: Exception) {
                _apkDownloadCompleted.emitFailure(e)
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
                _feedbackResult.emitSuccess(feedback)
            } catch (e: Exception) {
                _feedbackResult.emitFailure(e)
            }
        }
    }
}