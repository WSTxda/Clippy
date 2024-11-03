package com.wstxda.clippy.cleaner.modules

import android.net.Uri
import com.wstxda.clippy.cleaner.providers.ShortenerRegexProvider

object ShortenerRemover {

    fun removeShortenerParams(
        url: String, shortenerParameters: Set<String> = emptySet()
    ): String {
        val uri = Uri.parse(url) ?: return url

        val cleanUri = uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { queryParam ->
                queryParam !in shortenerParameters && !isShortenerParameter(
                    queryParam
                )
            }.forEach { queryParam ->
                uri.getQueryParameter(queryParam)?.let { value ->
                    appendQueryParameter(queryParam, value)
                }
            }
        }.build().toString()

        return uri.fragment?.let { "$cleanUri#$it" } ?: cleanUri
    }

    private fun isShortenerParameter(param: String): Boolean {
        return ShortenerRegexProvider.shortenerRegexes.any { it.matches(param) }
    }
}