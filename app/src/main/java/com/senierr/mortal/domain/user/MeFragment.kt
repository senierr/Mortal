package com.senierr.mortal.domain.user

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.repository.entity.bmob.UserInfo
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.coroutines.*

/**
 * 我的页面
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
@ExperimentalCoroutinesApi
class MeFragment : BaseFragment(R.layout.fragment_me) {

    companion object {
        const val REQUEST_CODE_LOGIN = 100
    }

    private lateinit var userInfoViewModel: UserInfoViewModel

    override fun onLazyCreate(context: Context) {
        initView()
        initViewModel(context)

        doRefresh()
    }

    private fun initView() {
        ll_user?.isClickable = false
    }

    private fun initViewModel(context: Context) {
        userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        userInfoViewModel.fetchUserInfoResult.observe(this, {
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
        ll_user?.click {
            // 用户详情
        }
        // 头像
//        iv_avatar?.show(userInfo.)
        // 昵称
        tv_nickname?.text = userInfo.username
    }

    /**
     * 渲染未登录状态
     */
    private fun renderNotLogged() {
        ll_user?.click {
            LoginActivity.startForResult(this, REQUEST_CODE_LOGIN)
        }
    }
}