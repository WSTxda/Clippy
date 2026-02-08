package com.wstxda.clippy.cleaner.modules

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.data.BuiltinRulesRegex
import com.wstxda.clippy.cleaner.providers.UrlBuiltinRulesProvider

object BuiltinRulesResolver {

    fun applyBuiltinRules(url: String): String {
        return UrlBuiltinRulesProvider.builtinRuleRegexes.fold(url) { processedUrl, rule ->
            if (matchesPattern(processedUrl, rule)) {
                rule.apply(processedUrl)
            } else {
                processedUrl
            }
        }
    }

    private fun matchesPattern(url: String, rule: BuiltinRulesRegex): Boolean {
        val uri = url.toUri()
        val hostMatches = uri.host?.matches(rule.pattern) == true
        val pathMatches = rule.pathPattern?.let { uri.path?.matches(it) } != false
        val queryMatches = rule.queryPattern?.let { uri.query?.matches(it) } != false
        return hostMatches && pathMatches && queryMatches
    }
}