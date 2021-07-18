package com.github.snowdream.apps.gitbook

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

/**
 * Created by yanghui.yangh on 2016/3/4.
 */
class MainFragment : Fragment() {
    private var mWebView: WebView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mWebView = view!!.findViewById(R.id.webview) as WebView
        mWebView!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                onBackPressed()
                return@OnKeyListener true
            }
            false
        })

        val webSettings = mWebView!!.settings
        webSettings.allowFileAccess = true
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        mWebView!!.setWebViewClient(WebViewClient())
        mWebView!!.loadUrl("file:///android_asset/book/index.html")


        val mAdView = view.findViewById(R.id.adView) as AdView
        val adRequest = AdRequest.Builder().build()

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mAdView.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                mAdView.visibility = View.GONE
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the application after tapping on an ad.
            }
        }

        mAdView.loadAd(adRequest)
    }


    fun onBackPressed() {
        if (mWebView!!.canGoBack()) {
            mWebView!!.goBack()
        } else {
            if (!fragmentManager.popBackStackImmediate()) {
                activity.supportFinishAfterTransition()
            }
        }
    }
}
