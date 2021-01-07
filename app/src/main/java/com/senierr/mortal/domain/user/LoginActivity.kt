package com.senierr.mortal.domain.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.KeyboardUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityLoginBinding
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.ext.showToast
import com.senierr.mortal.widget.CircularAnim
import com.senierr.repository.entity.bmob.BmobException
import com.senierr.repository.entity.bmob.UserInfo

/**
 * 登录页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    companion object {
        private const val EXTRA_TARGET_INTENT = "target_intent"
        const val LOGIN_SUCCESS = 1001
        const val LOGIN_FAILURE = 1002

        fun start(context: Context, targetIntent: Intent? = null) {
            val intent = Intent(context, LoginActivity::class.java)
            targetIntent?.let {
                intent.putExtra(EXTRA_TARGET_INTENT, it)
            }
            context.startActivity(intent)
        }

        fun startForResult(activity: FragmentActivity, requestCode: Int) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }

        fun startForResult(fragment: Fragment, requestCode: Int) {
            val intent = Intent(fragment.context, LoginActivity::class.java)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var loadingDialog: AlertDialog

    private val accountViewModel by getViewModel<AccountViewModel>()
    private val userInfoViewModel by getViewModel<UserInfoViewModel>()

    private val allCacheUserInfo = mutableListOf<UserInfo>()

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        userInfoViewModel.getAllCacheUserInfo()
    }

    override fun onBackPressed() {
        doFinish(false)
    }

    override fun onDestroy() {
        loadingDialog.dismiss()
        super.onDestroy()
    }

    private fun initView() {
        binding.btnClose.click { onBackPressed() }
        binding.btnLogin.click { doLogin() }
        binding.btnRegister.click {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loadingDialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.layout_status_loading)
            .create()
            .apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
    }

    private fun initViewModel() {
        userInfoViewModel.allCacheUserInfo.observe(this) {
            allCacheUserInfo.clear()
            allCacheUserInfo.addAll(it)
            allCacheUserInfo.first().let { info ->
                binding.etAccount.text = SpannableStringBuilder(info.username)
                binding.etPassword.text = SpannableStringBuilder(info.password)
            }
        }
        accountViewModel.loginResult.observe(this,
            { showLoginSuccess() },
            { showLoginFailure(it) })
    }

    /**
     * 验证账号合法性
     */
    private fun verifyAccount(account: String?): Boolean {
        if (account.isNullOrBlank()) {
            binding.tilAccount.isErrorEnabled = true
            binding.tilAccount.error = getString(R.string.account_empty)
            return false
        }
        binding.tilAccount.isErrorEnabled = false
        return true
    }

    /**
     * 验证密码合法性
     */
    private fun verifyPassword(password: String?): Boolean {
        if (password.isNullOrBlank()) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = getString(R.string.password_empty)
            return false
        }
        binding.tilPassword.isErrorEnabled = false
        return true
    }

    /**
     * 登录
     */
    private fun doLogin() {
        KeyboardUtil.hideSoftInput(this)

        val account = binding.etAccount.text?.toString()
        val password = binding.etPassword.text?.toString()

        if (verifyAccount(account) && verifyPassword(password)) {
            if (account != null && password != null) {
                loadingDialog.show()
                accountViewModel.login(account, password)
            }
        }
    }

    /**
     * 展现登录成功
     */
    private fun showLoginSuccess() {
        loadingDialog.dismiss()
        CircularAnim().fullActivity(this, binding.btnLogin)
            .colorOrImageRes(R.color.app_theme)
            .go(object : CircularAnim.OnAnimationEndListener {
                override fun onAnimationEnd() {
                    doFinish(true)
                }
            })
    }

    /**
     * 展现登录失败
     */
    private fun showLoginFailure(exception: Exception) {
        loadingDialog.dismiss()
        if (exception is BmobException) {
            showToast(exception.error)
        } else {
            showToast(R.string.network_error)
        }
    }

    /**
     * 页面关闭处理
     */
    private fun doFinish(isSuccess: Boolean) {
        if (isSuccess) {
            val targetIntent = intent.getParcelableExtra<Intent>(EXTRA_TARGET_INTENT)
            if (targetIntent != null) {
                startActivity(targetIntent)
            } else {
                setResult(LOGIN_SUCCESS, intent)
            }
        } else {
            setResult(LOGIN_FAILURE, intent)
        }
        finish()
    }
}
