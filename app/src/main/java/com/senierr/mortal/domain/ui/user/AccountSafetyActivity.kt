package com.senierr.mortal.domain.ui.user

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.mortal.support.ext.click
import com.senierr.mortal.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityAccountSafetyBinding
import com.senierr.mortal.domain.ui.user.vm.AccountViewModel
import com.senierr.mortal.domain.ui.user.vm.UserInfoViewModel
import com.senierr.mortal.support.ext.getViewModel
import com.senierr.mortal.support.ext.showToast
import com.senierr.mortal.repository.entity.bmob.UserInfo

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class AccountSafetyActivity : BaseActivity<ActivityAccountSafetyBinding>() {

    private val accountViewModel by getViewModel<AccountViewModel>()
    private val userInfoViewModel by getViewModel<UserInfoViewModel>()

    private var currentUserInfo: UserInfo? = null

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityAccountSafetyBinding {
        return ActivityAccountSafetyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        userInfoViewModel.getLoggedCacheUserInfo()
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.siResetPassword.click {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
        binding.siAccountCancellation.click {
            showAccountCancellationDialog()
        }
    }

    private fun initViewModel() {
        userInfoViewModel.loggedCacheUserInfo.observe(this, {
            currentUserInfo = it
        }, {
            // 未登录，跳转至登录页
            LoginActivity.start(this)
            finish()
        })
        accountViewModel.deleteResult.observe(this, {
            showToast(R.string.account_cancellation_success)
            finish()
        }, {
            showToast(it.message)
        })
    }

    /**
     * 显示注销账户确认弹框
     */
    private fun showAccountCancellationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.account_cancellation)
            .setMessage(R.string.account_cancellation_confirm)
            .setPositiveButton(R.string.done) { dialog, _ ->
                currentUserInfo?.let {
                    accountViewModel.delete(it.objectId, it.sessionToken)
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .apply {
                show()
                setCanceledOnTouchOutside(false)
                getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(this@AccountSafetyActivity, R.color.text_warn))
            }
    }
}
