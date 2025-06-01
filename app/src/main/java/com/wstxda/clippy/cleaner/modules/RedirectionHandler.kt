package com.wstxda.clippy.cleaner.modules

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

object RedirectionHandler {
    private const val DEFAULT_TIMEOUT_MILLIS = 3000
    private const val MAX_REDIRECTS = 3
    private const val TAG = "RedirectionHandler"

    fun resolveRedirectionParams(initialUrl: String): String {
        var url = initialUrl
        var redirects = 0

        while (redirects < MAX_REDIRECTS) {
            val result = runCatching {
                (URL(url).openConnection() as HttpURLConnection).apply {
                    instanceFollowRedirects = false
                    connectTimeout = DEFAULT_TIMEOUT_MILLIS
                    readTimeout = DEFAULT_TIMEOUT_MILLIS
                    connect()
                }.use { connection ->
                    val responseCode = connection.responseCode
                    if (responseCode in 300..399) {
                        connection.getHeaderField("Location") ?: return url
                    } else {
                        return url
                    }
                }
            }

            result.fold(onSuccess = { redirectedUrl ->
                if (redirectedUrl == url) return redirectedUrl
                url = redirectedUrl
                redirects++
            }, onFailure = { e ->
                Log.e(TAG, "Error resolving URL: ${e.message}", e)
                return url
            })
        }

        return url
    }

    private inline fun <T> HttpURLConnection.use(block: (HttpURLConnection) -> T): T {
        return try {
            block(this)
        } finally {
            disconnect()
        }
    }
}