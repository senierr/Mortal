package com.senierr.mortal.domain.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityUserInfoBinding
import com.senierr.mortal.databinding.DialogEditTextBinding
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

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.llNickname.click {
            val dialogBinding = DialogEditTextBinding.inflate(layoutInflater)
            // 默认值
            dialogBinding.etEdit.text = SpannableStringBuilder(currentUserInfo?.nickname)
            dialogBinding.etEdit.setSelection(dialogBinding.etEdit.text?.length ?: 0)
            AlertDialog.Builder(this)
                .setTitle(R.string.edit_nickname)
                .setView(dialogBinding.root)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.done) { dialog, _ ->
                    val newNickname = dialogBinding.etEdit.text?.toString()?.trim() ?: return@setPositiveButton
                    currentUserInfo?.let {
                        userInfoViewModel.updateNickname(it, newNickname)
                    }
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        binding.llEmail.click {
            val dialogBinding = DialogEditTextBinding.inflate(layoutInflater)
            // 默认值
            dialogBinding.etEdit.text = SpannableStringBuilder(currentUserInfo?.email)
            dialogBinding.etEdit.setSelection(dialogBinding.etEdit.text?.length ?: 0)
            AlertDialog.Builder(this)
                .setTitle(R.string.edit_email)
                .setView(dialogBinding.root)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.done) { dialog, _ ->
                    val newEmail = dialogBinding.etEdit.text?.toString()?.trim() ?: return@setPositiveButton
                    currentUserInfo?.let {
                        userInfoViewModel.updateEmail(it, newEmail)
                    }
                    dialog.dismiss()
                }
                .create()
                .show()
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
            binding.tvNickname.text = if (it.isNotBlank()) it else getString(R.string.none)
        }
        // 邮箱
        userInfo.email.let {
            binding.tvEmail.text = if (it.isNotBlank()) it else getString(R.string.none)
        }
    }
}
