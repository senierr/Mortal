package com.senierr.mortal.domain.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.main.MainActivity
import com.senierr.mortal.domain.splash.vm.SplashViewModel
import com.senierr.mortal.ext.show
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 引导页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
@ExperimentalCoroutinesApi
class SplashActivity : BaseActivity(R.layout.activity_splash) {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()

        splashViewModel.fetchAdvert()
        delayStart()
    }

    private fun initView() {
        btn_timer?.click {
            startToMain()
        }
    }

    private fun initViewModel() {
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        splashViewModel.fetchAdvertResult.observe(this, {
            iv_splash?.show(it.image)
        })
    }

    private fun delayStart() {
        launch {
            for (i in 5 downTo 0) {
                btn_timer?.text = getString(R.string.format_skip, i)
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