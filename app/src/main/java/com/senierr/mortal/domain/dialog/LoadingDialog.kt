package com.senierr.mortal.domain.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.mortal.R

/**
 * 正在加载Dialog
 *
 * @author chunjiezhou
 * @date 2021/01/14
 */

fun createLoadingDialog(context: Context): AlertDialog {
    return MaterialAlertDialogBuilder(context)
            .setView(R.layout.layout_status_loading)
            .create()
            .apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
}