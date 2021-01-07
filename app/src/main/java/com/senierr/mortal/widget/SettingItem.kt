package com.senierr.mortal.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.senierr.base.support.ext.setGone
import com.senierr.base.support.utils.DrawableUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.LayoutSettingItemBinding

/**
 * 设置项组合控件
 *
 * @author chunjiezhou
 * @date 2020/12/31
 */
class SettingItem : LinearLayout {

    private lateinit var binding: LayoutSettingItemBinding

    // 图标是否显示
    var iconEnabled = false
        set(value) {
            field = value
            binding.ivIcon.setGone(!field)
        }
    // 图标显示内容
    var icon = -1
        set(value) {
            field = value
            binding.ivIcon.setImageResource(field)
        }
    // 图标着色
    var iconTint = Color.BLACK
        set(value) {
            field = value
            DrawableUtil.tintDrawable(binding.ivIcon.drawable, field)
        }
    // 标题文本
    var title: String? = null
        set(value) {
            field = value
            binding.tvTitle.text = field
        }
    // 信息文本
    var message: String? = null
        set(value) {
            field = value
            binding.tvMessage.text = field
        }
    // 开关是否显示
    var switchEnabled = false
        set(value) {
            field = value
            binding.btnSwitch.setGone(!field)
        }
    // 开关是否打开
    var switchChecked = false
        set(value) {
            field = value
            binding.btnSwitch.isChecked = field
        }
    // 箭头是否显示
    var chevronEnabled = false
        set(value) {
            field = value
            binding.ivChevron.setGone(!field)
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr, 0) {
        initView()
        attrs?.let { retrieveAttributes(attrs) }
    }

    /**
     * 加载控件属性
     */
    private fun retrieveAttributes(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SettingItem)
        iconEnabled = a.getBoolean(R.styleable.SettingItem_si_icon_enabled, true)
        icon = a.getResourceId(R.styleable.SettingItem_si_icon, R.drawable.ic_notifications)
        iconTint = a.getColor(R.styleable.SettingItem_si_icon_tint, Color.BLACK)
        title = a.getString(R.styleable.SettingItem_si_title) ?: context.getString(R.string.setting)
        message = a.getString(R.styleable.SettingItem_si_message)
        switchEnabled = a.getBoolean(R.styleable.SettingItem_si_switch_enabled, false)
        switchChecked = a.getBoolean(R.styleable.SettingItem_si_switch_checked, false)
        chevronEnabled = a.getBoolean(R.styleable.SettingItem_si_chevron_enabled, false)
        a.recycle()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_setting_item, this)
        binding = LayoutSettingItemBinding.bind(this)
    }
}