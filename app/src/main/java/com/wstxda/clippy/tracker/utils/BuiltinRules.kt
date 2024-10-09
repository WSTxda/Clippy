package com.wstxda.clippy.tracker.utils

import android.net.Uri

object BuiltinRules {
    private val DOUBAN_URL_PATTERN = Regex("www\\.douban\\.com")
    private val ZHIHU_URL_PATTERN = Regex("link\\.zhihu\\.com")
    private val SMZDM_URL_PATTERN = Regex(".+\\.smzdm\\.com")
    private val STACKEXCHANGE_URL_PATTERN = Regex("(.+\\.stackexchange|askubuntu|serverfault|stackoverflow|superuser)\\.com")
    private val TAOBAO_URL_PATTERN = Regex(".+\\.(taobao|tmall)\\.com")
    private val TIKTOK_URL_PATTERN = Regex(".+\\.tiktok\\.com")
    private val TWITTER_URL_PATTERN = Regex("(twitter|x)\\.com")
    private val XIAOHONGSHU_URL_PATTERN = Regex(".+\\.xiaohongshu\\.com")
    private val YOUTUBE_URL_PATTERN = Regex("youtu\\.be|((www|music)\\.)?youtube\\.com")
    private val SPOTIFY_URL_PATTERN = Regex("open\\.spotify\\.com")
    private val URL_SHORTENERS_PATTERN = Regex("bit\\.ly|tinyurl\\.com|goo\\.gl|ow\\.ly|rebrand\\.ly|snip\\.ly")
    private val FACEBOOK_URL_PATTERN = Regex("www\\.facebook\\.com|m\\.facebook\\.com")
    private val INSTAGRAM_URL_PATTERN = Regex("www\\.instagram\\.com")
    private val LINKEDIN_URL_PATTERN = Regex("www\\.linkedin\\.com")
    private val PINTEREST_URL_PATTERN = Regex("www\\.pinterest\\.com")
    private val REDDIT_URL_PATTERN = Regex("www\\.reddit\\.com")
    private val QUORA_URL_PATTERN = Regex("www\\.quora\\.com")
    private val DISCORD_URL_PATTERN = Regex("(discord\\.gg|discordapp\\.com|discord\\.com)")

    private const val URL_PARAM_URL = "url"
    private const val URL_PARAM_TARGET = "target"

    fun applyBuiltinRules(url: String): String {
        return when {
            matchesPattern(
                url, DOUBAN_URL_PATTERN, "/link2/", ".*\\b$URL_PARAM_URL=.+"
            ) -> getQueryParameter(url, URL_PARAM_URL) ?: url

            matchesPattern(
                url, ZHIHU_URL_PATTERN, "/", ".*\\b$URL_PARAM_TARGET=.+"
            ) -> getQueryParameter(url, URL_PARAM_TARGET) ?: url

            matchesPattern(url, SMZDM_URL_PATTERN) -> clearQuery(url)
            matchesPattern(
                url, STACKEXCHANGE_URL_PATTERN, "/[aq]/[0-9]+/[0-9]+/?"
            ) -> updateEncodedPath(
                url, Uri.parse(url).encodedPath?.replace("/[0-9]+/?$".toRegex(), "") ?: ""
            )

            matchesPattern(url, TAOBAO_URL_PATTERN) -> retainQueryParameters(url, "id")
            matchesPattern(url, TIKTOK_URL_PATTERN) -> clearQuery(url)
            matchesPattern(url, TWITTER_URL_PATTERN) -> clearQuery(url)
            matchesPattern(url, XIAOHONGSHU_URL_PATTERN) -> clearQuery(url)
            matchesPattern(url, YOUTUBE_URL_PATTERN) -> retainQueryParameters(url, "index|list|t|v")
            matchesPattern(url, SPOTIFY_URL_PATTERN) -> setEncodedQuery(url, "")
            matchesPattern(url, URL_SHORTENERS_PATTERN) -> clearQuery(url)
            matchesPattern(url, FACEBOOK_URL_PATTERN) -> retainQueryParameters(
                url, "fbclid|refid|source"
            )

            matchesPattern(url, INSTAGRAM_URL_PATTERN) -> retainQueryParameters(
                url, "igshid|utm_source|utm_medium"
            )

            matchesPattern(url, LINKEDIN_URL_PATTERN) -> retainQueryParameters(url, "trk")
            matchesPattern(url, PINTEREST_URL_PATTERN) -> retainQueryParameters(
                url, "utm_source|utm_medium|utm_campaign|pin"
            )

            matchesPattern(url, REDDIT_URL_PATTERN) -> retainQueryParameters(
                url, "utm_source|utm_medium|utm_campaign"
            )

            matchesPattern(url, QUORA_URL_PATTERN) -> retainQueryParameters(
                url, "utm_source|utm_medium|utm_campaign"
            )

            matchesPattern(url, DISCORD_URL_PATTERN) -> retainQueryParameters(
                url, "utm_source|utm_medium|ref"
            )

            else -> url
        }
    }

    private fun matchesPattern(
        url: String, hostPattern: Regex, pathPattern: String? = null, queryPattern: String? = null
    ): Boolean {
        val uri = Uri.parse(url)
        val hostMatches = uri.host?.matches(hostPattern) ?: false
        val pathMatches = pathPattern?.let { uri.path?.matches(Regex(it)) } ?: true
        val queryMatches = queryPattern?.let { uri.query?.matches(Regex(it)) } ?: true

        return hostMatches && pathMatches && queryMatches
    }

    private fun getQueryParameter(url: String, param: String): String? {
        val uri = Uri.parse(url)
        return uri.getQueryParameter(param)
    }

    private fun setEncodedQuery(url: String, newQuery: String?): String {
        val uri = Uri.parse(url)
        return uri.buildUpon().encodedQuery(newQuery).build().toString()
    }

    private fun clearQuery(url: String): String {
        return setEncodedQuery(url, null)
    }

    private fun retainQueryParameters(url: String, paramsPattern: String): String {
        val uri = Uri.parse(url)
        val newBuilder = uri.buildUpon().clearQuery()

        uri.queryParameterNames.forEach { queryParam ->
            if (queryParam.matches(Regex(paramsPattern))) {
                newBuilder.appendQueryParameter(queryParam, uri.getQueryParameter(queryParam))
            }
        }

        return newBuilder.build().toString()
    }

    private fun updateEncodedPath(url: String, newPath: String): String {
        val uri = Uri.parse(url)
        return uri.buildUpon().encodedPath(newPath).build().toString()
    }
}