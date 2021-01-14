package com.senierr.mortal.domain.setting

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.arch.ext.androidViewModel
import com.senierr.base.support.arch.ext.doOnFailure
import com.senierr.base.support.arch.ext.doOnSuccess
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.base.support.ext.*
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.AppUtil
import com.senierr.base.support.utils.FileUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivitySettingBinding
import com.senierr.mortal.domain.dialog.createLoadingDialog
import com.senierr.mortal.domain.setting.vm.SettingViewModel
import com.senierr.mortal.domain.user.AccountSafetyActivity
import com.senierr.mortal.domain.user.LoginActivity
import com.senierr.mortal.domain.user.vm.AccountViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.ext.showToast
import com.senierr.mortal.domain.notification.NotificationManager
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.remote.progress.Progress
import com.senierr.repository.remote.progress.ProgressReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    companion object {
        private const val TAG_DOWNLOAD = "tag_download_apk_setting"
    }

    private val loadingDialog by lazy { createLoadingDialog(this) }

    private val accountViewModel: AccountViewModel by viewModel()
    private val userInfoViewModel: UserInfoViewModel by viewModel()
    private val settingViewModel: SettingViewModel by androidViewModel()

    private var currentUserInfo: UserInfo? = null

    // 下载进度监听
    private val downloadApkReceiver = object : ProgressReceiver() {
        override fun onProgress(context: Context, tag: String, progress: Progress) {
            if (tag == TAG_DOWNLOAD) {
                NotificationManager.sendDownloadNotification(context, progress.percent)
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
        registerReceiver(
            downloadApkReceiver,
            IntentFilter(ProgressReceiver.ACTION_REMOTE_PROGRESS_RECEIVER)
        )
        settingViewModel.getCacheSize()
    }

    override fun onDestroy() {
        unregisterReceiver(downloadApkReceiver)
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
            loadingDialog.show()
            settingViewModel.clearCache()
        }

        binding.siCheckNewVersion.message = AppUtil.getVersionName(this, packageName)
        binding.siCheckNewVersion.click {
            loadingDialog.show()
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
        lifecycleScope.launchWhenStarted {
            userInfoViewModel.loggedCacheUserInfo
                .doOnSuccess {
                    currentUserInfo = it
                    binding.btnLogOut.setGone(false)
                }
                .doOnFailure {
                    binding.btnLogOut.setGone(true)
                }
                .launchIn(this)

            settingViewModel.newVersionInfo
                .onEach {
                    loadingDialog.dismiss()
                }
                .doOnSuccess {
                    showNewVersionDialog(it)
                }
                .doOnFailure {
                    showToast(R.string.network_error)
                }
                .launchIn(this)

            settingViewModel.noNewVersionInfo
                .onEach {
                    loadingDialog.dismiss()
                }
                .doOnSuccess {
                    showToast(R.string.no_new_version)
                }
                .launchIn(this)

            settingViewModel.apkDownloadCompleted
                .doOnSuccess {
                    // 延迟，防止通知后发
                    delay(100)
                    // 移除下载通知
                    NotificationManager.cancel(
                        this@SettingActivity,
                        NotificationManager.NOTIFY_ID_UPDATE
                    )
                    // 安装APK
                    AppUtil.installApk(
                        this@SettingActivity,
                        "${this@SettingActivity.packageName}.provider",
                        it
                    )
                }
                .launchIn(this)

            settingViewModel.cacheSize
                .doOnSuccess {
                    binding.siClearCache.message = FileUtil.getFormatSize(it.toDouble())
                }
                .onEach {
                    loadingDialog.dismiss()
                }
                .launchIn(this)

            accountViewModel.logoutResult
                .onStart {
                    loadingDialog.dismiss()
                }
                .doOnSuccess {
                    LoginActivity.start(this@SettingActivity)
                    finish()
                }
                .launchIn(this)
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
                    loadingDialog.show()
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
                settingViewModel.downloadApk(TAG_DOWNLOAD, versionInfo)
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
