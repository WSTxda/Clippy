package com.wstxda.clippy.cleaner.modules

import java.net.HttpURLConnection
import java.net.URL
import android.util.Log

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

            when (urlConnection.responseCode) {
                in 300..399 -> {
                    urlConnection.getHeaderField("Location")?.let { location ->
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
            urlConnection?.disconnect()
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