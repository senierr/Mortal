package com.senierr.mortal.domain.main

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
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.AppUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityMainBinding
import com.senierr.mortal.domain.home.HomeFragment
import com.senierr.mortal.domain.recommend.RecommendFragment
import com.senierr.mortal.domain.user.MeFragment
import com.senierr.mortal.receiver.UpgradeReceiver
import com.senierr.mortal.service.UpgradeService
import com.senierr.repository.entity.bmob.VersionInfo
import java.io.File

/**
 * 主页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val upgradeReceiver = UpgradeReceiver(this@MainActivity)

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        registerReceiver(upgradeReceiver, IntentFilter(UpgradeReceiver.ACTION_UPGRADE_RECEIVER))
        UpgradeService.checkNewVersion(this)
    }

    override fun onDestroy() {
        unregisterReceiver(upgradeReceiver)
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