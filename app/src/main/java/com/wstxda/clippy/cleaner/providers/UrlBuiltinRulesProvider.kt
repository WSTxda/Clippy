package com.wstxda.clippy.cleaner.providers

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.utils.UriUtils
import com.wstxda.clippy.cleaner.data.BuiltinRulesRegex

object UrlBuiltinRulesProvider {

    val builtinRuleRegexes: List<BuiltinRulesRegex> = listOf(

        // AliExpress
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?aliexpress\\.com$"), apply = UriUtils::clearQuery
        ),

        // Amazon
        BuiltinRulesRegex(
            pattern = Regex("^www\\.amazon\\.(com|com\\.br|ca|de|es|fr|it|co\\.uk|co\\.jp)$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("dp|gp")) }),

        // Airbnb
        BuiltinRulesRegex(
            pattern = Regex("^www\\.airbnb\\.com$"), apply = UriUtils::clearQuery
        ),

        // AskUbuntu / StackExchange network
        BuiltinRulesRegex(
            pattern = Regex("(.+\\.)?(stackexchange|askubuntu|serverfault|stackoverflow|superuser)\\.com$"),
            pathPattern = Regex("/[aq]/[0-9]+/[0-9]+/?"),
            apply = UriUtils::clearTrailingId
        ),

        // BBC
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(bbc\\.com|bbc\\.co\\.uk)$"), apply = UriUtils::clearQuery
        ),

        // Bitly and other shorteners
        BuiltinRulesRegex(
            pattern = Regex("^(bit\\.ly|tinyurl\\.com|goo\\.gl|ow\\.ly|rebrand\\.ly|snip\\.ly|t\\.co|is\\.gd|v\\.gd|tiny\\.cc|cutt\\.ly)$"),
            apply = UriUtils::clearQuery
        ),

        // Bluesky
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?bluesky\\.app$"), apply = UriUtils::clearQuery
        ),

        // Booking.com
        BuiltinRulesRegex(
            pattern = Regex("^www\\.booking\\.com$"), apply = UriUtils::clearQuery
        ),

        // Canva
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?canva\\.com$"), apply = UriUtils::clearQuery
        ),

        // Clear redirection link - Douban
        BuiltinRulesRegex(
            pattern = Regex("^www\\.douban\\.com$"),
            pathPattern = Regex("/link2/"),
            queryPattern = Regex(".*\\burl=.+"),
            apply = { url -> UriUtils.extractParameter(url, "url") ?: url }),

        // Coursera
        BuiltinRulesRegex(
            pattern = Regex("^www\\.coursera\\.org$"), apply = { url ->
                UriUtils.retainParameters(
                    url, Regex("utm_source|utm_medium|utm_campaign")
                )
            }),

        // Dailymotion
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?dailymotion\\.com$"), apply = UriUtils::clearQuery
        ),

        // Discord
        BuiltinRulesRegex(
            pattern = Regex("^www\\.discord\\.com$"), apply = UriUtils::clearQuery
        ),

        // eBay
        BuiltinRulesRegex(
            pattern = Regex("^www\\.ebay\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("id|item_id")) }),

        // Facebook
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(facebook|m\\.facebook)\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("^(?!fbclid|utm_).*")) }),

        // Google Forms
        BuiltinRulesRegex(
            pattern = Regex("^forms\\.google\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("id|usp")) }),

        // Google Docs
        BuiltinRulesRegex(
            pattern = Regex("^docs\\.google\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("id|usp")) }),

        // Imgur
        BuiltinRulesRegex(
            pattern = Regex("^imgur\\.com$"), apply = UriUtils::clearQuery
        ),

        // Instagram
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?instagram\\.com$"), apply = UriUtils::clearQuery
        ),

        // LinkedIn
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(linkedin\\.com|lnkd\\.in)$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("^(?!li_fat_id|trk|utm_).*")) }),

        // Mastodon
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?mastodon\\..+$"), apply = UriUtils::clearQuery
        ),

        // Medium
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?medium\\.com$"), apply = UriUtils::clearQuery
        ),

        // Mercado Livre
        BuiltinRulesRegex(
            pattern = Regex("^www\\.mercadolivre\\.com\\.br$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("item_id|id|oid")) }),

        // Notion
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?notion\\.so$"), apply = UriUtils::clearQuery
        ),

        // Spotify
        BuiltinRulesRegex(
            pattern = Regex("^(open\\.)?spotify\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("si")) }),

        // Pinterest
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(pinterest\\.com|api\\.pinterest\\.com)$"), apply = { url ->
                UriUtils.retainParameters(
                    url, Regex("utm_source|utm_medium|utm_campaign|pin")
                )
            }),

        // Quora
        BuiltinRulesRegex(
            pattern = Regex("^www\\.quora\\.com$"), apply = { url ->
                UriUtils.retainParameters(
                    url, Regex("utm_source|utm_medium|utm_campaign")
                )
            }),

        // Reddit
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(reddit\\.com|redd\\.it)$"), apply = { url ->
                val uri = url.toUri()
                uri.buildUpon().clearQuery().apply {
                    uri.queryParameterNames.filter { it == "context" }.forEach { param ->
                        uri.getQueryParameter(param)?.let { value ->
                            appendQueryParameter(param, value)
                        }
                    }
                }.scheme(uri.scheme).authority(uri.authority).path(uri.path).build().toString()
            }),

        // Shein
        BuiltinRulesRegex(
            pattern = Regex("^www\\.shein\\.com$"), apply = UriUtils::clearQuery
        ),

        // Shopee
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?shopee\\.com(\\.br)?$"), apply = UriUtils::clearQuery
        ),

        // SoundCloud
        BuiltinRulesRegex(
            pattern = Regex("^www\\.soundcloud\\.com$"), apply = UriUtils::clearQuery
        ),

        // SourceForge
        BuiltinRulesRegex(
            pattern = Regex("^sourceforge\\.net$"),
            pathPattern = Regex("/projects/.+/files/.+/download/?"),
            apply = UriUtils::clearQuery
        ),

        // Substack
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?substack\\.com$"), apply = UriUtils::clearQuery
        ),

        // Taobao / Tmall
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(taobao|tmall)\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("id")) }),

        // Telegram
        BuiltinRulesRegex(
            pattern = Regex("^t\\.me$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("start|startapp|text")) }),

        // Temu
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?temu\\.com(\\.br)?$"), apply = UriUtils::clearQuery
        ),

        // Threads
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?threads\\.net$"), apply = UriUtils::clearQuery
        ),

        // TikTok
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(tiktok\\.com|vm\\.tiktok\\.com)$"),
            apply = UriUtils::clearQuery
        ),

        // TripAdvisor
        BuiltinRulesRegex(
            pattern = Regex("^www\\.tripadvisor\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("ref|adid")) }),

        // Twitter / X
        BuiltinRulesRegex(
            pattern = Regex("^(twitter|x)\\.com$"), apply = UriUtils::clearQuery
        ),

        // Udemy
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?udemy\\.com$"), apply = UriUtils::clearQuery
        ),

        // Vimeo
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?vimeo\\.com$"), apply = UriUtils::clearQuery
        ),

        // VK
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?vk\\.com$"), apply = UriUtils::clearQuery
        ),

        // WhatsApp
        BuiltinRulesRegex(
            pattern = Regex("^api\\.whatsapp\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("phone|text")) }),

        // Wikipedia
        BuiltinRulesRegex(
            pattern = Regex("^www\\.wikipedia\\.org$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("oldid|diff")) }),

        // Yelp
        BuiltinRulesRegex(
            pattern = Regex("^www\\.yelp\\.com$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("ref")) }),

        // YouTube
        BuiltinRulesRegex(
            pattern = Regex("^(youtu\\.be|((www|music)\\.)?youtube\\.com)$"),
            apply = { url -> UriUtils.retainParameters(url, Regex("v|list|t|index")) }),

        // Zhihu redirect
        BuiltinRulesRegex(
            pattern = Regex("^link\\.zhihu\\.com$"),
            queryPattern = Regex(".*\\btarget=.+"),
            apply = { url -> UriUtils.extractParameter(url, "target") ?: url })
    )
}