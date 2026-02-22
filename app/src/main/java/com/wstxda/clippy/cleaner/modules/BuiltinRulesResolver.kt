package com.wstxda.clippy.cleaner.modules

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.data.BuiltinRulesRegex
import com.wstxda.clippy.cleaner.providers.BuiltinRulesProvider
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat

object BuiltinRulesResolver {

    fun process(url: String): String {
        Logcat.d(Constants.BUILTIN_RULES_RESOLVER, "Processing URL: $url")

        var rulesApplied = 0
        val result = BuiltinRulesProvider.builtinRuleRegexes.fold(url) { processedUrl, rule ->
            if (matchesPattern(processedUrl, rule)) {
                rulesApplied++
                Logcat.d(
                    Constants.BUILTIN_RULES_RESOLVER,
                    "Rule applied for: ${processedUrl.toUri().host}"
                )
                rule.apply(processedUrl)
            } else {
                processedUrl
            }
        }

        if (rulesApplied > 0) {
            Logcat.i(Constants.BUILTIN_RULES_RESOLVER, "Applied $rulesApplied custom rule(s)")
        } else {
            Logcat.d(Constants.BUILTIN_RULES_RESOLVER, "No custom rules applied")
        }

        return result
    }

    private fun matchesPattern(url: String, rule: BuiltinRulesRegex): Boolean {
        val uri = url.toUri()

        val hostMatches = uri.host?.matches(rule.pattern) == true
        if (!hostMatches) return false

        val pathMatches = if (rule.pathPattern == null) {
            true
        } else {
            uri.path?.matches(rule.pathPattern) == true
        }
        if (!pathMatches) return false

        val queryMatches = if (rule.queryPattern == null) {
            true
        } else {
            uri.query?.matches(rule.queryPattern) == true
        }

        return queryMatches
    }
}