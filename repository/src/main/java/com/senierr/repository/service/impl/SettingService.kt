package com.senierr.repository.service.impl

import com.google.gson.Gson
import com.senierr.base.support.utils.AppUtil
import com.senierr.base.support.utils.FileUtil
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.Feedback
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.store.remote.RemoteManager
import com.senierr.repository.store.remote.api.SettingApi
import com.senierr.repository.service.api.ISettingService
import com.senierr.repository.store.sp.SPKey
import com.senierr.repository.store.sp.SPManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author zhouchunjie
 * @date 2020/5/25
 */
class SettingService : ISettingService {

    private val settingApi by lazy { RemoteManager.getBmobHttp().create(SettingApi::class.java) }
    private val spUtil by lazy { SPManager.getSP() }

    override suspend fun checkNewVersion(): VersionInfo? {
        return withContext(Dispatchers.IO) {
            val where = HashMap<String, String>()
            where["applicationId"] = Repository.getApplication().packageName
            where["platform"] = "android"
            val newVersionInfo = settingApi.checkNewVersion(Gson().toJson(where)).results.firstOrNull()
            if (newVersionInfo != null) {
                // 判断是否是新版本
                if (checkIfNewVersion(newVersionInfo.versionName)) {
                    // 判断是否是忽略版本
                    val ignoreVersionName = spUtil.getString(SPKey.IGNORE_UPDATE_VERSION_NAME)
                    if (newVersionInfo.versionName != ignoreVersionName) {
                        return@withContext newVersionInfo
                    }
                }
            }
            return@withContext null
        }
    }

    override suspend fun ignoreUpdateVersion(versionName: String): Boolean {
        return withContext(Dispatchers.IO) {
            spUtil.putString(SPKey.IGNORE_UPDATE_VERSION_NAME, versionName)
            return@withContext true
        }
    }

    override suspend fun getLocalCacheSize(): Long {
        return withContext(Dispatchers.IO) {
            val cacheDirSize = FileUtil.getFileSize(Repository.getApplication().cacheDir)
            val externalCacheDirSize = FileUtil.getFileSize(Repository.getApplication().externalCacheDir)
            return@withContext cacheDirSize + externalCacheDirSize
        }
    }

    override suspend fun clearLocalCache() {
        return withContext(Dispatchers.IO) {
            FileUtil.deleteFile(Repository.getApplication().cacheDir)
            FileUtil.deleteFile(Repository.getApplication().externalCacheDir)
            return@withContext
        }
    }

    override suspend fun feedback(content: String, userId: String): Feedback {
        return withContext(Dispatchers.IO) {

            val response = settingApi.feedback(mutableMapOf(
                Pair("content", content),
                Pair("userId", userId)
            ))
            response.content = content
            response.userId = userId
            return@withContext response
        }
    }

    /**
     * 检测是否需要升级
     */
    private fun checkIfNewVersion(targetVersionName: String): Boolean {
        val currentVersionName = AppUtil.getVersionName(Repository.getApplication()) ?: return false
        val currentVersionArray = currentVersionName.split(".").toTypedArray()
        val targetVersionArray = targetVersionName.split(".").toTypedArray()
        val minLength = currentVersionArray.size.coerceAtMost(targetVersionArray.size)
        for (i in 0 until minLength) {
            val diff = targetVersionArray[i].toInt() - currentVersionArray[i].toInt()
            if (diff != 0) return diff > 0
        }
        return false
    }
}