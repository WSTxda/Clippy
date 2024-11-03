package com.wstxda.clippy.cleaner.modules

import java.net.URL
import android.util.Log
import com.wstxda.clippy.cleaner.modules.utils.UrlConnectionManager

object RedirectionHandler {
    const val TIMEOUT_MILLIS = 3000

    fun resolveRedirectionParams(url: String): String {
        return try {
            val responseCode = UrlConnectionManager.connect(url)
            when (responseCode) {
                in 300..399 -> {
                    UrlConnectionManager.getRedirectLocation()?.let { location ->
                        if (isValidUrl(location)) {
                            location
                        } else {
                            Log.e("RedirectionHandler", "Invalid redirection URL: $location")
                            url
                        }
                    } ?: url
                }

                else -> url
            }
        } catch (e: Exception) {
            Log.e("RedirectionHandler", "Error resolving URL: ${e.message}", e)
            url
        } finally {
            UrlConnectionManager.disconnect()
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url).toURI()
            true
        } catch (e: Exception) {
            false
        }
    }
}