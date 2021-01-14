package com.senierr.mortal.domain.splash

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.senierr.base.support.arch.ext.androidViewModel
import com.senierr.base.support.arch.ext.doOnSuccess
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivitySplashBinding
import com.senierr.mortal.domain.main.MainActivity
import com.senierr.mortal.domain.splash.vm.SplashViewModel
import com.senierr.mortal.ext.showImage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 引导页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val splashViewModel: SplashViewModel by androidViewModel()

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
        lifecycleScope.launchWhenStarted {
            splashViewModel.randomGil
                    .doOnSuccess {
                        binding.ivSplash.showImage(it.url)
                    }
                    .collect()
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