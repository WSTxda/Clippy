package com.wstxda.clippy.cleaner.tools

import android.net.Uri
import android.util.Log
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider

object UrlValidator {
    fun isValidUrl(url: String): Boolean {
        return try {
            val uri = Uri.parse(url)
            val scheme = uri.scheme
            scheme != null && UrlSchemeProvider.isSupported(scheme)
        } catch (e: Exception) {
            Log.e("UrlValidator", "Invalid URL: $url", e)
            false
        }
    }
}