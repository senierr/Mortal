package com.senierr.mortal.domain.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.senierr.mortal.support.ext.click
import com.senierr.mortal.support.ui.BaseFragment
import com.senierr.mortal.R
import com.senierr.mortal.databinding.FragmentMeBinding
import com.senierr.mortal.domain.ui.category.CategoryManagerActivity
import com.senierr.mortal.domain.ui.setting.SettingActivity
import com.senierr.mortal.domain.ui.user.vm.UserInfoViewModel
import com.senierr.mortal.support.ext.getViewModel
import com.senierr.mortal.support.ext.showImage
import com.senierr.mortal.repository.entity.bmob.UserInfo

/**
 * 我的页面
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class MeFragment : BaseFragment<FragmentMeBinding>() {

    companion object {
        const val REQUEST_CODE_LOGIN = 100
    }

    private val userInfoViewModel by getViewModel<UserInfoViewModel>()

    private var currentUserInfo: UserInfo? = null

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMeBinding {
        return FragmentMeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        userInfoViewModel.getLoggedCacheUserInfo()
    }

    private fun initView() {
        binding?.llUser?.isClickable = false

        binding?.siCategoryManager?.click {
            startActivity(Intent(context, CategoryManagerActivity::class.java))
        }
        binding?.siSetting?.click {
            startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    private fun initViewModel() {
        userInfoViewModel.loggedCacheUserInfo.observe(this, {
            currentUserInfo = it
            renderUserInfo(it)
            userInfoViewModel.fetchUserInfo(it.objectId)
        }, {
            renderUserInfo(null)
        })
        userInfoViewModel.userinfo.observe(this) {
            currentUserInfo = it
            renderUserInfo(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == LoginActivity.LOGIN_SUCCESS) {
            userInfoViewModel.getLoggedCacheUserInfo()
        }
    }

    /**
     * 渲染登录状态
     */
    private fun renderUserInfo(userInfo: UserInfo?) {
        binding?.llUser?.click {
            if (userInfo == null) {
                LoginActivity.startForResult(this, REQUEST_CODE_LOGIN)
            } else {
                startActivity(Intent(context, UserInfoActivity::class.java))
            }
        }
        // 头像
        binding?.ivAvatar?.apply {
            if (userInfo?.avatar.isNullOrBlank()) {
                setImageResource(R.drawable.ic_account_circle)
            } else {
                userInfo?.avatar?.let { showImage(it) }
            }
        }
        // 昵称
        binding?.tvNickname?.apply {
            if (userInfo == null) {
                setText(R.string.login_or_register)
            } else {
                text = if (userInfo.nickname.isBlank()) {
                    userInfo.username
                } else {
                    userInfo.nickname
                }
            }
        }
    }
}