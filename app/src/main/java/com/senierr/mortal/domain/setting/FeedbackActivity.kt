package com.senierr.mortal.domain.setting

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.senierr.base.support.arch.ext.androidViewModel
import com.senierr.base.support.arch.ext.doOnFailure
import com.senierr.base.support.arch.ext.doOnSuccess
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityFeedbackBinding
import com.senierr.mortal.domain.setting.vm.SettingViewModel
import com.senierr.mortal.domain.user.LoginActivity
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.showToast
import com.senierr.repository.entity.bmob.UserInfo
import kotlinx.coroutines.flow.collect

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class FeedbackActivity : BaseActivity<ActivityFeedbackBinding>() {

    private val userInfoViewModel: UserInfoViewModel by viewModel()
    private val settingViewModel: SettingViewModel by androidViewModel()

    private var currentUserInfo: UserInfo? = null

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityFeedbackBinding {
        return ActivityFeedbackBinding.inflate(layoutInflater)
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val doneMenu = menu?.findItem(R.id.tab_done)
        val newValue = binding.etText.text.toString()
        if (newValue.isBlank() || newValue.length > 200) {
            doneMenu?.isEnabled = false
            doneMenu?.icon?.setTint(getColor(R.color.btn_unable))
        } else {
            doneMenu?.isEnabled = true
            doneMenu?.icon?.setTint(getColor(R.color.black))
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_common_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_done -> {
                val newValue = binding.etText.text.toString()
                currentUserInfo?.let {
                    settingViewModel.feedback(newValue, it.objectId)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.etText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
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
            userInfoViewModel.loggedCacheUserInfo
                    .doOnSuccess {
                        currentUserInfo = it
                    }
                    .doOnFailure {
                        // 未登录，跳转至登录页
                        LoginActivity.start(this@FeedbackActivity)
                        finish()
                    }
                    .collect()
        }

        lifecycleScope.launchWhenStarted {
            settingViewModel.feedbackResult
                    .doOnSuccess {
                        showToast(R.string.feedback_success)
                    }
                    .doOnFailure {
                        showToast(R.string.network_error)
                    }
                    .collect()
        }
    }
}
