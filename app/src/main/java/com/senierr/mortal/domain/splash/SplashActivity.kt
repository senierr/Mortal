package com.senierr.mortal.domain.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.domain.main.MainActivity
import com.senierr.mortal.domain.splash.vm.SplashViewModel

/**
 * 引导页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SplashActivity : BaseActivity(R.layout.activity_splash) {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
//        splashViewModel.autoLogin()
        MainActivity.start(this)
        finish()
    }

    private fun initViewModel() {
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        splashViewModel.autoLoginResult.observe(this) {
            MainActivity.start(this)
            finish()
        }
    }
}