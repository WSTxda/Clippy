package com.wstxda.clippy.cleaner.modules

import com.wstxda.clippy.cleaner.modules.utils.BuiltinRulesData
import com.wstxda.clippy.cleaner.providers.UrlBuiltinRulesProvider
import androidx.core.net.toUri

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
        val uri = url.toUri()
        val hostMatches = uri.host?.matches(rule.pattern) == true
        val pathMatches = rule.pathPattern?.let { uri.path?.matches(it) } != false
        val queryMatches = rule.queryPattern?.let { uri.query?.matches(it) } != false
        return hostMatches && pathMatches && queryMatches
    }
}