package com.wstxda.clippy.tracker.utils

import android.net.Uri

object ShortenerRemover {
    val shortenerRegexes = listOf(
        Regex("trk_\\w+", RegexOption.IGNORE_CASE),
        Regex("aff\\w+", RegexOption.IGNORE_CASE),
        Regex("(src|source)\\d*", RegexOption.IGNORE_CASE),
        Regex("(ad|advertising|ads)_\\w+", RegexOption.IGNORE_CASE),
        Regex("session([_\\-])id", RegexOption.IGNORE_CASE),
        Regex("sid", RegexOption.IGNORE_CASE),
        Regex("(clid|irclickid|click_id|clickid)", RegexOption.IGNORE_CASE),
        Regex("utm_\\w+", RegexOption.IGNORE_CASE),
        Regex("ref", RegexOption.IGNORE_CASE),
        Regex("si", RegexOption.IGNORE_CASE),
        Regex("campaign[_-]?id", RegexOption.IGNORE_CASE),
        Regex("promo[_-]?code", RegexOption.IGNORE_CASE),
        Regex("ad_id", RegexOption.IGNORE_CASE),
        Regex("ad_group_id", RegexOption.IGNORE_CASE),
        Regex("fbclid", RegexOption.IGNORE_CASE),
        Regex("gclid", RegexOption.IGNORE_CASE),
        Regex("msclkid", RegexOption.IGNORE_CASE),
        Regex("referrer", RegexOption.IGNORE_CASE),
        Regex("session_id", RegexOption.IGNORE_CASE),
        Regex("visitor_id", RegexOption.IGNORE_CASE),
        Regex("user_id", RegexOption.IGNORE_CASE),
        Regex("click_id", RegexOption.IGNORE_CASE),
        Regex("tracking_id", RegexOption.IGNORE_CASE),
        Regex("full_url", RegexOption.IGNORE_CASE),
        Regex("fallback_url", RegexOption.IGNORE_CASE),
        Regex("feature", RegexOption.IGNORE_CASE)
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