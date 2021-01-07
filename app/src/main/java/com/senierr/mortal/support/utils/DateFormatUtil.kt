package com.senierr.mortal.support.utils

import android.content.Context
import com.senierr.mortal.R
import java.util.*

/**
 * 时间格式化工具
 *
 * @author zhouchunjie
 * @date 2020/5/23 10:12
 */
object DateFormatUtil {

    private const val minute = 60 * 1000L   // 1分钟
    private const val hour = 60 * minute    // 1小时
    private const val day = 24 * hour       // 1天
    private const val month = 30 * day       // 1月

    /**
     * 获取通用日期格式
     */
    fun getFormatTime(context: Context, mills: Long): String {
        val diff: Long = Date().time - Date(mills).time
        if (diff > month) {
            return context.getString(R.string.format_date_month_ago, diff / month)
        }
        if (diff > day) {
            return context.getString(R.string.format_date_day_ago, diff / day)
        }
        if (diff > hour) {
            return context.getString(R.string.format_date_hour_ago, diff / hour)
        }
        if (diff > minute) {
            return context.getString(R.string.format_date_minute_ago, diff / minute)
        }
        return context.getString(R.string.format_date_recent)
    }

    /**
     * 获取通用日期格式
     */
    fun getFormatTime(context: Context, time: String): String {
        val diff: Long = Date().time - Date(DateUtil.parse(time, "yyyy-MM-dd HH:mm:ss")).time
        if (diff > month) {
            return context.getString(R.string.format_date_month_ago, diff / month)
        }
        if (diff > day) {
            return context.getString(R.string.format_date_day_ago, diff / day)
        }
        if (diff > hour) {
            return context.getString(R.string.format_date_hour_ago, diff / hour)
        }
        if (diff > minute) {
            return context.getString(R.string.format_date_minute_ago, diff / minute)
        }
        return context.getString(R.string.format_date_recent)
    }
}