package com.wstxda.clippy.cleaner.modules

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.providers.ShortenerRegexProvider
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat

object ShortenerRemover {

    fun process(url: String): String {
        Logcat.d(Constants.SHORTENER_REMOVER, "Processing URL: $url")

        val uri = url.toUri()
        val initialParams = uri.queryParameterNames.size

        val cleanUri = uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filterNot { param ->
                ShortenerRegexProvider.shortenerRegexes.any { it.matches(param) }
            }.forEach { param ->
                uri.getQueryParameter(param)?.let { value ->
                    appendQueryParameter(param, value)
                }
            }
        }.build().toString()

        // Preserve fragment
        val fragment = uri.fragment
        val result = if (fragment != null && !cleanUri.contains("#$fragment")) {
            "$cleanUri#$fragment"
        } else {
            cleanUri
        }

        val finalParams = result.toUri().queryParameterNames.size
        val removed = initialParams - finalParams

        if (removed > 0) {
            Logcat.i(
                Constants.SHORTENER_REMOVER, "Removed $removed shortener parameter(s)"
            )
        } else {
            Logcat.d(Constants.SHORTENER_REMOVER, "No shortener parameters found")
        }

        return result
    }
}