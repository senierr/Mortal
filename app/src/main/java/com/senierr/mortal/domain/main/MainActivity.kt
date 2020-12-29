package com.senierr.mortal.domain.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.AppUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityMainBinding
import com.senierr.mortal.domain.home.HomeFragment
import com.senierr.mortal.domain.main.vm.MainViewModel
import com.senierr.mortal.domain.recommend.RecommendFragment
import com.senierr.mortal.domain.user.MeFragment
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.notification.NotificationManager
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.VersionInfo

/**
 * 主页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        private const val TAG_DOWNLOAD = "MainActivity_apk_download"

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val mainViewModel by getViewModel<MainViewModel>()

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()

        mainViewModel.checkNewVersion()
    }

    private fun initView() {
        binding.vpMain.adapter = MainPageAdapter(this)
        binding.vpMain.offscreenPageLimit = 2
        binding.vpMain.isUserInputEnabled = false
        binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bnvBottom.selectedItemId = R.id.tab_home
                    1 -> binding.bnvBottom.selectedItemId = R.id.tab_recommend
                    2 -> binding.bnvBottom.selectedItemId = R.id.tab_me
                    else -> binding.bnvBottom.selectedItemId = R.id.tab_home
                }
            }
        })

        binding.bnvBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab_home -> binding.vpMain.currentItem = 0
                R.id.tab_recommend -> binding.vpMain.currentItem = 1
                R.id.tab_me -> binding.vpMain.currentItem = 2
            }
            return@setOnNavigationItemSelectedListener true
        }

        // TODO 测试徽章功能
        val badgeDrawable = binding.bnvBottom.getOrCreateBadge(R.id.tab_me)
        badgeDrawable?.badgeTextColor = ContextCompat.getColor(this, R.color.text_white)
        badgeDrawable?.backgroundColor = ContextCompat.getColor(this, R.color.app_warn)
        badgeDrawable?.maxCharacterCount = 2
        badgeDrawable?.number = 999
    }

    private fun initViewModel() {
        mainViewModel.newVersionResult.observe(this) {
            showNewVersionDialog(it)
        }
        mainViewModel.apkDownloadResult.observe(this) {
            // 移除下载通知
            NotificationManager.cancel(this, NotificationManager.NOTIFY_ID_UPDATE)
            // 安装APK
            AppUtil.installApk(this, "${packageName}.provider", it)
        }
        // 监听下载进度
        Repository.getProgressBus().downloadProgress.observe(this, Observer {
            if (it.tag == TAG_DOWNLOAD) {
                NotificationManager.sendUpdateNotification(this, it.percent)
            }
        })
    }

    /**
     * 显示新版本提示
     */
    private fun showNewVersionDialog(versionInfo: VersionInfo) {
        val newVersionDialog = AlertDialog.Builder(this)
                .setTitle(R.string.discover_new_version)
                .setMessage(versionInfo.changeLog.replace("\\n", "\n")) // 传输时\n被转义成\\n了
                .setPositiveButton(R.string.upgrade_now) { dialog, _ ->
                    mainViewModel.downloadApk(versionInfo, TAG_DOWNLOAD)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.upgrade_later) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNeutralButton(R.string.ignore_this_version) { dialog, _ ->
                    mainViewModel.ignoreThisVersion(versionInfo)
                    dialog.dismiss()
                }
                .create()
        newVersionDialog.show()
        newVersionDialog.setCanceledOnTouchOutside(false)
        newVersionDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.text_hint))
        newVersionDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                .setTextColor(ContextCompat.getColor(this, R.color.text_warn))
    }

    private inner class MainPageAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> HomeFragment()
            1 -> RecommendFragment()
            2 -> MeFragment()
            else -> HomeFragment()
        }
    }
}