package com.senierr.mortal.support.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.senierr.mortal.support.proxy.InstallFragment
import java.io.File

/**
 * 应用工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
object AppUtil {

    /**
     * 判断App是否安装
     */
    fun isAppInstalled(context: Context, packageName: String = context.packageName): Boolean {
        try {
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            return packageInfo != null
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 安装APK
     */
    fun installApk(context: FragmentActivity, authority: String, apkFile: File) {
        val fragmentManager = context.supportFragmentManager
        var proxyFragment = fragmentManager.findFragmentByTag(InstallFragment.TAG) as InstallFragment?
        if (proxyFragment == null) {
            proxyFragment = InstallFragment()
            fragmentManager.beginTransaction().add(proxyFragment, InstallFragment.TAG).commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        proxyFragment.authority = authority
        proxyFragment.apkFile = apkFile
        proxyFragment.installApk()
    }

    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @param packageName 包名
     */
    fun getVersionName(context: Context, packageName: String = context.packageName): String? {
        try {
            val pi = context.packageManager.getPackageInfo(packageName, 0)
            return pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取VersionCode
     *
     * @param context 上下文
     * @param packageName 包名
     */
    @Suppress("DEPRECATION")
    fun getVersionCode(context: Context, packageName: String = context.packageName): Long {
        try {
            val pi = context.packageManager.getPackageInfo(packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return pi?.longVersionCode ?: -1L
            } else {
                return pi?.versionCode?.toLong() ?: -1L
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1L
    }

    /**
     * 判断App是否是系统应用
     *
     * @param context 上下文
     * @param packageName 包名
     */
    fun isSystemApp(context: Context, packageName: String = context.packageName): Boolean {
        try {
            val ai = context.packageManager.getApplicationInfo(packageName, 0)
            return ai != null && ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }
}