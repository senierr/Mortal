package com.senierr.mortal.domain.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.FragmentMeBinding
import com.senierr.mortal.domain.category.CategoryManagerActivity
import com.senierr.mortal.domain.setting.SettingActivity
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.ext.show
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

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMeBinding {
        return FragmentMeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            initView()
            initViewModel(it)
            doRefresh()
        }
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

    private fun initViewModel(context: Context) {
        userInfoViewModel.userinfo.observe(this, {
            renderLogged(it)
        }, {
            ToastUtil.showShort(context, it.message)
            renderNotLogged()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == LoginActivity.LOGIN_SUCCESS) {
            doRefresh()
        }
    }

    /**
     * 刷新页面
     */
    private fun doRefresh() {
        userInfoViewModel.fetchUserInfo()
    }

    /**
     * 渲染登录状态
     */
    private fun renderLogged(userInfo: UserInfo) {
        binding?.llUser?.click {
            // 用户详情
            activity?.let { context ->
                UserInfoActivity.start(context)
            }
        }
        // 头像
        binding?.ivAvatar?.show(userInfo.avatar, isCircle = true)
        // 昵称
        binding?.tvNickname?.text = userInfo.username
    }

    /**
     * 渲染未登录状态
     */
    private fun renderNotLogged() {
        binding?.llUser?.click {
            LoginActivity.startForResult(this, REQUEST_CODE_LOGIN)
        }
        // 头像
        binding?.ivAvatar?.show(R.drawable.ic_account_circle, isCircle = true)
        // 昵称
        binding?.tvNickname?.setText(R.string.login_or_register)
    }
}