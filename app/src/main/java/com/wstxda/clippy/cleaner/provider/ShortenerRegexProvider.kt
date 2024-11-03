package com.wstxda.clippy.cleaner.provider

object ShortenerRegexProvider {
    val shortenerRegexes = listOf(
        Regex("trk_\\w+", RegexOption.IGNORE_CASE),
        Regex("aff\\w+", RegexOption.IGNORE_CASE),
        Regex("(src|source)\\d*", RegexOption.IGNORE_CASE),
        Regex("(ad|advertising|ads)_\\w+", RegexOption.IGNORE_CASE),
        Regex("session([_\\-])id", RegexOption.IGNORE_CASE),
        Regex("sid", RegexOption.IGNORE_CASE),
        Regex("(clid|irclickid|click_id|clickid)", RegexOption.IGNORE_CASE),
        Regex("utm_\\w+", RegexOption.IGNORE_CASE),
        Regex("ref", RegexOption.IGNORE_CASE),
        Regex("si", RegexOption.IGNORE_CASE),
        Regex("campaign[_-]?id", RegexOption.IGNORE_CASE),
        Regex("promo[_-]?code", RegexOption.IGNORE_CASE),
        Regex("ad_id", RegexOption.IGNORE_CASE),
        Regex("ad_group_id", RegexOption.IGNORE_CASE),
        Regex("fbclid", RegexOption.IGNORE_CASE),
        Regex("gclid", RegexOption.IGNORE_CASE),
        Regex("msclkid", RegexOption.IGNORE_CASE),
        Regex("referrer", RegexOption.IGNORE_CASE),
        Regex("session_id", RegexOption.IGNORE_CASE),
        Regex("visitor_id", RegexOption.IGNORE_CASE),
        Regex("user_id", RegexOption.IGNORE_CASE),
        Regex("click_id", RegexOption.IGNORE_CASE),
        Regex("tracking_id", RegexOption.IGNORE_CASE),
        Regex("full_url", RegexOption.IGNORE_CASE),
        Regex("fallback_url", RegexOption.IGNORE_CASE),
        Regex("feature", RegexOption.IGNORE_CASE)
    )
}
