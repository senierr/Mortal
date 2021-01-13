package com.senierr.mortal.domain.setting

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.arch.ext.doOnFailure
import com.senierr.base.support.arch.ext.doOnSuccess
import com.senierr.base.support.arch.ext.getAndroidViewModel
import com.senierr.base.support.arch.ext.getViewModel
import com.senierr.base.support.ext.*
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.AppUtil
import com.senierr.base.support.utils.FileUtil
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivitySettingBinding
import com.senierr.mortal.domain.setting.vm.SettingViewModel
import com.senierr.mortal.domain.user.AccountSafetyActivity
import com.senierr.mortal.domain.user.LoginActivity
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.showToast
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.entity.bmob.VersionInfo
import kotlinx.coroutines.flow.collect

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

    private val downloadApkReceiver = DownloadApkReceiver()

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        settingViewModel.getCacheSize()
//        registerReceiver(downloadApkReceiver, IntentFilter(ProgressReceiver.ACTION_REMOTE_PROGRESS_RECEIVER))
    }

    override fun onDestroy() {
//        unregisterReceiver(downloadApkReceiver)
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        userInfoViewModel.getLoggedCacheUserInfo()
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
            settingViewModel.checkNewVersion()
        }

        binding.siFeedback.click {
            startActivity(Intent(this, FeedbackActivity::class.java))
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

        lifecycleScope.launchWhenStarted {
            LogUtil.logE("newVersionInfo")
            settingViewModel.newVersionInfo
                    .doOnSuccess {
                        showNewVersionDialog(it)
                    }
                    .doOnFailure {
                        showToast(R.string.network_error)
                    }
                    .collect {}
            LogUtil.logE("noNewVersionInfo")
            settingViewModel.noNewVersionInfo.doOnSuccess {
                showToast(R.string.no_new_version)
            }
                    .collect()
            LogUtil.logE("apkDownloadCompleted")
            settingViewModel.apkDownloadCompleted
                    .doOnSuccess {
                        AppUtil.installApk(
                                this@SettingActivity,
                                "${this@SettingActivity.packageName}.provider",
                                it
                        )
                    }
                    .collect()
            LogUtil.logE("cacheSize")
            settingViewModel.cacheSize
                .doOnSuccess {
                    binding.siClearCache.message = FileUtil.getFormatSize(it.toDouble())
                }
                .collect {}
        }

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
                settingViewModel.downloadApk(versionInfo)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.upgrade_later) { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton(R.string.ignore_this_version) { dialog, _ ->
                settingViewModel.ignoreThisVersion(versionInfo)
                dialog.dismiss()
            }
            .create()
            .apply {
                show()
                setCanceledOnTouchOutside(false)
                getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.text_hint))
                getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setTextColor(ContextCompat.getColor(context, R.color.text_warn))
            }
    }
}
