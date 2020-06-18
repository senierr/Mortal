package com.senierr.base.support.utils

import java.util.*
import java.util.regex.Pattern

/**
 * 正则工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
object RegexUtil {

    /**
     * 正则：手机号（简单）
     */
    const val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"
    /**
     * 正则：手机号（精确）
     *
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     *
     * 联通：130、131、132、145、155、156、175、176、185、186
     *
     * 电信：133、153、173、177、180、181、189
     *
     * 全球星：1349
     *
     * 虚拟运营商：170
     */
    const val REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$"
    /**
     * 正则：电话号码
     */
    const val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"
    /**
     * 正则：身份证号码15位
     */
    const val REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"
    /**
     * 正则：身份证号码18位
     */
    const val REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"
    /**
     * 正则：邮箱
     */
    const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
    /**
     * 正则：URL
     */
    const val REGEX_URL = "[a-zA-z]+://[^\\s]*"
    /**
     * 正则：汉字
     */
    const val REGEX_ZH = "^[\\u4e00-\\u9fa5]+$"
    /**
     * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    const val REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$"
    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    const val REGEX_DATE =
        "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"
    /**
     * 正则：IP地址
     */
    const val REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"

    /**
     * 验证手机号（简单）
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isMobileSimple(input: CharSequence): Boolean {
        return isMatch(REGEX_MOBILE_SIMPLE, input)
    }

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isMobileExact(input: CharSequence): Boolean {
        return isMatch(REGEX_MOBILE_EXACT, input)
    }

    /**
     * 验证电话号码
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isTel(input: CharSequence): Boolean {
        return isMatch(REGEX_TEL, input)
    }

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isIDCard15(input: CharSequence): Boolean {
        return isMatch(REGEX_ID_CARD15, input)
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isIDCard18(input: CharSequence): Boolean {
        return isMatch(REGEX_ID_CARD18, input)
    }

    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isEmail(input: CharSequence): Boolean {
        return isMatch(REGEX_EMAIL, input)
    }

    /**
     * 验证URL
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isURL(input: CharSequence): Boolean {
        return isMatch(REGEX_URL, input)
    }

    /**
     * 验证汉字
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isZh(input: CharSequence): Boolean {
        return isMatch(REGEX_ZH, input)
    }

    /**
     * 验证用户名
     *
     * 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isUsername(input: CharSequence): Boolean {
        return isMatch(REGEX_USERNAME, input)
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isDate(input: CharSequence): Boolean {
        return isMatch(REGEX_DATE, input)
    }

    /**
     * 验证IP地址
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isIP(input: CharSequence): Boolean {
        return isMatch(REGEX_IP, input)
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isMatch(regex: String, input: CharSequence): Boolean {
        return input.isNotEmpty() && Pattern.matches(regex, input)
    }

    /**
     * 获取正则匹配的部分
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return 正则匹配的部分
     */
    fun getMatches(regex: String, input: CharSequence): List<String>? {
        val matches = ArrayList<String>()
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        while (matcher.find()) {
            matches.add(matcher.group())
        }
        return matches
    }

    /**
     * 替换正则匹配的第一部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换正则匹配的第一部分
     */
    fun getReplaceFirst(input: String?, regex: String, replacement: String): String? {
        return if (input == null) null else Pattern.compile(regex).matcher(input).replaceFirst(replacement)
    }

    /**
     * 替换所有正则匹配的部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换所有正则匹配的部分
     */
    fun getReplaceAll(input: String?, regex: String, replacement: String): String? {
        return if (input == null) null else Pattern.compile(regex).matcher(input).replaceAll(replacement)
    }
}