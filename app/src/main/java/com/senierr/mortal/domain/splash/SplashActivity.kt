package com.senierr.mortal.domain.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivitySplashBinding
import com.senierr.mortal.domain.main.MainActivity
import com.senierr.mortal.domain.splash.vm.SplashViewModel
import com.senierr.mortal.ext.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 引导页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        splashViewModel.fetchAdvertResult.observe(this) {
            binding.ivSplash.show(it.image)
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

    private fun startToMain() {
        MainActivity.start(this@SplashActivity)
        finish()
    }
}