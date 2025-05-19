package com.wstxda.clippy.cleaner.tools

import android.util.Log
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider
import androidx.core.net.toUri

object UrlValidator {
    fun isValidUrl(url: String): Boolean {
        return try {
            val uri = url.toUri()
            val scheme = uri.scheme
            scheme != null && UrlSchemeProvider.isSupported(scheme)
        } catch (e: Exception) {
            Log.e("UrlValidator", "Invalid URL: $url", e)
            false
        }
    }
}