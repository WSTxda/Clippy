package com.wstxda.clippy.tracker.utils

import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

object RedirectionHandler {
    private const val TIMEOUT_MILLIS = 3000

    fun handleRedirection(url: String): String {
        return try {
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            urlConnection.instanceFollowRedirects = false
            urlConnection.connectTimeout = TIMEOUT_MILLIS
            urlConnection.readTimeout = TIMEOUT_MILLIS
            urlConnection.connect()

            if (urlConnection.responseCode in 300..399) {
                val redirectedUrl = urlConnection.getHeaderField("Location")
                urlConnection.disconnect()
                redirectedUrl ?: url
            } else {
                urlConnection.disconnect()
                url
            }
        } catch (e: UnknownHostException) {
            url
        } catch (e: Exception) {
            url
        }
    }
}