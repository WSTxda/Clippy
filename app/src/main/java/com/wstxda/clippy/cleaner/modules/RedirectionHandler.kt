package com.wstxda.clippy.cleaner.modules

import android.util.Log
import com.wstxda.clippy.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object RedirectionHandler {

    suspend fun resolveRedirects(initialUrl: String): String = withContext(Dispatchers.IO) {
        var currentUrl = initialUrl
        var redirectCount = 0

        while (redirectCount < Constants.MAX_REDIRECTS) {
            val result = runCatching {
                val connection = createConnection(currentUrl)
                connection.use { conn ->
                    val responseCode = conn.responseCode
                    if (responseCode in 300..399) {
                        val location = conn.getHeaderField("Location")
                        if (location.isNullOrEmpty()) {
                            currentUrl
                        } else {
                            URL(conn.url, location).toString()
                        }
                    } else {
                        currentUrl
                    }
                }
            }

            result.fold(onSuccess = { redirectedUrl ->
                if (redirectedUrl == currentUrl) return@withContext redirectedUrl
                currentUrl = redirectedUrl
                redirectCount++
            }, onFailure = { error ->
                Log.e(
                    Constants.REDIRECTION_HANDLER, "Error resolving URL: ${error.message}", error
                )
                return@withContext currentUrl
            })
        }

        return@withContext currentUrl
    }

    private fun createConnection(url: String): HttpURLConnection {
        return (URL(url).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = false
            connectTimeout = Constants.DEFAULT_TIMEOUT_MILLIS
            readTimeout = Constants.DEFAULT_TIMEOUT_MILLIS
            connect()
        }
    }

    private inline fun <T> HttpURLConnection.use(block: (HttpURLConnection) -> T): T {
        return try {
            block(this)
        } finally {
            disconnect()
        }
    }
}