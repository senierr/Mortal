package com.senierr.mortal.domain.ui.main

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.mortal.support.ui.BaseActivity
import com.senierr.mortal.support.utils.AppUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityMainBinding
import com.senierr.mortal.domain.ui.home.HomeFragment
import com.senierr.mortal.domain.ui.recommend.RecommendFragment
import com.senierr.mortal.domain.ui.user.MeFragment
import com.senierr.mortal.domain.service.UpgradeService
import com.senierr.mortal.repository.entity.bmob.VersionInfo
import java.io.File

/**
 * 主页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class MainActivity : BaseActivity<ActivityMainBinding>(), UpgradeService.UpgradeCallback {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private var upgradeBinder: UpgradeService.UpgradeBinder? = null
    private val upgradeConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            upgradeBinder = service as UpgradeService.UpgradeBinder
            upgradeBinder?.setCallBack(this@MainActivity)
            upgradeBinder?.getService()?.checkNewVersion()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            upgradeBinder = null
        }
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        bindService(Intent(this, UpgradeService::class.java), upgradeConnection, BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(upgradeConnection)
        super.onDestroy()
    }

    /**
     * 发现新版本
     */
    override fun onNewVersion(versionInfo: VersionInfo?) {
        versionInfo?.let {
            showNewVersionDialog(it)
        }
    }

    /**
     * 新版本下载完成
     */
    override fun onDownloadCompleted(apkFile: File) {
        AppUtil.installApk(this, "${packageName}.provider", apkFile)
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

    /**
     * 显示新版本提示
     */
    private fun showNewVersionDialog(versionInfo: VersionInfo) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.discover_new_version)
            .setMessage(versionInfo.changeLog.replace("\\n", "\n")) // 传输时\n被转义成\\n了
            .setPositiveButton(R.string.upgrade_now) { dialog, _ ->
                upgradeBinder?.getService()?.downloadApk(versionInfo)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.upgrade_later) { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton(R.string.ignore_this_version) { dialog, _ ->
                upgradeBinder?.getService()?.ignoreThisVersion(versionInfo)
                dialog.dismiss()
            }
            .create()
            .apply {
                show()
                setCanceledOnTouchOutside(false)
                getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_hint))
                getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setTextColor(ContextCompat.getColor(this@MainActivity, R.color.text_warn))
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