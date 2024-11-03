package com.wstxda.clippy.cleaner.modules.utils

import com.wstxda.clippy.cleaner.modules.RedirectionHandler
import java.net.HttpURLConnection
import java.net.URL

object UrlConnectionManager {
    private var urlConnection: HttpURLConnection? = null

    fun connect(url: String): Int {
        urlConnection = (URL(url).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = false
            connectTimeout = RedirectionHandler.TIMEOUT_MILLIS
            readTimeout = RedirectionHandler.TIMEOUT_MILLIS
            connect()
        }
        return urlConnection!!.responseCode
    }

    fun getRedirectLocation(): String? {
        return urlConnection?.getHeaderField("Location")
    }

    fun disconnect() {
        urlConnection?.disconnect()
    }
}