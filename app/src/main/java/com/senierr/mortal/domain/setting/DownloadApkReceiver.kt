package com.senierr.mortal.domain.setting

import com.senierr.base.support.utils.LogUtil
import com.senierr.repository.remote.progress.Progress
import com.senierr.repository.remote.progress.ProgressReceiver

/**
 *
 * @author chunjiezhou
 * @date 2021/01/13
 */
class DownloadApkReceiver : ProgressReceiver() {
    override fun onProgress(tag: String, progress: Progress) {
        LogUtil.logE("$tag : $progress")
    }
}