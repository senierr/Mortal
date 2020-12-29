package com.senierr.repository.service.api

import java.io.File

/**
 * 公共服务
 *
 * @author zhouchunjie
 * @date 2020/6/19
 */
interface ICommonService {

    /**
     * 下载
     */
    suspend fun downloadFile(tag: String, url: String, destName: String, md5: String = ""): File
}