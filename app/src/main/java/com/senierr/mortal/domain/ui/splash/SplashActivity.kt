package com.senierr.mortal.domain.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.senierr.mortal.support.ext.click
import com.senierr.mortal.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivitySplashBinding
import com.senierr.mortal.domain.ui.main.MainActivity
import com.senierr.mortal.domain.ui.splash.vm.SplashViewModel
import com.senierr.mortal.support.ext.getAndroidViewModel
import com.senierr.mortal.support.ext.showImage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 引导页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val splashViewModel by getAndroidViewModel<SplashViewModel>()

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        splashViewModel.fetchAdvert()
        delayStart()
    }

    private fun initView() {
        binding.btnTimer.click {
            startToMain()
        }
    }

    private fun initViewModel() {
        splashViewModel.randomGil.observe(this) {
            binding.ivSplash.showImage(it.url)
        }
    }

    private fun delayStart() {
        lifecycleScope.launch {
            for (i in 5 downTo 0) {
                binding.btnTimer.text = getString(R.string.format_skip, i)
                delay(1000)
            }
            startToMain()
        }
    }

    /**
     * 打开首页
     */
    private fun startToMain() {
        lifecycleScope.cancel() // 主动关闭异步任务：finish有时间过程，极端情况会导致跳转两次
        MainActivity.start(this@SplashActivity)
        finish()
    }
}