package com.senierr.mortal.domain.setting

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.senierr.base.support.ext.setGone
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityFeedbackBinding
import com.senierr.mortal.domain.setting.vm.SettingViewModel
import com.senierr.mortal.domain.user.LoginActivity
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.getAndroidViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.ext.showToast
import com.senierr.repository.entity.bmob.UserInfo

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class FeedbackActivity : BaseActivity<ActivityFeedbackBinding>() {

    private val userInfoViewModel by getViewModel<UserInfoViewModel>()
    private val settingViewModel by getAndroidViewModel<SettingViewModel>()

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
        userInfoViewModel.loggedCacheUserInfo.observe(this, {
            currentUserInfo = it
        }, {
            // 未登录，跳转至登录页
            LoginActivity.start(this)
            finish()
        })
        settingViewModel.feedbackResult.observe(this) {
            if (it.isSuccess) {
                showToast(R.string.feedback_success)
                finish()
            } else {
                showToast(R.string.network_error)
            }
        }
    }
}
