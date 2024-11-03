package com.wstxda.clippy.cleaner.modules

import android.net.Uri
import com.wstxda.clippy.cleaner.modules.utils.BuiltinRulesData
import com.wstxda.clippy.cleaner.providers.UrlBuiltinRulesProvider

object BuiltinRulesResolver {

    fun applyBuiltinRules(url: String): String {
        return UrlBuiltinRulesProvider.builtinRulesData.fold(url) { processedUrl, rule ->
            if (matchesPattern(processedUrl, rule)) {
                rule.apply(processedUrl)
            } else {
                processedUrl
            }
        }
    }

    private fun matchesPattern(url: String, rule: BuiltinRulesData): Boolean {
        val uri = Uri.parse(url)
        val hostMatches = uri.host?.matches(rule.pattern) ?: false
        val pathMatches = rule.pathPattern?.let { uri.path?.matches(Regex(it)) } ?: true
        val queryMatches = rule.queryPattern?.let { uri.query?.matches(Regex(it)) } ?: true

        return hostMatches && pathMatches && queryMatches
    }

    fun extractQueryParameter(url: String, param: String): String? {
        return Uri.parse(url).getQueryParameter(param)
    }

    fun setEncodedQuery(url: String, newQuery: String?): String {
        return Uri.parse(url).buildUpon().encodedQuery(newQuery).build().toString()
    }

    fun clearQuery(url: String): String {
        return setEncodedQuery(url, null)
    }

    fun retainSpecificQueryParameters(url: String, paramsPattern: String): String {
        val uri = Uri.parse(url)
        val newBuilder = uri.buildUpon().clearQuery()

        uri.queryParameterNames.filter { it.matches(Regex(paramsPattern)) }.forEach { queryParam ->
            newBuilder.appendQueryParameter(queryParam, uri.getQueryParameter(queryParam))
        }

        return newBuilder.build().toString()
    }

    fun updatePathWithoutTrailingId(url: String, newPath: String): String {
        return Uri.parse(url).buildUpon().encodedPath(newPath).build().toString()
    }
}