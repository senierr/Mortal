package com.senierr.mortal.domain.setting

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.ext.click
import com.senierr.base.support.ext.setGone
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.AppUtil
import com.senierr.base.support.utils.FileUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivitySettingBinding
import com.senierr.mortal.domain.setting.vm.SettingViewModel
import com.senierr.mortal.domain.user.AccountSafetyActivity
import com.senierr.mortal.domain.user.LoginActivity
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.getAndroidViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.ext.showToast
import com.senierr.mortal.receiver.UpgradeReceiver
import com.senierr.mortal.service.UpgradeService
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.entity.bmob.VersionInfo

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val accountViewModel by getViewModel<AccountViewModel>()
    private val userInfoViewModel by getViewModel<UserInfoViewModel>()
    private val settingViewModel by getAndroidViewModel<SettingViewModel>()

    private var currentUserInfo: UserInfo? = null

    private val upgradeReceiver = object : UpgradeReceiver(this@SettingActivity) {
        override fun onNewVersion(versionInfo: VersionInfo?) {
            super.onNewVersion(versionInfo)
            if (versionInfo == null) {
                showToast(R.string.no_new_version)
            }
        }
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        registerReceiver(upgradeReceiver, IntentFilter(UpgradeReceiver.ACTION_UPGRADE_RECEIVER))
        settingViewModel.getCacheSize()
    }

    override fun onStart() {
        super.onStart()
        userInfoViewModel.getLoggedCacheUserInfo()
    }

    override fun onDestroy() {
        unregisterReceiver(upgradeReceiver)
        super.onDestroy()
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.siAccountAndSafety.click {
            startActivity(Intent(this, AccountSafetyActivity::class.java))
        }

        binding.siClearCache.click {
            settingViewModel.clearCache()
        }

        binding.siCheckNewVersion.message = AppUtil.getVersionName(this, packageName)
        binding.siCheckNewVersion.click {
            UpgradeService.checkNewVersion(this)
        }

        binding.siAbout.click {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        binding.btnLogOut.click {
            showLogoutConfirmDialog()
        }
    }

    private fun initViewModel() {
        userInfoViewModel.loggedCacheUserInfo.observe(this, {
            currentUserInfo = it
            binding.btnLogOut.setGone(false)
        }, {
            binding.btnLogOut.setGone(true)
        })
        accountViewModel.logoutResult.observe(this) {
            LoginActivity.start(this)
            finish()
        }
        settingViewModel.cacheSize.observe(this) {
            binding.siClearCache.message = FileUtil.getFormatSize(it.toDouble())
        }
    }

    /**
     * 显示退出登录确认弹框
     */
    private fun showLogoutConfirmDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.log_out)
            .setMessage(R.string.log_out_confirm)
            .setPositiveButton(R.string.done) { dialog, _ ->
                currentUserInfo?.let {
                    accountViewModel.logout(it.objectId)
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .apply {
                show()
                setCanceledOnTouchOutside(false)
                getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.text_hint))
            }
    }
}
