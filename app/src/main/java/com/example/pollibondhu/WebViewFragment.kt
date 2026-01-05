package com.example.pollibondhu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class WebViewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.fragment_web_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString("URL") ?: "https://google.com"
        val title = arguments?.getString("TITLE") ?: "Web"

        view.findViewById<TextView>(R.id.tvUrlTitle).text = title
        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener { findNavController().popBackStack() }

        val webView = view.findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
    }
}