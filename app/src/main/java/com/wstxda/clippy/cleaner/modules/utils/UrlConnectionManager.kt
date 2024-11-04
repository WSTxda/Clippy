package com.wstxda.clippy.cleaner.modules.utils

import java.net.HttpURLConnection
import java.net.URL

object UrlConnectionManager {
    private var urlConnection: HttpURLConnection? = null
    private const val DEFAULT_TIMEOUT_MILLIS = 3000

    fun connect(url: String): Int {
        urlConnection = (URL(url).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = false
            connectTimeout = DEFAULT_TIMEOUT_MILLIS
            readTimeout = DEFAULT_TIMEOUT_MILLIS
            connect()
        }
        return urlConnection?.responseCode ?: -1
    }

    fun getRedirectLocation(): String? = urlConnection?.getHeaderField("Location")

    fun disconnect() {
        urlConnection?.disconnect()
        urlConnection = null
    }
}