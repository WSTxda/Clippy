package com.wstxda.clippy.cleaner.modules

import android.util.Log
import com.wstxda.clippy.cleaner.modules.utils.UrlConnectionManager

object RedirectionHandler {
    suspend fun resolveRedirectionParams(url: String): String {
        return try {
            val (responseCode, redirectLocation) = UrlConnectionManager.connect(url)
            when (responseCode) {
                in 300..399 -> redirectLocation ?: url
                else -> url
            }
        } catch (e: Exception) {
            Log.e("RedirectionHandler", "Error resolving URL: ${e.message}", e)
            url
        }
    }
}