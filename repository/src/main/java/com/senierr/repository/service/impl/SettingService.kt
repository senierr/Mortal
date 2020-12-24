package com.senierr.repository.service.impl

import com.google.gson.Gson
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.SettingApi
import com.senierr.repository.service.api.ISettingService
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

    override suspend fun checkNewVersion(applicationId: String): VersionInfo? {
        return withContext(Dispatchers.IO) {
            val where = HashMap<String, String>()
            where["applicationId"] = applicationId
            val results = settingApi.checkNewVersion(Gson().toJson(where)).results
            return@withContext results.firstOrNull()
        }
    }
}