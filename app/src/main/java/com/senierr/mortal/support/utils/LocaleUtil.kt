package com.senierr.mortal.support.utils

import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 * 多语言工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
object LocaleUtil {

    /**
     * 获取真实系统首选语言
     *
     * @return 语言
     */
    fun locale(): Locale =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().get(0)
        } else {
            Locale.getDefault()
        }

    /**
     * 判断是否是中文
     *
     * @return `true`: 中文<br></br>`false`: 非中文
     */
    fun isZh(): Boolean {
        val locale = locale()
        val language = locale.language.trim()
        return language.equals("zh", ignoreCase = true)
    }

    /**
     * 判断是否是中国大陆
     *
     * @return `true`: 中国大陆<br></br>`false`: 非中国大陆
     */
    fun isZhCN(): Boolean {
        val locale = locale()
        val language = locale.language.trim()
        val country = locale.country.toLowerCase().trim()
        return language.equals("zh", ignoreCase = true) && country.equals("cn", ignoreCase = true)
    }
}
