package com.senierr.mortal.ext

import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.senierr.base.support.ext.click
import com.senierr.mortal.R
import com.senierr.mortal.widget.MultiStateView

/**
 * 多状态布局扩展函数
 *
 * @author zhouchunjie
 * @date 2020/5/23 22:08
 */

/**
 * 显示加载进度
 */
fun MultiStateView.showLoadingView() {
    viewState = MultiStateView.VIEW_STATE_LOADING
}

/**
 * 显示内容
 */
fun MultiStateView.showContentView() {
    viewState = MultiStateView.VIEW_STATE_CONTENT
}

/**
 * 显示空布局
 */
fun MultiStateView.showEmptyView() {
    viewState = MultiStateView.VIEW_STATE_EMPTY
}

/**
 * 显示网络异常
 */
fun MultiStateView.showNetworkErrorView(reload: () -> Unit) {
    viewState = MultiStateView.VIEW_STATE_ERROR
    val errorView = getView(MultiStateView.VIEW_STATE_ERROR)
    val lavIcon = errorView?.findViewById<LottieAnimationView>(R.id.lav_icon)
    lavIcon?.setAnimation(R.raw.lottie_network_error)
    lavIcon?.playAnimation()
    val btnReload = errorView?.findViewById<MaterialButton>(R.id.btn_reload)
    btnReload?.click { reload.invoke() }
}
