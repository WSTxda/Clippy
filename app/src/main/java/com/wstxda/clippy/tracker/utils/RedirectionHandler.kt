package com.wstxda.clippy.tracker.utils

import android.net.Uri
import com.wstxda.clippy.tracker.provider.ShortenersDomainsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

object RedirectionHandler {
    private val client = OkHttpClient()

    suspend fun handleRedirection(url: String): String {
        return withContext(Dispatchers.IO) {
            if (isRedirectionDomain(url)) {
                extractRedirectionTarget(url)
            } else {
                url
            }
        }
    }

    private fun isRedirectionDomain(url: String): Boolean {
        val uri = Uri.parse(url)
        val host = uri.host ?: return false
        return ShortenersDomainsProvider.getShortenersDomains().contains(host)
    }

    private suspend fun extractRedirectionTarget(url: String): String {
        val uri = Uri.parse(url)
        return uri.getQueryParameter("url") ?: uri.getQueryParameter("u") ?: getOriginalUrl(url)
    }

    private suspend fun getOriginalUrl(shortUrl: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(shortUrl).head().build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        response.request.url.toString()
                    } else {
                        shortUrl
                    }
                }
            } catch (e: Exception) {
                shortUrl
            }
        }
    }
}