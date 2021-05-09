package com.senierr.mortal.domain.user

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.arch.ext.onFailure
import com.senierr.base.support.arch.ext.onSuccess
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityAccountSafetyBinding
import com.senierr.mortal.domain.dialog.createLoadingDialog
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.showToast
import com.senierr.repository.entity.bmob.UserInfo
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class AccountSafetyActivity : BaseActivity<ActivityAccountSafetyBinding>() {

    private val loadingDialog by lazy { createLoadingDialog(this) }

    private val accountViewModel: AccountViewModel by viewModel()
    private val userInfoViewModel: UserInfoViewModel by viewModel()

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
        lifecycleScope.launchWhenStarted {
            userInfoViewModel.loggedCacheUserInfo
                .onSuccess {
                    currentUserInfo = it
                }
                .onFailure {
                    // 未登录，跳转至登录页
                    LoginActivity.start(this@AccountSafetyActivity)
                    finish()
                }
                .launchIn(this)

            accountViewModel.deleteResult
                .onSuccess {
                    showToast(R.string.account_cancellation_success)
                    finish()
                }
                .onFailure {
                    showToast(it?.message)
                }
                .onEach {
                    loadingDialog.dismiss()
                }
                .launchIn(this)
        }
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
                    loadingDialog.show()
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
                    .setTextColor(
                        ContextCompat.getColor(
                            this@AccountSafetyActivity,
                            R.color.text_warn
                        )
                    )
            }
    }
}
