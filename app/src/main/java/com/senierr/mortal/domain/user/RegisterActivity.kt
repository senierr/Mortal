package com.senierr.mortal.domain.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.senierr.base.support.arch.ext.onFailure
import com.senierr.base.support.arch.ext.onSuccess
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.RegexUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityRegisterBinding
import com.senierr.mortal.domain.dialog.createLoadingDialog
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.ext.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * 注册页面
 *
 * @author zhouchunjie
 * @date 2021/1/4
 */
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    companion object {
        /**
         * 正则：密码，取值范围为a-z、A-Z、0-9、"_"，3-16位
         */
        const val REGEX_ACCOUNT = "^[a-zA-Z0-9_]{3,16}$"

        /**
         * 正则：密码，取值范围为a-z、A-Z、0-9、"_"，6-16位
         */
        const val REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$"
    }

    private val loadingDialog by lazy { createLoadingDialog(this) }

    private val accountViewModel: AccountViewModel by viewModel()

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val doneMenu = menu?.findItem(R.id.tab_done)
        doneMenu?.let { updateDoneMenu(doneMenu) }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_common_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_done -> doRegister()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.etAccount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val account = binding.etAccount.text.toString().trim()
                if (verifyAccount(account)) {
                    binding.tilAccount.error = null
                } else {
                    binding.tilAccount.error = getString(R.string.account_verify_hint)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                invalidateOptionsMenu()
            }
        })
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
        binding.etRePassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val rePassword = binding.etRePassword.text.toString().trim()
                if (verifyPassword(rePassword)) {
                    binding.tilRePassword.error = null
                } else {
                    binding.tilRePassword.error = getString(R.string.password_verify_hint)
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
        lifecycleScope.launchWhenStarted {
            accountViewModel.registerResult
                .onSuccess {
                    showToast(R.string.register_success)
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
     * 更新提交按钮
     */
    private fun updateDoneMenu(menuItem: MenuItem) {
        val account = binding.etAccount.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val rePassword = binding.etRePassword.text.toString().trim()
        if (verifyAccount(account) && verifyPassword(password) && verifyPassword(rePassword)
            && password == rePassword
        ) {
            menuItem.isEnabled = true
            menuItem.icon?.setTint(getColor(R.color.black))
        } else {
            menuItem.isEnabled = false
            menuItem.icon?.setTint(getColor(R.color.btn_unable))
        }
    }

    /**
     * 验证帐号合法性
     */
    private fun verifyAccount(account: String): Boolean {
        return RegexUtil.isMatch(REGEX_ACCOUNT, account)
    }

    /**
     * 验证密码合法性
     */
    private fun verifyPassword(password: String): Boolean {
        return RegexUtil.isMatch(REGEX_PASSWORD, password)
    }

    /**
     * 执行注册
     */
    private fun doRegister() {
        val account = binding.etAccount.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val rePassword = binding.etRePassword.text.toString().trim()
        if (verifyAccount(account) && verifyPassword(password) && verifyPassword(rePassword)
            && password == rePassword
        ) {
            loadingDialog.show()
            accountViewModel.register(account, password)
        }
    }
}
