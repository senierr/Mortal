package com.senierr.mortal.domain.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.KeyboardUtil
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.user.vm.LoginViewModel
import com.senierr.mortal.widget.CircularAnim
import com.senierr.repository.entity.bmob.BmobException
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

/**
 * 登录页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SettingActivity : BaseActivity(R.layout.activity_login) {

    companion object {
        private const val EXTRA_TARGET_INTENT = "target_intent"
        const val LOGIN_SUCCESS = 1001
        const val LOGIN_FAILURE = 1002

        fun start(context: Context, targetIntent: Intent? = null) {
            val intent = Intent(context, SettingActivity::class.java)
            targetIntent?.let {
                intent.putExtra(EXTRA_TARGET_INTENT, it)
            }
            context.startActivity(intent)
        }

        fun startForResult(activity: FragmentActivity, requestCode: Int) {
            val intent = Intent(activity, SettingActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }

        fun startForResult(fragment: Fragment, requestCode: Int) {
            val intent = Intent(fragment.context, SettingActivity::class.java)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var loadingDialog: AlertDialog

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onBackPressed() {
        doFinish(false)
    }

    private fun initView() {
        btn_close?.click { onBackPressed() }
        btn_login?.click { doLogin() }

        loadingDialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.layout_status_loading)
            .create()
    }

    private fun initViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginViewModel.loginResult.observe(this,
            { showLoginSuccess() },
            { showLoginFailure(it) })
    }

    /**
     * 验证账号合法性
     */
    private fun verifyAccount(account: String?): Boolean {
        if (account.isNullOrBlank()) {
            til_account?.isErrorEnabled = true
            til_account?.error = getString(R.string.account_empty)
            return false
        }
        til_account?.isErrorEnabled = false
        return true
    }

    /**
     * 验证密码合法性
     */
    private fun verifyPassword(password: String?): Boolean {
        if (password.isNullOrBlank()) {
            til_password?.isErrorEnabled = true
            til_password?.error = getString(R.string.password_empty)
            return false
        }
        til_password?.isErrorEnabled = false
        return true
    }

    /**
     * 登录
     */
    private fun doLogin() {
        KeyboardUtil.hideSoftInput(this)

        val account = et_account?.text?.toString()
        val password = et_password?.text?.toString()

        if (verifyAccount(account) && verifyPassword(password)) {
            if (account != null && password != null) {
                loadingDialog.show()
                loginViewModel.login(account, password)
            }
        }
    }

    /**
     * 展现登录成功
     */
    private fun showLoginSuccess() {
        loadingDialog.dismiss()
        CircularAnim().fullActivity(this, btn_login)
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
            ToastUtil.showLong(this, exception.error)
        } else {
            ToastUtil.showLong(this, R.string.network_error)
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
