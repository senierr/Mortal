package com.senierr.mortal.domain.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityUserInfoBinding
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.show
import com.senierr.repository.entity.bmob.UserInfo

/**
 * 用户详情页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class UserInfoActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserInfoActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var userInfoViewModel: UserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        userInfoViewModel.fetchUserInfo()
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }
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
        binding.ivAvatar.show(userInfo.avatar, isCircle = true)
        // 昵称
        userInfo.username.let {
            binding.tvNickname.text = if (it.isNotBlank()) it else getString(R.string.none)
        }
        // 邮箱
        userInfo.email.let {
            binding.tvEmail.text = if (it.isNotBlank()) it else getString(R.string.none)
        }
    }
}
