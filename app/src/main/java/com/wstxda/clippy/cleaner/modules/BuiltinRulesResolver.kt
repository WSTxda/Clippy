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
}