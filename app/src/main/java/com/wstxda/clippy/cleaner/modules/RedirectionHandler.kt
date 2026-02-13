package com.wstxda.clippy.cleaner.modules

import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object RedirectionHandler {

    suspend fun process(url: String): String = withContext(Dispatchers.IO) {
        Logcat.d(Constants.REDIRECTION_HANDLER, "Processing URL: $url")

        var currentUrl = url
        var redirectCount = 0

        while (redirectCount < Constants.MAX_REDIRECTS) {
            val result = runCatching {
                val connection = createConnection(currentUrl)
                connection.use { conn ->
                    val responseCode = conn.responseCode

                    if (responseCode in 300..399) {
                        val location = conn.getHeaderField("Location")
                        if (location.isNullOrEmpty()) {
                            Logcat.w(
                                Constants.REDIRECTION_HANDLER,
                                "Redirect without Location header"
                            )
                            currentUrl
                        } else {
                            val redirectUrl = URL(conn.url, location).toString()
                            Logcat.d(
                                Constants.REDIRECTION_HANDLER,
                                "Redirect $redirectCount: $redirectUrl"
                            )
                            redirectUrl
                        }
                    } else {
                        Logcat.d(
                            Constants.REDIRECTION_HANDLER,
                            "Response code: $responseCode (not a redirect)"
                        )
                        currentUrl
                    }
                }
            }

            result.fold(onSuccess = { redirectedUrl ->
                if (redirectedUrl == currentUrl) {
                    Logcat.i(
                        Constants.REDIRECTION_HANDLER,
                        "Final URL reached after $redirectCount redirect(s)"
                    )
                    return@withContext redirectedUrl
                }
                currentUrl = redirectedUrl
                redirectCount++
            }, onFailure = { error ->
                Logcat.e(
                    Constants.REDIRECTION_HANDLER,
                    "Error resolving redirect: ${error.message}",
                    error
                )
                return@withContext currentUrl
            })
        }

        Logcat.w(
            Constants.REDIRECTION_HANDLER, "Maximum redirect limit reached ($redirectCount)"
        )
        return@withContext currentUrl
    }

    private fun createConnection(url: String): HttpURLConnection {
        return (URL(url).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = false
            connectTimeout = Constants.REDIRECTION_TIMEOUT_MILLIS
            readTimeout = Constants.REDIRECTION_TIMEOUT_MILLIS
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