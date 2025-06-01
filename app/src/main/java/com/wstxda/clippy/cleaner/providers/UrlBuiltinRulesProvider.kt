package com.wstxda.clippy.cleaner.providers

import com.wstxda.clippy.cleaner.modules.utils.BuiltinRulesData
import androidx.core.net.toUri

object UrlBuiltinRulesProvider {
    val builtinRulesData: List<BuiltinRulesData> = listOf(
        BuiltinRulesData(
            pattern = Regex("www\\.douban\\.com"),
            pathPattern = Regex("/link2/"),
            queryPattern = Regex(".*\\burl=.+"),
            apply = { url -> extractParameter(url, "url") ?: url }
        ),
        BuiltinRulesData(
            pattern = Regex("link\\.zhihu\\.com"),
            queryPattern = Regex(".*\\btarget=.+"),
            apply = { url -> extractParameter(url, "target") ?: url }
        ),
        BuiltinRulesData(
            pattern = Regex(".+\\.smzdm\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("(.+\\.stackexchange|askubuntu|serverfault|stackoverflow|superuser)\\.com"),
            pathPattern = Regex("/[aq]/[0-9]+/[0-9]+/?"),
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
            apply = { url -> retainParameters(url, "si") }
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
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.amazon\\.com\\.br"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("shopee\\.com\\.br"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("br\\.temu\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.twitter\\.com|x\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.whatsapp\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.netflix\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.discord\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.ebay\\.com"),
            apply = { url -> retainParameters(url, "id|item_id") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.tripadvisor\\.com"),
            apply = { url -> retainParameters(url, "ref|adid") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.wikipedia\\.org"),
            apply = { url -> retainParameters(url, "oldid|diff") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.yelp\\.com"),
            apply = { url -> retainParameters(url, "ref") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.soundcloud\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.coursera\\.org"),
            apply = { url -> retainParameters(url, "utm_source|utm_medium|utm_campaign") }
        ),
        BuiltinRulesData(
            pattern = Regex("www\\.tumblr\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("medium\\.com"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("sourceforge\\.net"),
            pathPattern = Regex("/projects/.+/files/.+/download/?"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("(www\\.)?(bbc\\.com|bbc\\.co\\.uk)"),
            apply = ::clearQuery
        ),
        BuiltinRulesData(
            pattern = Regex("imgur\\.com"),
            apply = ::clearQuery
        )
    )

    private fun extractParameter(url: String, paramName: String): String? {
        return url.toUri().getQueryParameter(paramName)
    }

    private fun clearQuery(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().scheme(uri.scheme).authority(uri.authority)
            .path(uri.path).build().toString()
    }

    private fun retainParameters(url: String, regexPattern: String): String {
        val uri = url.toUri()
        val regex = Regex(regexPattern)
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { regex.matches(it) }
                .forEach { param -> appendQueryParameter(param, uri.getQueryParameter(param)) }
        }.scheme(uri.scheme).authority(uri.authority).path(uri.path).build().toString()
    }

    private fun clearTrailingId(url: String): String {
        val uri = url.toUri()
        val modifiedPath = uri.path?.replace("/[0-9]+/?$".toRegex(), "") ?: uri.path
        return uri.buildUpon().path(modifiedPath).scheme(uri.scheme).authority(uri.authority)
            .build().toString()
    }
}