package com.senierr.mortal.domain.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import com.senierr.mortal.support.ext.click
import com.senierr.mortal.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityAboutBinding
import com.senierr.mortal.domain.ui.common.WebViewActivity
import com.senierr.mortal.support.ext.showToast

/**
 * 关于页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class AboutActivity : BaseActivity<ActivityAboutBinding>() {

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityAboutBinding {
        return ActivityAboutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.siProjectHomePage.click {
            WebViewActivity.start(this, "https://github.com/senierr/Mortal")
        }

        binding.siUserAgreement.click {
            WebViewActivity.start(this, "https://github.com/senierr/Mortal/blob/master/LICENSE")
        }

        binding.siPrivacyAndPolicy.click {
            showToast(R.string.none)
        }
    }
}
