package com.senierr.repository.service.api

import com.senierr.repository.entity.DataSource
import com.senierr.repository.store.remote.progress.OnProgressListener
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * 公共服务
 *
 * @author zhouchunjie
 * @date 2020/6/19
 */
interface ICommonService {

    /**
     * 上传
     */
    suspend fun uploadFile(file: File, onUploadListener: OnProgressListener): Boolean

    /**
     * 下载
     */
    fun downloadFile(url: String, destName: String, md5: String): Flow<DataSource<File>>
}