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
        private const val KEY_TIPS = "key_tips"
        private const val KEY_DEFAULT_VALUE = "key_default_value"

        fun startForResult(context: FragmentActivity, requestCode: Int,
                           title: String, tips: String? = "", defaultValue: String? = ""
        ) {
            val intent = Intent(context, EditTextActivity::class.java)
            intent.putExtra(KEY_TITLE, title)
            intent.putExtra(KEY_TIPS, tips)
            intent.putExtra(KEY_DEFAULT_VALUE, defaultValue)
            context.startActivityForResult(intent, requestCode)
        }
    }

    private lateinit var title: String
    private lateinit var tips: String
    private lateinit var defaultValue: String

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
        val newValue = binding.etEdit.text.toString()
        if (newValue == defaultValue) {
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
                val newValue = binding.etEdit.text.toString()
                intent.putExtra(KEY_EDIT_TEXT, newValue)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initParams() {
        title = intent.getStringExtra(KEY_TITLE) ?: ""
        tips = intent.getStringExtra(KEY_TIPS) ?: ""
        defaultValue = intent.getStringExtra(KEY_DEFAULT_VALUE) ?: ""
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.tbTop.title = title
        binding.tvTips.text = SpannableStringBuilder(tips)
        binding.etEdit.text = SpannableStringBuilder(defaultValue)
        binding.etEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                invalidateOptionsMenu()
            }
        })
    }
}
