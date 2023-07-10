package com.devstar.deeplinkwebview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.devstar.deeplinkwebview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = binding.webview
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressCircular.visibility = VISIBLE
                binding.inputUrl.setText(url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                if (url.contains(Constants.DEEP_LINK_PREFIX)) {
                    Log.d("MainActivity", "shouldOverrideUrlLoading: $url")
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent, null)
                    } catch (e: Exception) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(Constants.PAYTM_URL)
                        startActivity(intent, null)
                    }

                } else {
                    view?.loadUrl(url)

                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url)
                binding.progressCircular.visibility = GONE
            }
        }

        // this will load the url of the website
        webView.loadUrl(Constants.WEBVIEW_LOAD_URL)

        binding.inputUrl.setOnEditorActionListener { inputView , actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_GO){
                webView.loadUrl(inputView.text.toString())
                true
            } else false
        }
        binding.btnBack.setOnClickListener {
            webView.goBack()
        }
        binding.btnForward.setOnClickListener {
            webView.goForward()
        }

    }
}