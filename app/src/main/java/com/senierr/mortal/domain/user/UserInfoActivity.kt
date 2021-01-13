package com.senierr.mortal.domain.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.RegexUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityUserInfoBinding
import com.senierr.mortal.domain.common.EditTextActivity
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.mortal.ext.showImage
import com.senierr.mortal.ext.showToast
import com.senierr.repository.entity.bmob.UserInfo

/**
 * 用户详情页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {

    companion object {
        private const val REQUEST_CODE_EDIT_NICKNAME = 100
        private const val REQUEST_CODE_EDIT_EMAIL = 101
    }

    private val userInfoViewModel by viewModel<UserInfoViewModel>()

    private var currentUserInfo: UserInfo? = null

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityUserInfoBinding {
        return ActivityUserInfoBinding.inflate(layoutInflater)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_EDIT_NICKNAME -> {
                    val newNickname = data.getStringExtra(EditTextActivity.KEY_EDIT_TEXT) ?: return
                    currentUserInfo?.let {
                        userInfoViewModel.updateNickname(it, newNickname)
                    }
                }
                REQUEST_CODE_EDIT_EMAIL -> {
                    val newEmail = data.getStringExtra(EditTextActivity.KEY_EDIT_TEXT) ?: return
                    currentUserInfo?.let {
                        userInfoViewModel.updateEmail(it, newEmail)
                    }
                }
            }
        }
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.siNickname.click {
            currentUserInfo?.let {
                EditTextActivity.startForResult(
                    this, REQUEST_CODE_EDIT_NICKNAME,
                    getString(R.string.edit_nickname), it.nickname,
                    getString(R.string.nickname), getString(R.string.nickname_helper_text)
                )
            }
        }

        binding.siEmail.click {
            currentUserInfo?.let {
                EditTextActivity.startForResult(
                    this, REQUEST_CODE_EDIT_EMAIL,
                    getString(R.string.edit_email), it.email,
                    getString(R.string.email),
                    regex = RegexUtil.REGEX_EMAIL, errorText = getString(R.string.email_verify_hint)
                )
            }
        }
    }

    private fun initViewModel() {
        userInfoViewModel.loggedCacheUserInfo.observe(this, {
            currentUserInfo = it
            renderUserInfo(it)
            userInfoViewModel.fetchUserInfo(it.objectId)
        }, {
            // 未登录，跳转至登录页
            LoginActivity.start(this)
            finish()
        })
        userInfoViewModel.userinfo.observe(this, {
            currentUserInfo = it
            renderUserInfo(it)
        }, {
            showToast(it.message)
        })
    }

    /**
     * 渲染用户信息
     */
    private fun renderUserInfo(userInfo: UserInfo) {
        // 缓存当前用户信息
        currentUserInfo = userInfo
        // 头像
        binding.ivAvatar.showImage(userInfo.avatar)
        // 昵称
        userInfo.nickname.let {
            binding.siNickname.message = if (it.isNotBlank()) it else getString(R.string.none)
        }
        // 邮箱
        userInfo.email.let {
            binding.siEmail.message = if (it.isNotBlank()) it else getString(R.string.none)
        }
    }
}
