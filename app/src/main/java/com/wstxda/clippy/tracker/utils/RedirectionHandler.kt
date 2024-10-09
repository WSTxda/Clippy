package com.wstxda.clippy.tracker.utils

import java.net.HttpURLConnection
import java.net.URL

object RedirectionHandler {
    fun handleRedirection(url: String): String {
        val urlConnection = URL(url).openConnection() as HttpURLConnection
        urlConnection.instanceFollowRedirects = false
        urlConnection.connect()

        return if (urlConnection.responseCode in 300..399) {
            val redirectedUrl = urlConnection.getHeaderField("Location")
            urlConnection.disconnect()
            redirectedUrl ?: url
        } else {
            urlConnection.disconnect()
            url
        }
    }
}