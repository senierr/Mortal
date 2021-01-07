package com.senierr.mortal.support.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 软键盘工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
object KeyboardUtil {

    /*
      避免输入法面板遮挡
      <p>在manifest.xml中activity中设置</p>
      <p>android:windowSoftInputMode="adjustPan"</p>
     */

    /**
     * 动态显示软键盘
     *
     * @param activity activity
     */
    fun showSoftInput(activity: Activity?) {
        if (activity == null) return
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    /**
     * 动态显示软键盘
     *
     * @param context 上下文
     * @param view 视图
     */
    fun showSoftInput(context: Context?, view: View?) {
        if (context == null || view == null) return
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    fun hideSoftInput(activity: Activity?) {
        if (activity == null) return
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 动态隐藏软键盘
     *
     * @param context 上下文
     * @param view 视图
     */
    fun hideSoftInput(context: Context?, view: View?) {
        if (context == null || view == null) return
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 切换键盘显示与否状态
     *
     * @param context 上下文
     */
    fun toggleSoftInput(context: Context?) {
        if (context == null) return
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     *
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     *
     * 需重写dispatchTouchEvent
     *
     * 参照以下注释代码
     */
    fun clickBlankArea2HideSoftInput() {
        Log.d("tips", "U should copy the following code.")
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }
        */
    }
}
