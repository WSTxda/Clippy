package com.wstxda.clippy.cleaner.providers

import android.net.Uri
import com.wstxda.clippy.cleaner.modules.utils.BuiltinRulesData

object UrlBuiltinRulesProvider {

    val builtinRulesData: List<BuiltinRulesData> = listOf(
        BuiltinRulesData(
            pattern = Regex("www\\.douban\\.com"),
            pathPattern = "/link2/",
            queryPattern = ".*\\burl=.+",
            apply = { url -> extractParameter(url, "url") ?: url }
        ),
        BuiltinRulesData(
            pattern = Regex("link\\.zhihu\\.com"),
            queryPattern = ".*\\btarget=.+",
            apply = { url -> extractParameter(url, "target") ?: url }
        ),
        BuiltinRulesData(
            pattern = Regex(".+\\.smzdm\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("(.+\\.stackexchange|askubuntu|serverfault|stackoverflow|superuser)\\.com"),
            pathPattern = "/[aq]/[0-9]+/[0-9]+/?",
            apply = { url -> clearTrailingId(url) }
        ),
        BuiltinRulesData(
            pattern = Regex(".+\\.(taobao|tmall)\\.com"),
            apply = { url -> retainParameters(url, "id") }
        ),
        BuiltinRulesData(
            pattern = Regex(".+\\.tiktok\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("(twitter|x)\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex(".+\\.xiaohongshu\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("youtu\\.be|((www|music)\\.)?youtube\\.com"),
            apply = { url -> retainParameters(url, "index|list|t|v") }
        ),
        BuiltinRulesData(
            pattern = Regex("open\\.spotify\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("bit\\.ly|tinyurl\\.com|goo\\.gl|ow\\.ly|rebrand\\.ly|snip\\.ly"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.facebook\\.com|m\\.facebook\\.com"),
            apply = { url -> retainParameters(url, "fbclid|refid|source") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.instagram\\.com"),
            apply = { url -> retainParameters(url, "igshid|utm_source|utm_medium") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.linkedin\\.com"),
            apply = { url -> retainParameters(url, "trk") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.pinterest\\.com|api\\.pinterest\\.com"),
            apply = { url -> retainParameters(url, "utm_source|utm_medium|utm_campaign|pin") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.reddit\\.com"),
            apply = { url -> retainParameters(url, "utm_source|utm_medium|utm_campaign") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.quora\\.com"),
            apply = { url -> retainParameters(url, "utm_source|utm_medium|utm_campaign") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.aliexpress\\.com|m\\.aliexpress\\.com"),
            apply = ::clearQuery
        )
    )

    private fun extractParameter(url: String, paramName: String): String? {
        return Uri.parse(url).getQueryParameter(paramName)
    }

    private fun clearQuery(url: String): String {
        return Uri.parse(url).buildUpon().clearQuery().build().toString()
    }

    private fun retainParameters(url: String, regexPattern: String): String {
        val uri = Uri.parse(url)
        val regex = Regex(regexPattern)

        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames
                .filter { regex.matches(it) }
                .forEach { param -> appendQueryParameter(param, uri.getQueryParameter(param)) }
        }.build().toString()
    }

    private fun clearTrailingId(url: String): String {
        val uri = Uri.parse(url)
        val modifiedPath = uri.path?.replace("/[0-9]+/?$".toRegex(), "") ?: uri.path
        return uri.buildUpon().path(modifiedPath).build().toString()
    }
}