package com.senierr.mortal.domain.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityAccountSafetyBinding

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class AccountSafetyActivity : BaseActivity<ActivityAccountSafetyBinding>() {

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityAccountSafetyBinding {
        return ActivityAccountSafetyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.siResetPassword.click {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
        binding.siAccountCancellation.click {

        }
    }
}
