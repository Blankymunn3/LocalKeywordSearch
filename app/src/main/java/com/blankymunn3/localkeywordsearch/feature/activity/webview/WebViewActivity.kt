package com.blankymunn3.localkeywordsearch.feature.activity.webview

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.blankymunn3.localkeywordsearch.R
import com.blankymunn3.localkeywordsearch.databinding.ActivityWebviewBinding
import com.blankymunn3.localkeywordsearch.util.BaseActivity
import com.blankymunn3.localkeywordsearch.util.GetViewModel

class WebViewActivity: BaseActivity() {
    val binding by binding<ActivityWebviewBinding>(R.layout.activity_webview)
    val viewModel by GetViewModel(WebViewViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@WebViewActivity


        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbWebView.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.webView.webViewClient = WebViewClient()
        viewModel.title.postValue(intent.getStringExtra("EXTRA_TITLE"))
        binding.webView.loadUrl(intent.getStringExtra("EXTRA_URL")!!)

        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.defaultZoom = WebSettings.ZoomDensity.FAR
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.domStorageEnabled = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.hold, R.anim.slide_right)
    }
}