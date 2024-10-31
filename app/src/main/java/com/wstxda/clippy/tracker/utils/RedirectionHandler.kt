package com.wstxda.clippy.tracker.utils

import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

object RedirectionHandler {
    private const val TIMEOUT_MILLIS = 3000

    fun handleRedirection(url: String): String {
        return try {
            var currentUrl = url
            while (true) {
                val connection = URL(currentUrl).openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
                connection.connectTimeout = TIMEOUT_MILLIS
                connection.readTimeout = TIMEOUT_MILLIS
                connection.connect()

                if (connection.responseCode in 300..399) {
                    val location = connection.getHeaderField("Location") ?: break
                    connection.disconnect()
                    currentUrl = location
                } else {
                    connection.disconnect()
                    break
                }
            }
            currentUrl
        } catch (e: UnknownHostException) {
            url
        } catch (e: Exception) {
            url
        }
    }
}