package com.wstxda.clippy.cleaner.providers

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.utils.BuiltinRulesUri
import com.wstxda.clippy.cleaner.data.BuiltinRulesRegex

object BuiltinRulesProvider {

    private val facebookRetainParams = Regex("^(v|story_fbid|id|set|type)$")
    private val githubRetainParams   = Regex("^(tab|q|type|language)$")
    private val redditRetainParams   = Regex("^(context|sort)$")
    private val xRetainParams  = Regex("^(lang|reply_to)$")
    private val linkedinBlacklist    = Regex("""^(li_fat_id|trk|trkInfo|utm_.+|ref|tracking.*|original.*|context).*""")

    private fun applyFacebook(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { facebookRetainParams.matches(it) }
                .forEach { param ->
                    uri.getQueryParameter(param)?.let { value ->
                        appendQueryParameter(param, value)
                    }
                }
        }.scheme(uri.scheme).authority(uri.authority).path(uri.path).fragment(uri.fragment)
            .build().toString()
    }

    private fun applyGitHub(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { githubRetainParams.matches(it) }
                .forEach { param ->
                    uri.getQueryParameter(param)?.let { value ->
                        appendQueryParameter(param, value)
                    }
                }
        }.scheme(uri.scheme).authority(uri.authority).path(uri.path).fragment(uri.fragment)
            .build().toString()
    }

    private fun applyLinkedIn(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { !linkedinBlacklist.matches(it) }
                .forEach { param ->
                    uri.getQueryParameter(param)?.let { value ->
                        appendQueryParameter(param, value)
                    }
                }
        }.scheme(uri.scheme).authority(uri.authority).path(uri.path).fragment(uri.fragment)
            .build().toString()
    }

    private fun applyReddit(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { redditRetainParams.matches(it) }
                .forEach { param ->
                    uri.getQueryParameter(param)?.let { value ->
                        appendQueryParameter(param, value)
                    }
                }
        }.scheme(uri.scheme).authority(uri.authority).path(uri.path).fragment(uri.fragment)
            .build().toString()
    }

    private fun applyX(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { xRetainParams.matches(it) }
                .forEach { param ->
                    uri.getQueryParameter(param)?.let { value ->
                        appendQueryParameter(param, value)
                    }
                }
        }.scheme(uri.scheme).authority(uri.authority).path(uri.path).fragment(uri.fragment)
            .build().toString()
    }

    val builtinRuleRegexes: List<BuiltinRulesRegex> = listOf(

        // Airbnb
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?airbnb\\.(com|com\\.br|ca|co\\.uk|de|fr|es|it)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // AliExpress
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?aliexpress\\.(com|us|ru)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(
                    url, Regex("^(item|productId|product_id|SearchText)$")
                )
            }),

        // Amazon
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?amazon\\.(com|com\\.br|ca|cn|de|es|fr|it|nl|co\\.uk|co\\.jp|com\\.au|in|com\\.mx|sg)$"),
            apply = { url ->
                BuiltinRulesUri.retainParameters(
                    url, Regex("^(dp|gp|ASIN|asin|productId|keywords|field-keywords|url|k)$")
                )
            }),

        // Amazon — search
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?amazon\\.(com|com\\.br|ca|de|es|fr|it|nl|co\\.uk|co\\.jp)$"),
            pathPattern = Regex("/s"),
            apply = { url ->
                BuiltinRulesUri.retainParameters(
                    url, Regex("^(k|keywords|field-keywords|i|rh|url)$")
                )
            }),

        // Americanas
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?americanas\\.com\\.br$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Apple Music
        BuiltinRulesRegex(
            pattern = Regex("^(music|itunes)\\.apple\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(i|ls|app|mt|uo)$"))
            }),

        // Ars Technica
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?arstechnica\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Asana
        BuiltinRulesRegex(
            pattern = Regex("^app\\.asana\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // StackExchange network (AskUbuntu, ServerFault, StackOverflow, SuperUser, etc.)
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(stackexchange|askubuntu|serverfault|stackoverflow|superuser)\\.com$"),
            pathPattern = Regex("/[aq]/[0-9]+(/[0-9]+)?/?"),
            apply = BuiltinRulesUri::clearTrailingId
        ),

        // BBC
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(bbc\\.com|bbc\\.co\\.uk)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Best Buy
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?bestbuy\\.(com|ca)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(skuId|id|productId)$"))
            }),

        // Bilibili
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(bilibili\\.com|b23\\.tv)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Bing
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?bing\\.(com|co\\.uk|de|fr|it|es)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(q|first|count|filters)$"))
            }),

        // Bluesky
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(bsky\\.app|bluesky\\.app)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Booking.com
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?booking\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(
                    url,
                    Regex("^(hotel_id|aid|label|checkin|checkout|group_adults|group_children)$")
                )
            }),

        // Canva
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?canva\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // CNET
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?cnet\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // CNN
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(cnn\\.com|edition\\.cnn\\.com)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Coursera
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?coursera\\.org$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(specialization|courseId)$"))
            }),

        // Daily Coding Problem
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?dailycodingproblem\\.com$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Dailymotion
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?dailymotion\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Deezer
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?deezer\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // DeviantArt
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?deviantart\\.com$"),
            pathPattern = Regex(".*/outgoing"),
            apply = { url -> BuiltinRulesUri.extractParameter(url, "url") ?: url }),

        // Discord
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?discord\\.(com|gg)$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Docker Hub
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?hub\\.docker\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Douban redirect
        BuiltinRulesRegex(
            pattern = Regex("^www\\.douban\\.com$"),
            pathPattern = Regex("/link2/"),
            queryPattern = Regex(".*\\burl=.+"),
            apply = { url -> BuiltinRulesUri.extractParameter(url, "url") ?: url }),

        // DuckDuckGo
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?duckduckgo\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(q|t|ia|iax)$"))
            }),

        // eBay
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?ebay\\.(com|com\\.br|ca|co\\.uk|de|fr|it|es|com\\.au)$"),
            apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(item|itm|id|var|hash|_nkw)$"))
            }),

        // edX
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?edx\\.org$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Epic Games
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?epicgames\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Etsy
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?etsy\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(listing_id|ref)$"))
            }),

        // Expedia
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?expedia\\.(com|com\\.br|ca|co\\.uk)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(
                    url, Regex("^(destination|d1|d2|adults|children)$")
                )
            }),

        // Facebook
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(facebook|fb|m\\.facebook)\\.com$"),
            apply = ::applyFacebook
        ),

        // Facebook redirect
        BuiltinRulesRegex(
            pattern = Regex("^l[a-z]?\\.facebook\\.com$"),
            pathPattern = Regex("/l\\.php"),
            queryPattern = Regex(".*\\bu=.+"),
            apply = { url -> BuiltinRulesUri.extractParameter(url, "u") ?: url }),

        // Figma
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?figma\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(node-id|mode|t)$"))
            }),

        // Flickr
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?flickr\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // GitHub
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?github\\.com$"),
            apply = ::applyGitHub
        ),

        // GitLab
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?gitlab\\.(com|io)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(ref|search|page)$"))
            }),

        // Globo.com
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(globo\\.com|g1\\.globo\\.com|oglobo\\.globo\\.com)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // GOG
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?gog\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Google
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?google\\.(com|com\\.br|co\\.uk|de|fr|it|es|ca|com\\.au|co\\.jp)$"),
            apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(q|hl|lr|ie|num|start|tbm|tbs)$"))
            }),

        // Google Docs/Sheets/Slides
        BuiltinRulesRegex(
            pattern = Regex("^docs\\.google\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(id|usp|edit|copy)$"))
            }),

        // Google Forms
        BuiltinRulesRegex(
            pattern = Regex("^docs\\.google\\.com$"),
            pathPattern = Regex("/forms/.*"),
            apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(viewform|usp|embedded)$"))
            }),

        // Google redirect
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?google\\.(com|com\\.br|co\\.uk)$"),
            pathPattern = Regex("/url"),
            queryPattern = Regex(".*\\b(url|q)=.+"),
            apply = { url ->
                BuiltinRulesUri.extractParameter(url, "url")
                    ?: BuiltinRulesUri.extractParameter(url, "q") ?: url
            }),

        // HH.ru
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?hh\\.ru$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Hotels.com
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?hotels\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(hotelId|pos|locale)$"))
            }),

        // IMDb
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?imdb\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(s|ref_)$"))
            }),

        // Imgur
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?imgur\\.(com|io)$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Indeed
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?indeed\\.(com|com\\.br|ca|co\\.uk)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(jk|q|l)$"))
            }),

        // Instagram
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?instagram\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Khan Academy
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?khanacademy\\.org$"), apply = BuiltinRulesUri::clearQuery
        ),

        // LinkedIn
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(linkedin\\.com|lnkd\\.in)$"),
            apply = ::applyLinkedIn
        ),

        // Magalu
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?magazineluiza\\.com\\.br$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Mastodon
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?mastodon\\..+$"), apply = BuiltinRulesUri::clearQuery
        ),

        // MDN
        BuiltinRulesRegex(
            pattern = Regex("^developer\\.mozilla\\.org$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Medium
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(medium\\.com|towardsdatascience\\.com)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(source|postPublishedType)$"))
            }),

        // Mercado Livre
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?mercado(libre|livre)\\.(com\\.ar|com\\.br|com\\.mx|cl|co|com\\.uy|com\\.ve)$"),
            apply = { url ->
                BuiltinRulesUri.retainParameters(
                    url, Regex("^(item_id|id|oid|deal_id|promotion_id|q)$")
                )
            }),

        // Miro
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?miro\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Mozilla
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?mozilla\\.org$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(as|client|channel)$"))
            }),

        // Mozilla outgoing
        BuiltinRulesRegex(
            pattern = Regex("^outgoing\\.prod\\.mozaws\\.net$"), apply = { url ->
                val uri = url.toUri()
                uri.path?.substringAfterLast('/')?.let {
                    "https://$it"
                } ?: url
            }),

        // MyAnimeList
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?myanimelist\\.net$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Net Parade
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?net-parade\\.it$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Netflix
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?netflix\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(title|s|t)$"))
            }),

        // Netshoes
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?netshoes\\.com\\.br$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Newegg
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?newegg\\.(com|ca)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(Item|N|D)$"))
            }),

        // New York Times
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?nytimes\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Notion
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?notion\\.(so|site)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^([pv])$"))
            }),

        // npm
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?npmjs\\.(com|org)$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Pinterest
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(pinterest\\.com|pin\\.it)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(pin_id|url)$"))
            }),

        // Prvnizpravy
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?prvnizpravy\\.cz$"), apply = BuiltinRulesUri::clearQuery
        ),

        // PyPI
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?pypi\\.org$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Quora
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?quora\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(share|ch)$"))
            }),

        // ReadDC
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?readdc\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Reddit
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(reddit\\.com|redd\\.it|old\\.reddit\\.com)$"),
            apply = ::applyReddit
        ),

        // Reddit redirect
        BuiltinRulesRegex(
            pattern = Regex("^(out|click)\\.reddit\\.com$"),
            queryPattern = Regex(".*\\burl=.+"),
            apply = { url -> BuiltinRulesUri.extractParameter(url, "url") ?: url }),

        // Reuters
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?reuters\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Rotten Tomatoes
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?rottentomatoes\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Shein
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?shein\\.com(\\.br)?$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(goods_id|url_from)$"))
            }),

        // Shopee
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?shopee\\.(com\\.br|com|sg|my|ph|th|vn|tw|co\\.id)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Shutterstock
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?shutterstock\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Skyscanner
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?skyscanner\\.(com|com\\.br|net)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Snapchat
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(snapchat\\.com|t\\.snapchat\\.com)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // SoundCloud
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(soundcloud\\.com|on\\.soundcloud\\.com)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // SourceForge
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?sourceforge\\.net$"),
            pathPattern = Regex("/projects/.+/files/.+/download/?"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Spotify
        BuiltinRulesRegex(
            pattern = Regex("^(open\\.)?(spotify\\.com|spotify\\.link)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(si|context|play)$"))
            }),

        // Steam
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?store\\.steampowered\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(id|appid)$"))
            }),

        // Steam Community
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?steamcommunity\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Substack
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?substack\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(token|publication_id)$"))
            }),

        // Taobao / Tmall
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?(taobao|tmall)\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(id|item_id|spm)$"))
            }),

        // Target
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?target\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Tchibo
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?tchibo\\.de$"), apply = BuiltinRulesUri::clearQuery
        ),

        // TechCrunch
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?techcrunch\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Telegram
        BuiltinRulesRegex(
            pattern = Regex("^t\\.me$"), apply = { url ->
                BuiltinRulesUri.retainParameters(
                    url, Regex("^(start|startapp|text|startgroup|startchannel)$")
                )
            }),

        // Temu
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?temu\\.com(\\.br)?$"), apply = BuiltinRulesUri::clearQuery
        ),

        // The Guardian
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?theguardian\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // The Verge
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?theverge\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Threads
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?threads\\.net$"), apply = BuiltinRulesUri::clearQuery
        ),

        // TikTok
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(tiktok\\.com|vm\\.tiktok\\.com|vt\\.tiktok\\.com)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // TripAdvisor
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?tripadvisor\\.(com|com\\.br|co\\.uk|de|fr|it|es)$"),
            apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^([gd])$"))
            }),

        // Trello
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?trello\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Tweakers
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?tweakers\\.net$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Twitch
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?twitch\\.tv$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Twitter t.co shortener
        BuiltinRulesRegex(
            pattern = Regex("^t\\.co$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Udemy
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?udemy\\.com$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(couponCode)$"))
            }),

        // Unsplash
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?unsplash\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // UOL
        BuiltinRulesRegex(
            pattern = Regex("^(.+\\.)?uol\\.com\\.br$"), apply = BuiltinRulesUri::clearQuery
        ),

        // Vimeo
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(vimeo\\.com|player\\.vimeo\\.com)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(h|app_id)$"))
            }),

        // Vivaldi
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?vivaldi\\.(com|net)$"), apply = BuiltinRulesUri::clearQuery
        ),

        // VK
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(vk\\.com|vkontakte\\.ru)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(w|z|id)$"))
            }),

        // Walmart
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?walmart\\.(com|com\\.br|ca)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(id|athcpid|ip|selectedSellerId)$"))
            }),

        // WhatsApp
        BuiltinRulesRegex(
            pattern = Regex("^(api\\.)?(whatsapp\\.com|wa\\.me)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(phone|text|app)$"))
            }),

        // Wikipedia
        BuiltinRulesRegex(
            pattern = Regex("^([a-z]{2,3}\\.)?wikipedia\\.org$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(oldid|diff|title|action)$"))
            }),

        // Yandex
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?yandex\\.(ru|com)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(url, Regex("^(text|lr)$"))
            }),

        // Yelp
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?yelp\\.(com|com\\.br|ca|co\\.uk)$"),
            apply = BuiltinRulesUri::clearQuery
        ),

        // Youku
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?youku\\.com$"), apply = BuiltinRulesUri::clearQuery
        ),

        // YouTube
        BuiltinRulesRegex(
            pattern = Regex("^(youtu\\.be|((www|music|m)\\.)?youtube\\.com)$"), apply = { url ->
                BuiltinRulesUri.retainParameters(
                    url, Regex("^(v|list|t|index|time_continue|start)$")
                )
            }),

        // X (Twitter)
        BuiltinRulesRegex(
            pattern = Regex("^(www\\.)?(twitter|x)\\.com$"),
            apply = ::applyX
        ),

        // Zhihu redirect
        BuiltinRulesRegex(
            pattern = Regex("^link\\.zhihu\\.com$"),
            queryPattern = Regex(".*\\btarget=.+"),
            apply = { url -> BuiltinRulesUri.extractParameter(url, "target") ?: url })
    )
}