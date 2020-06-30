package com.senierr.mortal.domain.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.show
import com.senierr.repository.entity.bmob.UserInfo
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.activity_webview.tb_top
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.fragment_me.iv_avatar
import kotlinx.android.synthetic.main.fragment_me.tv_nickname
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 用户详情页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
@ExperimentalCoroutinesApi
class UserInfoActivity : BaseActivity(R.layout.activity_user_info) {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserInfoActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var userInfoViewModel: UserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()

        userInfoViewModel.fetchUserInfo()
    }

    private fun initView() {
        setSupportActionBar(tb_top)
        tb_top?.navigationIcon?.setTint(getColor(R.color.btn_black))
        tb_top?.setNavigationOnClickListener { finish() }
    }

    private fun initViewModel() {
        userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        userInfoViewModel.fetchUserInfoResult.observe(this, {
            renderLogged(it)
        }, {
            ToastUtil.showShort(this, it.message)
        })
    }

    /**
     * 渲染登录状态
     */
    private fun renderLogged(userInfo: UserInfo) {
        // 头像
        iv_avatar?.show(userInfo.avatar, isCircle = true)
        // 昵称
        userInfo.username.let {
            tv_nickname?.text = if (it.isNotBlank()) it else getString(R.string.none)
        }
        // 邮箱
        userInfo.email.let {
            tv_email?.text = if (it.isNotBlank()) it else getString(R.string.none)
        }
    }
}
