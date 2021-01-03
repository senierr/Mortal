package com.senierr.mortal.domain.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.senierr.base.support.ext.click
import com.senierr.base.support.ext.setGone
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivitySettingBinding
import com.senierr.mortal.domain.user.AccountSafetyActivity
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.repository.entity.bmob.UserInfo

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val accountViewModel by getViewModel<AccountViewModel>()
    private val userInfoViewModel by getViewModel<UserInfoViewModel>()

    private var currentUserInfo: UserInfo? = null

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        userInfoViewModel.fetchUserInfo()
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.siAccountAndSafety.click {
            startActivity(Intent(this, AccountSafetyActivity::class.java))
        }

        binding.btnLogOut.click {
            currentUserInfo?.let {
                accountViewModel.logout(it.objectId)
            }
        }
    }

    private fun initViewModel() {
        userInfoViewModel.userinfo.observe(this, {
            currentUserInfo = it
            binding.btnLogOut.setGone(false)
        }, {
            binding.btnLogOut.setGone(true)
        })
        accountViewModel.logoutResult.observe(this) {
            userInfoViewModel.fetchUserInfo()
        }
    }
}
