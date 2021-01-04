package com.senierr.mortal.domain.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseFragment
import com.senierr.mortal.R
import com.senierr.mortal.databinding.FragmentMeBinding
import com.senierr.mortal.domain.category.CategoryManagerActivity
import com.senierr.mortal.domain.setting.SettingActivity
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.ext.showImage
import com.senierr.repository.entity.bmob.UserInfo

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
        userInfoViewModel.getLoggedCacheUserInfo()
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
            if (userInfo == null) {
                setImageResource(R.drawable.ic_account_circle)
            } else {
                showImage(userInfo.avatar)
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