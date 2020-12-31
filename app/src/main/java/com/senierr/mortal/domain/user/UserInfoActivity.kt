package com.senierr.mortal.domain.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityUserInfoBinding
import com.senierr.mortal.domain.common.EditTextActivity
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.ext.show
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

        fun start(context: Context) {
            val intent = Intent(context, UserInfoActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val userInfoViewModel by getViewModel<UserInfoViewModel>()

    private var currentUserInfo: UserInfo? = null

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityUserInfoBinding {
        return ActivityUserInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        userInfoViewModel.fetchUserInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 0 && data != null) {
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
                EditTextActivity.startForResult(this, REQUEST_CODE_EDIT_NICKNAME,
                    getString(R.string.edit_nickname), "提示", it.nickname)
            }
        }

        binding.siEmail.click {
            currentUserInfo?.let {
                EditTextActivity.startForResult(this, REQUEST_CODE_EDIT_EMAIL,
                    getString(R.string.edit_email), "提示", it.email)
            }
        }
    }

    private fun initViewModel() {
        userInfoViewModel.userinfo.observe(this, {
            renderUserInfo(it)
        }, {
            ToastUtil.showShort(this, it.message)
        })
        userInfoViewModel.userinfo.observe(this, {
            renderUserInfo(it)
        }, {
            ToastUtil.showShort(this, it.message)
        })
    }

    /**
     * 渲染用户信息
     */
    private fun renderUserInfo(userInfo: UserInfo) {
        // 缓存当前用户信息
        currentUserInfo = userInfo
        // 头像
        binding.ivAvatar.show(userInfo.avatar, isCircle = true)
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
