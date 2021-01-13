package com.senierr.mortal.domain.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.RegexUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityResetPasswordBinding
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.mortal.ext.showToast
import com.senierr.repository.entity.bmob.UserInfo

/**
 * 重置密码页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding>() {

    companion object {
        /**
         * 正则：密码，取值范围为a-z、A-Z、0-9、"_"，6-16位
         */
        private const val REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$"
    }

    private val accountViewModel by viewModel<AccountViewModel>()
    private val userInfoViewModel by viewModel<UserInfoViewModel>()

    private var currentUserInfo: UserInfo? = null

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityResetPasswordBinding {
        return ActivityResetPasswordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val doneMenu = menu?.findItem(R.id.tab_done)
        val password = binding.etPassword.text.toString().trim()
        val newPassword = binding.etNewPassword.text.toString().trim()
        if (verifyPassword(password) && verifyPassword(newPassword)) {
            doneMenu?.isEnabled = true
            doneMenu?.icon?.setTint(getColor(R.color.black))
        } else {
            doneMenu?.isEnabled = false
            doneMenu?.icon?.setTint(getColor(R.color.btn_unable))
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_common_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_done -> resetPassword()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        userInfoViewModel.getLoggedCacheUserInfo()
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = binding.etPassword.text.toString().trim()
                if (verifyPassword(password)) {
                    binding.tilPassword.error = null
                } else {
                    binding.tilPassword.error = getString(R.string.password_verify_hint)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                invalidateOptionsMenu()
            }
        })
        binding.etNewPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newPassword = binding.etNewPassword.text.toString().trim()
                if (verifyPassword(newPassword)) {
                    binding.tilNewPassword.error = null
                } else {
                    binding.tilNewPassword.error = getString(R.string.password_verify_hint)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                invalidateOptionsMenu()
            }
        })
    }

    private fun initViewModel() {
        userInfoViewModel.loggedCacheUserInfo.observe(this, {
            currentUserInfo = it
        }, {
            // 未登录，跳转至登录页
            LoginActivity.start(this)
            finish()
        })
        accountViewModel.resetPasswordResult.observe(this, {
            showToast(R.string.reset_password_success)
            finish()
        }, {
            showToast(it.message)
        })
    }

    /**
     * 验证密码合法性
     *
     * 6-16位，数字、字母、下划线
     */
    private fun verifyPassword(password: String): Boolean {
        return RegexUtil.isMatch(REGEX_PASSWORD, password)
    }

    private fun resetPassword() {
        val password = binding.etPassword.text.toString().trim()
        val newPassword = binding.etNewPassword.text.toString().trim()
        if (verifyPassword(password) && verifyPassword(newPassword)) {
            currentUserInfo?.let {
                accountViewModel.resetPassword(it, password, newPassword)
            }
        }
    }
}
