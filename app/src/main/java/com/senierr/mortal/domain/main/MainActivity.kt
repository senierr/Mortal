package com.senierr.mortal.domain.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.domain.home.HomeFragment
import com.senierr.mortal.domain.recommend.RecommendFragment
import com.senierr.mortal.domain.user.MeFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 主页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        vp_main?.adapter = MainPageAdapter(this)
        vp_main?.offscreenPageLimit = 2
        vp_main?.isUserInputEnabled = false
        vp_main?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bnv_bottom?.selectedItemId = R.id.tab_home
                    1 -> bnv_bottom?.selectedItemId = R.id.tab_recommend
                    2 -> bnv_bottom?.selectedItemId = R.id.tab_me
                    else -> bnv_bottom?.selectedItemId = R.id.tab_home
                }
            }
        })

        bnv_bottom?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tab_home -> vp_main.currentItem = 0
                R.id.tab_recommend -> vp_main.currentItem = 1
                R.id.tab_me -> vp_main.currentItem = 2
            }
            return@setOnNavigationItemSelectedListener true
        }

        // TODO 测试徽章功能
        val badgeDrawable = bnv_bottom?.getOrCreateBadge(R.id.tab_me)
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