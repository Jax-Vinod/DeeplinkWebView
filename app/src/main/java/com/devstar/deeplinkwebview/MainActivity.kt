package com.devstar.deeplinkwebview

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings.LOAD_NO_CACHE
import android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import com.devstar.deeplinkwebview.ui.theme.DeeplinkWebViewTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeeplinkWebViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FUllWebView()
                }
            }
        }
    }
}

@Composable
fun FUllWebView() {
    // Declare a string that contains a url
    // Adding a WebView inside AndroidView
    // with layout as full screen
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.loadsImagesAutomatically = true
            settings.javaScriptEnabled = true
            settings.cacheMode = LOAD_NO_CACHE
            settings.builtInZoomControls = true
            settings.displayZoomControls = true
            settings.domStorageEnabled = true
            settings.mixedContentMode = MIXED_CONTENT_COMPATIBILITY_MODE
            loadUrl(Constants.WEBVIEW_LOAD_URL)

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (url.contains(Constants.DEEP_LINK_PREFIX)) {
                        Log.d("MainActivity", "shouldOverrideUrlLoading: $url")
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            startActivity(context, intent, null)
                        } catch (e: Exception) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(Constants.PAYTM_URL)
                            startActivity(context, intent, null)
                        }

                    } else {
                        view?.loadUrl(url)
                    }
                    return true
                }
            }
        }
    }, update = {
        it.loadUrl(Constants.WEBVIEW_LOAD_URL)
    })
}

@Preview(showBackground = true)
@Composable
fun FUllWebViewPreview() {
    DeeplinkWebViewTheme {
        FUllWebView()
    }
}
