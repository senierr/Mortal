package com.senierr.mortal.domain.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.LogUtil
import com.senierr.base.support.utils.NetworkUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityWebviewBinding

/**
 * WebView容器
 *
 * @author zhouchunjie
 * @date 2019/6/12 11:38
 */
class WebViewActivity : BaseActivity() {

    companion object {
        private const val TAG_LOG = "WebViewActivity"

        private const val KEY_URL = "url"
        private const val KEY_TITLE = "title"
        private const val KEY_ALLOW_FILE_ACCESS = "allow_file_access"

        /**
         * 启动入口
         *
         * @param url 加载的链接地址
         * @param title 显示标题，否则显示网页标题
         * @param allowFileAccess 允许文件访问权限，谨慎使用
         */
        fun start(context: Context, url: String, title: String? = null, allowFileAccess: Boolean = false) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_TITLE, title)
            intent.putExtra(KEY_ALLOW_FILE_ACCESS, allowFileAccess)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityWebviewBinding

    private var originalUrl: String? = null
    private var title: String? = null
    private var allowFileAccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initParams()
        initView()

        originalUrl?.let { binding.wvWeb.loadUrl(it) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        binding.wvWeb.settings?.javaScriptEnabled = true
    }

    override fun onStop() {
        super.onStop()
        binding.wvWeb.settings?.javaScriptEnabled = false
    }

    override fun onDestroy() {
        binding.wvWeb.parent?.let {
            if (it is ViewGroup) {
                it.removeView(binding.wvWeb)
            }
        }
        binding.wvWeb.stopLoading()
        binding.wvWeb.settings?.javaScriptEnabled = false
        binding.wvWeb.clearHistory()
        binding.wvWeb.removeAllViews()
        binding.wvWeb.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (binding.wvWeb.canGoBack()) {
            binding.wvWeb.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun initParams() {
        originalUrl = intent.getStringExtra(KEY_URL)
        title = intent.getStringExtra(KEY_TITLE)
        allowFileAccess = intent.getBooleanExtra(KEY_ALLOW_FILE_ACCESS, false)
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }
        // 设置标题
        title?.let { binding.tbTop.title = it }
        // 初始化WebView
        initWebView(binding.wvWeb)
    }

    /**
     * 初始化WebView
     */
    private fun initWebView(webView: WebView) {
        initWebViewSettings(webView)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return interceptUrl(url)
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return interceptUrl(request?.url?.toString())
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                LogUtil.logE(TAG_LOG, "onReceivedSslError: ${error?.toString()}")
                // 忽略错误，继续访问
                handler?.proceed()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                LogUtil.logE(TAG_LOG, "onReceivedError: ${error?.toString()}")
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                LogUtil.logE(TAG_LOG, "onReceivedHttpError: ${errorResponse?.toString()}")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, t: String?) {
                super.onReceivedTitle(view, t)
                // 当Title参数为空时，设置Title为网页Title
                binding.tbTop.title = title ?: t
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress in 0..95) {
                    binding.pbProgress.visibility = View.VISIBLE
                } else {
                    binding.pbProgress.visibility = View.GONE
                }
                binding.pbProgress.progress = newProgress
            }
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebViewSettings(webView: WebView) {
        val webSettings = webView.settings

        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        // 在Android 4.3版本调用WebSettings.setJavaScriptEnabled()方法时会调用一下reload方法，同时会回调多次WebChromeClient.onJsPrompt()。
        // 如果有业务逻辑依赖于这两个方法，就需要注意判断回调多次是否会带来影响了。
        webSettings.javaScriptEnabled = true

        // 将图片调整到适合webview的大小
        webSettings.useWideViewPort = true
        // 缩放至屏幕的大小
        webSettings.loadWithOverviewMode = true
        // 支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(true)
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.builtInZoomControls = false
        // 隐藏原生的缩放控件
        webSettings.displayZoomControls = false
        // 禁用文字缩放
        webSettings.textZoom = 100
        // 缓存设置
        if (NetworkUtil.isConnected(this)) {
            // 有网络，默认缓存处理：cache-control
            webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        } else {
            webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }
        // 10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024)
        // 允许缓存，默认缓存位置：
        webSettings.setAppCacheEnabled(true)

        // 设置可以访问文件，修复：域控制不严格漏洞
        // 注：这会导致无法加载本地Html文件，若想开启，在启动参数设置allowFileAccess
        webSettings.allowFileAccess = allowFileAccess
        webSettings.allowFileAccessFromFileURLs = false
        webSettings.allowUniversalAccessFromFileURLs = false

        // 支持通过JS打开新窗口
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        // 支持自动加载图片
        webSettings.loadsImagesAutomatically = true
        // 设置编码格式
        webSettings.defaultTextEncodingName = "utf-8"
        // 支持H5本地存储
        webSettings.domStorageEnabled = true

        // 不保存密码，修复：密码明文存储漏洞
        webSettings.savePassword = false

        //Android 5.0及以上版本使用WebView不能存储第三方Cookies解决方案
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
            // 5.1以上默认禁止了https和http混用，以下方式是开启
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // 3.0 ~ 4.2 之间的版本需要移除Google注入的几个对象，修复：接口引起远程代码执行漏洞
        try {
            if (Build.VERSION.SDK_INT in 11..16) {
                webView.removeJavascriptInterface("searchBoxJavaBridge_")
                webView.removeJavascriptInterface("accessibility")
                webView.removeJavascriptInterface("accessibilityTraversal")
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 拦截并处理特殊Url
     */
    private fun interceptUrl(url: String?): Boolean {
        if (url == null) return false
        try {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                // 交由WebView处理
                return false
            } else if (url.startsWith("mailto://") || url.startsWith("tel://")) {
                // 内部URL
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                // 其他特殊URL
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
}