package com.senierr.base.support.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle

/**
 *
 * @author zhouchunjie
 * @date 2019/6/24
 */
object MetaDataUtil {

    /**
     * 从Application中获取meta-data中String值
     *
     * @param context 上下文
     * @param key meta-data的name
     * @param defValue 默认值：""
     * @return meta-data的value
     */
    fun getStringFromApplication(context: Context, key: String, defValue: String = ""): String {
        val bundle = getBundleFromApplication(context.packageManager, context.packageName)
        return if (bundle != null && bundle.containsKey(key)) bundle.getString(key) ?: defValue else defValue
    }

    /**
     * 获取Application中的meta-data.
     *
     * @param packageManager 应用管理
     * @param packageName 包名
     * @return Bundle
     */
    fun getBundleFromApplication(packageManager: PackageManager, packageName: String): Bundle? {
        var bundle: Bundle? = null
        try {
            val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            bundle = ai.metaData
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return bundle
    }

    /**
     * 根据key从Activity中返回的Bundle中获取value
     *
     * @param context 上下文
     * @param component 组件
     * @param key meta-data的name
     * @param defValue 默认值
     * @return meta-data的value
     */
    fun getStringFromActivity(context: Context, component: ComponentName, key: String, defValue: String = ""): String? {
        val bundle = getBundleFromActivity(context.packageManager, component)
        return if (bundle != null && bundle.containsKey(key)) bundle.getString(key) ?: defValue else defValue
    }

    /**
     * 获取Activity中的meta-data.
     *
     * @param packageManager 应用管理
     * @param component 组件
     * @return Bundle
     */
    fun getBundleFromActivity(packageManager: PackageManager, component: ComponentName): Bundle? {
        var bundle: Bundle? = null
        try {
            val ai = packageManager.getActivityInfo(component, PackageManager.GET_META_DATA)
            bundle = ai.metaData
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return bundle
    }
}