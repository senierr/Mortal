package com.senierr.mortal.domain.main

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.arch.ext.androidViewModel
import com.senierr.base.support.arch.ext.doOnFailure
import com.senierr.base.support.arch.ext.doOnSuccess
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.AppUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityMainBinding
import com.senierr.mortal.domain.home.HomeFragment
import com.senierr.mortal.domain.recommend.RecommendFragment
import com.senierr.mortal.domain.setting.vm.SettingViewModel
import com.senierr.mortal.domain.user.MeFragment
import com.senierr.mortal.ext.showToast
import com.senierr.mortal.domain.notification.NotificationManager
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.store.remote.progress.Progress
import com.senierr.repository.store.remote.progress.ProgressReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn

/**
 * 主页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val settingViewModel by androidViewModel<SettingViewModel>()

    companion object {
        private const val TAG_DOWNLOAD = "tag_download_apk_main"

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    // 下载进度监听
    private val downloadApkReceiver = object : ProgressReceiver() {
        override fun onProgress(context: Context, tag: String, progress: Progress) {
            if (tag == TAG_DOWNLOAD) {
                NotificationManager.sendDownloadNotification(context, progress.percent)
            }
        }
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        registerReceiver(downloadApkReceiver, IntentFilter(ProgressReceiver.ACTION_REMOTE_PROGRESS_RECEIVER))
        settingViewModel.checkNewVersion()
    }

    override fun onDestroy() {
        unregisterReceiver(downloadApkReceiver)
        super.onDestroy()
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
                R.id.tab_home -> binding.vpMain.setCurrentItem(0, false)
                R.id.tab_recommend -> binding.vpMain.setCurrentItem(1, false)
                R.id.tab_me -> binding.vpMain.setCurrentItem(2, false)
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
        lifecycleScope.launchWhenStarted {
            settingViewModel.newVersionInfo
                .doOnSuccess {
                    showNewVersionDialog(it)
                }
                .doOnFailure {
                    showToast(R.string.network_error)
                }
                .launchIn(this)
        }

        lifecycleScope.launchWhenStarted {
            settingViewModel.apkDownloadCompleted
                .doOnSuccess {
                    // 延迟，防止通知后发
                    delay(100)
                    // 移除下载通知
                    NotificationManager.cancel(this@MainActivity, NotificationManager.NOTIFY_ID_UPDATE)
                    // 安装APK
                    AppUtil.installApk(this@MainActivity, "${this@MainActivity.packageName}.provider", it)
                }
                .collect()
        }
    }

    /**
     * 显示新版本提示
     */
    private fun showNewVersionDialog(versionInfo: VersionInfo) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.discover_new_version)
            .setMessage(versionInfo.changeLog.replace("\\n", "\n")) // 传输时\n被转义成\\n了
            .setPositiveButton(R.string.upgrade_now) { dialog, _ ->
                settingViewModel.downloadApk(TAG_DOWNLOAD, versionInfo)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.upgrade_later) { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton(R.string.ignore_this_version) { dialog, _ ->
                settingViewModel.ignoreThisVersion(versionInfo)
                dialog.dismiss()
            }
            .create()
            .apply {
                show()
                setCanceledOnTouchOutside(false)
                getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.text_hint))
                getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setTextColor(ContextCompat.getColor(context, R.color.text_warn))
            }
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