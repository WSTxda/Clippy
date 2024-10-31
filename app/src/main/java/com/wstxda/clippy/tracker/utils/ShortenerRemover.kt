package com.wstxda.clippy.tracker.utils

import android.net.Uri

object ShortenerRemover {
    val shortenerRegexes = listOf(
        Regex("trk_\\w+", RegexOption.IGNORE_CASE),
        Regex("aff\\w+", RegexOption.IGNORE_CASE),
        Regex("(src|source|utm)_\\w*", RegexOption.IGNORE_CASE),
        Regex("(ad|ads|advertising)_?\\w*", RegexOption.IGNORE_CASE),
        Regex("(session|visitor|user|click|tracking|promo|campaign|full|fallback)_?(id|code|url)?", RegexOption.IGNORE_CASE),
        Regex("fbclid|gclid|msclkid|irclickid|clid", RegexOption.IGNORE_CASE),
        Regex("ref|referrer", RegexOption.IGNORE_CASE),
        Regex("si|feature|_t|_r", RegexOption.IGNORE_CASE)
    )

    fun removeShortenerParamsFromUrl(
        url: String, shortenerParameters: Set<String> = emptySet()
    ): String {
        val uri = Uri.parse(url)
        val cleanUriBuilder = uri.buildUpon().clearQuery()

        uri.queryParameterNames.forEach { queryParam ->
            if (queryParam !in shortenerParameters && !isShortenerParameter(queryParam)) {
                uri.getQueryParameter(queryParam)?.let { value ->
                    cleanUriBuilder.appendQueryParameter(queryParam, value)
                }
            }
        }

        val cleanUri = cleanUriBuilder.build().toString()
        return if (uri.fragment != null) {
            "$cleanUri#${uri.fragment}"
        } else {
            cleanUri
        }
    }

    private fun isShortenerParameter(param: String): Boolean {
        return shortenerRegexes.any { it.matches(param) }
    }
}