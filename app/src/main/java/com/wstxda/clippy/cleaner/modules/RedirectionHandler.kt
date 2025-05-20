package com.wstxda.clippy.cleaner.modules

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

object RedirectionHandler {
    private const val DEFAULT_TIMEOUT_MILLIS = 3000

    fun resolveRedirectionParams(url: String): String {
        var connection: HttpURLConnection? = null
        try {
            connection = (URL(url).openConnection() as HttpURLConnection).apply {
                instanceFollowRedirects = false
                connectTimeout = DEFAULT_TIMEOUT_MILLIS
                readTimeout = DEFAULT_TIMEOUT_MILLIS
                connect()
            }
            val responseCode = connection.responseCode
            return when (responseCode) {
                in 300..399 -> connection.getHeaderField("Location") ?: url
                else -> url
            }
        } catch (e: Exception) {
            Log.e("RedirectionHandler", "Error resolving URL: ${e.message}", e)
            return url
        } finally {
            connection?.disconnect()
        }
    }
}