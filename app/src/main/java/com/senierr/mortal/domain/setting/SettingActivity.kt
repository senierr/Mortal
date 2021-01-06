package com.senierr.mortal.domain.setting

import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.ext.click
import com.senierr.base.support.ext.setGone
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.AppUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivitySettingBinding
import com.senierr.mortal.domain.user.AccountSafetyActivity
import com.senierr.mortal.domain.user.LoginActivity
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.service.UpgradeService
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.entity.bmob.VersionInfo
import java.io.File

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SettingActivity : BaseActivity<ActivitySettingBinding>(), UpgradeService.UpgradeCallback {

    private val accountViewModel by getViewModel<AccountViewModel>()
    private val userInfoViewModel by getViewModel<UserInfoViewModel>()

    private var currentUserInfo: UserInfo? = null

    private var isBindUpgradeService = false
    private var upgradeBinder: UpgradeService.UpgradeBinder? = null
    private val upgradeConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            upgradeBinder = service as UpgradeService.UpgradeBinder
            upgradeBinder?.setCallBack(this@SettingActivity)
            upgradeBinder?.getService()?.checkNewVersion()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            upgradeBinder = null
        }
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
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

    override fun onDestroy() {
        if (isBindUpgradeService) {
            unbindService(upgradeConnection)
        }
        super.onDestroy()
    }

    /**
     * 发现新版本
     */
    override fun onNewVersion(versionInfo: VersionInfo) {
        showNewVersionDialog(versionInfo)
    }

    /**
     * 新版本下载完成
     */
    override fun onDownloadCompleted(apkFile: File) {
        AppUtil.installApk(this, "${packageName}.provider", apkFile)
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.siAccountAndSafety.click {
            startActivity(Intent(this, AccountSafetyActivity::class.java))
        }

        binding.siAbout.click {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        binding.siCheckNewVersion.message = AppUtil.getVersionName(this, packageName)
        binding.siCheckNewVersion.click {
            val intent = Intent(this, UpgradeService::class.java)
            isBindUpgradeService = bindService(intent, upgradeConnection, BIND_AUTO_CREATE)
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

    /**
     * 显示新版本提示
     */
    private fun showNewVersionDialog(versionInfo: VersionInfo) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.discover_new_version)
            .setMessage(versionInfo.changeLog.replace("\\n", "\n")) // 传输时\n被转义成\\n了
            .setPositiveButton(R.string.upgrade_now) { dialog, _ ->
                upgradeBinder?.getService()?.downloadApk(versionInfo)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.upgrade_later) { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton(R.string.ignore_this_version) { dialog, _ ->
                upgradeBinder?.getService()?.ignoreThisVersion(versionInfo)
                dialog.dismiss()
            }
            .create()
            .apply {
                show()
                setCanceledOnTouchOutside(false)
                getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.text_hint))
                getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setTextColor(ContextCompat.getColor(this@SettingActivity, R.color.text_warn))
            }
    }
}
