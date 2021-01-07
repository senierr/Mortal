package com.senierr.mortal.repository.store.sp

import android.content.Context
import com.senierr.mortal.support.utils.SPUtil

/**
 * SharedPreferences管理器
 *
 * @author zhouchunjie
 * @date 2019/11/28
 */
object SPManager {

    private const val SP_NAME = "repository_sp"

    private lateinit var spUtil: SPUtil

    /**
     * 初始化
     */
    fun initialize(context: Context) {
        spUtil = SPUtil.getInstance(context, SP_NAME)
    }

    /**
     * 获取SharedPreferences
     */
    fun getSP(): SPUtil {
        return spUtil
    }
}