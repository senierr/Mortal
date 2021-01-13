package com.senierr.repository.service.api

import com.senierr.repository.remote.progress.OnProgressListener
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
    suspend fun downloadFile(
        url: String,
        destName: String,
        md5: String = "",
        tag: String? = null
    ): File
}