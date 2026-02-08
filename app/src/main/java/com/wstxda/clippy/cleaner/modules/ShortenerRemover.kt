package com.wstxda.clippy.cleaner.modules

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.providers.ShortenerRegexProvider

object ShortenerRemover {

    fun removeShortenerParams(url: String): String {
        val uri = url.toUri()
        val cleanUri = uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filterNot { param ->
                ShortenerRegexProvider.shortenerRegexes.any { it.matches(param) }
            }.forEach { param ->
                uri.getQueryParameter(param)?.let { value ->
                    appendQueryParameter(param, value)
                }
            }
        }.build().toString()

        val fragment = uri.fragment
        return if (fragment != null && !cleanUri.contains("#$fragment")) {
            "$cleanUri#$fragment"
        } else {
            cleanUri
        }
    }
}