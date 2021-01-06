package com.senierr.mortal.domain.common

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.RegexUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityEditTextBinding

/**
 * 设置页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class EditTextActivity : BaseActivity<ActivityEditTextBinding>() {

    companion object {
        const val KEY_EDIT_TEXT = "key_edit_text"

        private const val KEY_TITLE = "key_title"
        private const val KEY_DEFAULT_VALUE = "key_default_value"
        private const val KEY_HINT = "key_hint"
        private const val KEY_HELPER_TEXT = "key_helper_text"
        private const val KEY_REGEX = "key_regex"
        private const val KEY_ERROR_TEXT = "key_error_text"

        fun startForResult(context: FragmentActivity, requestCode: Int,
                           title: String? = null, defaultValue: String? = null,
                           hint: String? = null, helperText: String? = null,
                           regex: String? = null, errorText: String? = null
        ) {
            val intent = Intent(context, EditTextActivity::class.java)
            intent.putExtra(KEY_TITLE, title)
            intent.putExtra(KEY_DEFAULT_VALUE, defaultValue)
            intent.putExtra(KEY_HINT, hint)
            intent.putExtra(KEY_HELPER_TEXT, helperText)
            intent.putExtra(KEY_REGEX, regex)
            intent.putExtra(KEY_ERROR_TEXT, errorText)
            context.startActivityForResult(intent, requestCode)
        }
    }

    private var title: String? = null
    private var defaultValue: String? = null
    private var hint: String? = null
    private var helperText: String? = null
    private var regex: String? = null
    private var errorText: String? = null

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityEditTextBinding {
        return ActivityEditTextBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
        initView()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val doneMenu = menu?.findItem(R.id.tab_done)
        val newValue = binding.etText.text.toString()
        if (newValue == defaultValue || !verifyEditText()) {
            doneMenu?.isEnabled = false
            doneMenu?.icon?.setTint(getColor(R.color.btn_unable))
        } else {
            doneMenu?.isEnabled = true
            doneMenu?.icon?.setTint(getColor(R.color.black))
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_common_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_done -> {
                val newValue = binding.etText.text.toString()
                intent.putExtra(KEY_EDIT_TEXT, newValue)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initParams() {
        title = intent.getStringExtra(KEY_TITLE)
        defaultValue = intent.getStringExtra(KEY_DEFAULT_VALUE)
        hint = intent.getStringExtra(KEY_HINT)
        helperText = intent.getStringExtra(KEY_HELPER_TEXT)
        regex = intent.getStringExtra(KEY_REGEX)
        errorText = intent.getStringExtra(KEY_ERROR_TEXT)
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        if (title != null) {
            binding.tbTop.title = title
        }
        if (defaultValue != null) {
            binding.etText.text = SpannableStringBuilder(defaultValue)
        }
        binding.etText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 验证内容合法性
                if (verifyEditText()) {
                    binding.tilText.error = null
                } else {
                    binding.tilText.error = errorText
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                invalidateOptionsMenu()
            }
        })
        if (hint != null) {
            binding.tilText.hint = hint
        }
        if (helperText != null) {
            binding.tilText.helperText = helperText
        }
    }

    /**
     * 验证输入内容
     */
    private fun verifyEditText(): Boolean {
        var isMatch = true
        regex?.let {
            val value = binding.etText.text.toString()
            isMatch = RegexUtil.isMatch(it, value)
        }
        return isMatch
    }
}
