package com.wstxda.clippy.cleaner.providers

import com.wstxda.clippy.cleaner.modules.utils.BuiltinRulesData
import androidx.core.net.toUri

object UrlBuiltinRulesProvider {
    private val trailingIdRegex = Regex("/[0-9]+/?$")

    val builtinRulesData: List<BuiltinRulesData> = listOf(

        // AliExpress
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?aliexpress\\.com$"),
            apply = ::clearQuery
        ),

        // Amazon
        BuiltinRulesData(
            pattern = Regex("^www\\.amazon\\.(com|com\\.br|ca|de|es|fr|it|co\\.uk|co\\.jp)$"),
            apply = { url -> retainParameters(url, Regex("dp|gp")) }
        ),

        // Airbnb
        BuiltinRulesData(
            pattern = Regex("^www\\.airbnb\\.com$"),
            apply = ::clearQuery
        ),

        // AskUbuntu / StackExchange network
        BuiltinRulesData(
            pattern = Regex("(.+\\.)?(stackexchange|askubuntu|serverfault|stackoverflow|superuser)\\.com$"),
            pathPattern = Regex("/[aq]/[0-9]+/[0-9]+/?"),
            apply = { url -> clearTrailingId(url) }
        ),

        // BBC
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?(bbc\\.com|bbc\\.co\\.uk)$"),
            apply = ::clearQuery
        ),

        // Bitly and other shorteners
        BuiltinRulesData(
            pattern = Regex("^(bit\\.ly|tinyurl\\.com|goo\\.gl|ow\\.ly|rebrand\\.ly|snip\\.ly|t\\.co|is\\.gd|v\\.gd|tiny\\.cc|cutt\\.ly)$"),
            apply = ::clearQuery
        ),

        // Bluesky
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?bluesky\\.app$"),
            apply = ::clearQuery
        ),

        // Booking.com
        BuiltinRulesData(
            pattern = Regex("^www\\.booking\\.com$"),
            apply = ::clearQuery
        ),

        // Canva
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?canva\\.com$"),
            apply = ::clearQuery
        ),

        // Clear redirection link - Douban
        BuiltinRulesData(
            pattern = Regex("^www\\.douban\\.com$"),
            pathPattern = Regex("/link2/"),
            queryPattern = Regex(".*\\burl=.+"),
            apply = { url -> extractParameter(url, "url") ?: url }
        ),

        // Coursera
        BuiltinRulesData(
            pattern = Regex("^www\\.coursera\\.org$"),
            apply = { url -> retainParameters(url, Regex("utm_source|utm_medium|utm_campaign")) }
        ),

        // Dailymotion
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?dailymotion\\.com$"),
            apply = ::clearQuery
        ),

        // Discord
        BuiltinRulesData(
            pattern = Regex("^www\\.discord\\.com$"),
            apply = ::clearQuery
        ),

        // eBay
        BuiltinRulesData(
            pattern = Regex("^www\\.ebay\\.com$"),
            apply = { url -> retainParameters(url, Regex("id|item_id")) }
        ),

        // Facebook
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?(facebook|m\\.facebook)\\.com$"),
            apply = { url -> retainParameters(url, Regex("^(?!fbclid|utm_).*")) }
        ),

        // Forms (Google Forms)
        BuiltinRulesData(
            pattern = Regex("^forms\\.google\\.com$"),
            apply = { url -> retainParameters(url, Regex("id|usp")) }
        ),

        // Google Docs
        BuiltinRulesData(
            pattern = Regex("^docs\\.google\\.com$"),
            apply = { url -> retainParameters(url, Regex("id|usp")) }
        ),

        // Imgur
        BuiltinRulesData(
            pattern = Regex("^imgur\\.com$"),
            apply = ::clearQuery
        ),

        // Instagram
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?instagram\\.com$"),
            apply = ::clearQuery
        ),

        // LinkedIn
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?(linkedin\\.com|lnkd\\.in)$"),
            apply = { url -> retainParameters(url, Regex("^(?!li_fat_id|trk|utm_).*")) }
        ),

        // Mastodon
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?mastodon\\..+$"),
            apply = ::clearQuery
        ),

        // Medium
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?medium\\.com$"),
            apply = ::clearQuery
        ),

        // Mercado Livre
        BuiltinRulesData(
            pattern = Regex("^www\\.mercadolivre\\.com\\.br$"),
            apply = { url -> retainParameters(url, Regex("item_id|id|oid")) }
        ),

        // Notion
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?notion\\.so$"),
            apply = ::clearQuery
        ),

        // Open Spotify
        BuiltinRulesData(
            pattern = Regex("^open\\.spotify\\.com$"),
            apply = { url -> retainParameters(url, Regex("si")) }
        ),

        // Pinterest
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?(pinterest\\.com|api\\.pinterest\\.com)$"),
            apply = { url -> retainParameters(url, Regex("utm_source|utm_medium|utm_campaign|pin")) }
        ),

        // Quora
        BuiltinRulesData(
            pattern = Regex("^www\\.quora\\.com$"),
            apply = { url -> retainParameters(url, Regex("utm_source|utm_medium|utm_campaign")) }
        ),

        // Reddit
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?(reddit\\.com|redd\\.it)$"),
            apply = { url ->
                val uri = url.toUri()
                uri.buildUpon().clearQuery().apply {
                    uri.queryParameterNames.filter { it == "context" }
                        .forEach { param -> appendQueryParameter(param, uri.getQueryParameter(param)) }
                }.scheme(uri.scheme).authority(uri.authority).path(uri.path).build().toString()
            }
        ),

        // Shein
        BuiltinRulesData(
            pattern = Regex("^www\\.shein\\.com$"),
            apply = ::clearQuery
        ),

        // Shopee
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?shopee\\.com(\\.br)?$"),
            apply = ::clearQuery
        ),

        // SoundCloud
        BuiltinRulesData(
            pattern = Regex("^www\\.soundcloud\\.com$"),
            apply = ::clearQuery
        ),

        // SourceForge
        BuiltinRulesData(
            pattern = Regex("^sourceforge\\.net$"),
            pathPattern = Regex("/projects/.+/files/.+/download/?"),
            apply = ::clearQuery
        ),

        // Spotify (already covered as open.spotify.com)
        // included for redundancy safety
        BuiltinRulesData(
            pattern = Regex("^spotify\\.com$"),
            apply = { url -> retainParameters(url, Regex("si")) }
        ),

        // Substack
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?substack\\.com$"),
            apply = ::clearQuery
        ),

        // Taobao / Tmall
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?(taobao|tmall)\\.com$"),
            apply = { url -> retainParameters(url, Regex("id")) }
        ),

        // Telegram
        BuiltinRulesData(
            pattern = Regex("^t\\.me$"),
            apply = { url -> retainParameters(url, Regex("start|startapp|text")) }
        ),

        // Temu
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?temu\\.com(\\.br)?$"),
            apply = ::clearQuery
        ),

        // Threads
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?threads\\.net$"),
            apply = ::clearQuery
        ),

        // TikTok
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?(tiktok\\.com|vm\\.tiktok\\.com)$"),
            apply = ::clearQuery
        ),

        // TripAdvisor
        BuiltinRulesData(
            pattern = Regex("^www\\.tripadvisor\\.com$"),
            apply = { url -> retainParameters(url, Regex("ref|adid")) }
        ),

        // Twitter / X
        BuiltinRulesData(
            pattern = Regex("^(twitter|x)\\.com$"),
            apply = ::clearQuery
        ),

        // Udemy
        BuiltinRulesData(
            pattern = Regex("^(.+\\.)?udemy\\.com$"),
            apply = ::clearQuery
        ),

        // Vimeo
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?vimeo\\.com$"),
            apply = ::clearQuery
        ),

        // VK
        BuiltinRulesData(
            pattern = Regex("^(www\\.)?vk\\.com$"),
            apply = ::clearQuery
        ),

        // WhatsApp
        BuiltinRulesData(
            pattern = Regex("^api\\.whatsapp\\.com$"),
            apply = { url -> retainParameters(url, Regex("phone|text")) }
        ),

        // Wikipedia
        BuiltinRulesData(
            pattern = Regex("^www\\.wikipedia\\.org$"),
            apply = { url -> retainParameters(url, Regex("oldid|diff")) }
        ),

        // Yelp
        BuiltinRulesData(
            pattern = Regex("^www\\.yelp\\.com$"),
            apply = { url -> retainParameters(url, Regex("ref")) }
        ),

        // YouTube
        BuiltinRulesData(
            pattern = Regex("^(youtu\\.be|((www|music)\\.)?youtube\\.com)$"),
            apply = { url -> retainParameters(url, Regex("v|list|t|index")) }
        ),

        // Zhihu redirect
        BuiltinRulesData(
            pattern = Regex("^link\\.zhihu\\.com$"),
            queryPattern = Regex(".*\\btarget=.+"),
            apply = { url -> extractParameter(url, "target") ?: url }
        )
    )

    private fun extractParameter(url: String, paramName: String): String? {
        return url.toUri().getQueryParameter(paramName)
    }

    private fun clearQuery(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon()
            .clearQuery()
            .scheme(uri.scheme)
            .authority(uri.authority)
            .path(uri.path)
            .build()
            .toString()
    }

    private fun retainParameters(url: String, paramNameRegex: Regex): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames
                .filter { paramNameRegex.matches(it) }
                .forEach { param -> appendQueryParameter(param, uri.getQueryParameter(param)) }
        }.scheme(uri.scheme)
            .authority(uri.authority)
            .path(uri.path)
            .build()
            .toString()
    }

    private fun clearTrailingId(url: String): String {
        val uri = url.toUri()
        val modifiedPath = uri.path?.replace(trailingIdRegex, "") ?: uri.path
        return uri.buildUpon()
            .path(modifiedPath)
            .scheme(uri.scheme)
            .authority(uri.authority)
            .build()
            .toString()
    }
}