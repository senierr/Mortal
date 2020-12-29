package com.senierr.repository.service.impl

import com.google.gson.Gson
import com.senierr.base.support.utils.AppUtil
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.SettingApi
import com.senierr.repository.service.api.ISettingService
import com.senierr.repository.sp.SPKey
import com.senierr.repository.sp.SPManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author zhouchunjie
 * @date 2020/5/25
 */
class SettingService : ISettingService {

    private val settingApi by lazy {
        RemoteManager.getBmobHttp().create(SettingApi::class.java)
    }
    private val spUtil by lazy { SPManager.getSP() }

    override suspend fun checkNewVersion(): VersionInfo? {
        return withContext(Dispatchers.IO) {
            val where = HashMap<String, String>()
            where["applicationId"] = Repository.getApplication().packageName
            where["platform"] = "android"
            val newVersionInfo = settingApi.checkNewVersion(Gson().toJson(where)).results.firstOrNull()
            if (newVersionInfo != null) {
                val currentVersionCode = AppUtil.getVersionCode(Repository.getApplication())
                // 判断是否是新版本
                if (newVersionInfo.versionCode > currentVersionCode) {
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
}