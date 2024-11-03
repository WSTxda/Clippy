package com.wstxda.clippy.cleaner.modules

import java.net.HttpURLConnection
import java.net.URL

object RedirectionHandler {
    private const val TIMEOUT_MILLIS = 3000

    fun resolveRedirectionParams(url: String): String {
        var urlConnection: HttpURLConnection? = null
        return try {
            urlConnection = (URL(url).openConnection() as HttpURLConnection).apply {
                instanceFollowRedirects = false
                connectTimeout = TIMEOUT_MILLIS
                readTimeout = TIMEOUT_MILLIS
                connect()
            }

            if (urlConnection.responseCode in 300..399) {
                urlConnection.getHeaderField("Location") ?: url
            } else {
                url
            }
        } catch (e: Exception) {
            url
        } finally {
            urlConnection?.disconnect()
        }
    }
}