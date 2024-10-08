package com.wstxda.clippy.tracker.cleaner

import android.net.Uri

object UrlCleaner {
    val trackingRegexes = listOf(
        Regex("trk_\\w+", RegexOption.IGNORE_CASE),
        Regex("aff\\w+", RegexOption.IGNORE_CASE),
        Regex("(src|source)\\d*", RegexOption.IGNORE_CASE),
        Regex("(ad|advertising|ads)_\\w+", RegexOption.IGNORE_CASE),
        Regex("session([_\\-])id", RegexOption.IGNORE_CASE),
        Regex("sid", RegexOption.IGNORE_CASE),
        Regex("(clid|irclickid|click_id|clickid)", RegexOption.IGNORE_CASE),
        Regex("utm_\\w+", RegexOption.IGNORE_CASE),
        Regex("ref", RegexOption.IGNORE_CASE)
    )

    fun cleanUrlQueryParameters(url: String, trackingParameters: Set<String> = emptySet()): String {
        val uri = Uri.parse(url)
        val cleanUriBuilder = uri.buildUpon().clearQuery()

        uri.queryParameterNames.forEach { queryParam ->
            if (queryParam !in trackingParameters && !isTrackingParameter(queryParam)) {
                uri.getQueryParameter(queryParam)?.let { value ->
                    cleanUriBuilder.appendQueryParameter(queryParam, value)
                }
            }
        }

        return cleanUriBuilder.build().toString()
    }

    private fun isTrackingParameter(param: String): Boolean {
        return trackingRegexes.any { it.matches(param) }
    }
}