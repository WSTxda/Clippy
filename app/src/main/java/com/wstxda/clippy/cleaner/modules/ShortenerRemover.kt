package com.wstxda.clippy.cleaner.modules

import android.net.Uri
import com.wstxda.clippy.cleaner.provider.ShortenerRegexProvider

object ShortenerRemover {

    fun removeShortenerParams(
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
        return ShortenerRegexProvider.shortenerRegexes.any { it.matches(param) }
    }
}
