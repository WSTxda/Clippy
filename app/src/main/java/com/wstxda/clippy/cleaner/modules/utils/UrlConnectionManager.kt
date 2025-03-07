package com.wstxda.clippy.cleaner.modules.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object UrlConnectionManager {
    private val client = OkHttpClient.Builder()
        .followRedirects(false)
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    suspend fun connect(url: String): Pair<Int, String?> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            Pair(response.code, response.header("Location"))
        }
    }
}