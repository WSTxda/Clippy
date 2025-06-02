package com.wstxda.clippy.cleaner.modules

import android.util.Log
import com.wstxda.clippy.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object RedirectionHandler {

    suspend fun resolveRedirectionParams(initialUrl: String): String = withContext(Dispatchers.IO) {
        var url = initialUrl
        var redirects = 0

        while (redirects < Constants.MAX_REDIRECTS) {
            val result = runCatching {
                (URL(url).openConnection() as HttpURLConnection).apply {
                    instanceFollowRedirects = false
                    connectTimeout = Constants.DEFAULT_TIMEOUT_MILLIS
                    readTimeout = Constants.DEFAULT_TIMEOUT_MILLIS
                    connect()
                }.use { connection ->
                    val responseCode = connection.responseCode
                    if (responseCode in 300..399) {
                        connection.getHeaderField("Location") ?: return@runCatching url
                    } else {
                        return@runCatching url
                    }
                }
            }

            result.fold(onSuccess = { redirectedUrl ->
                if (redirectedUrl == url) return@withContext redirectedUrl
                url = redirectedUrl
                redirects++
            }, onFailure = { e ->
                Log.e(Constants.REDIRECTION_HANDLER, "Error resolving URL: ${e.message}", e)
                return@withContext url
            })
        }

        return@withContext url
    }

    private inline fun <T> HttpURLConnection.use(block: (HttpURLConnection) -> T): T {
        return try {
            block(this)
        } finally {
            disconnect()
        }
    }
}