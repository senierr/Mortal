package com.senierr.mortal.support.utils

import android.text.TextUtils

import java.util.regex.Pattern

/**
 * 字符串工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
object StringUtil {

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    fun upperFirstLetter(s: String): String {
        return if (TextUtils.isEmpty(s) || !Character.isLowerCase(s[0])) s else (s[0].toInt() - 32).toChar() + s.substring(1)
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    fun lowerFirstLetter(s: String): String {
        return if (TextUtils.isEmpty(s) || !Character.isUpperCase(s[0])) s else (s[0].toInt() + 32).toChar() + s.substring(1)
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    fun reverse(s: String): String? {
        val len = s.length
        if (len <= 1) return s
        val mid = len shr 1
        val chars = s.toCharArray()
        var c: Char
        for (i in 0 until mid) {
            c = chars[i]
            chars[i] = chars[len - i - 1]
            chars[len - i - 1] = c
        }
        return String(chars)
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    fun toDBC(s: String): String {
        if (TextUtils.isEmpty(s)) return s
        val chars = s.toCharArray()
        var i = 0
        val len = chars.size
        while (i < len) {
            if (chars[i].toInt() == 12288) {
                chars[i] = ' '
            } else if (chars[i].toInt() in 65281..65374) {
                chars[i] = (chars[i].toInt() - 65248).toChar()
            } else {
                chars[i] = chars[i]
            }
            i++
        }
        return String(chars)
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    fun toSBC(s: String): String {
        if (TextUtils.isEmpty(s)) return s
        val chars = s.toCharArray()
        var i = 0
        val len = chars.size
        while (i < len) {
            if (chars[i] == ' ') {
                chars[i] = 12288.toChar()
            } else if (chars[i].toInt() in 33..126) {
                chars[i] = (chars[i].toInt() + 65248).toChar()
            } else {
                chars[i] = chars[i]
            }
            i++
        }
        return String(chars)
    }

    /**
     * 判断是否是正负整数
     *
     * @param str 待检测字符串
     * @return `true`: 是<br></br> `false`: 不是
     */
    fun isInteger(str: String): Boolean {
        val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
        return pattern.matcher(str).matches()
    }
}
