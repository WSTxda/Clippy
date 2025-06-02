package com.wstxda.clippy.cleaner.providers

object ShortenerRegexProvider {
    val shortenerRegexes = listOf(
        Regex("ad[_\\-]?id", RegexOption.IGNORE_CASE),
        Regex("affiliate[_\\-]?id", RegexOption.IGNORE_CASE),
        Regex("aff\\w+", RegexOption.IGNORE_CASE),
        Regex("(ad|advertising|ads|campaign)[-_]?\\w+", RegexOption.IGNORE_CASE),
        Regex("click_id", RegexOption.IGNORE_CASE),
        Regex("(clid|irclickid|click_id|clickid)", RegexOption.IGNORE_CASE),
        Regex("cookie[_\\-]?id", RegexOption.IGNORE_CASE),
        Regex("device[_\\-]?id", RegexOption.IGNORE_CASE),
        Regex("fbclid", RegexOption.IGNORE_CASE),
        Regex("feature", RegexOption.IGNORE_CASE),
        Regex("full_url", RegexOption.IGNORE_CASE),
        Regex("gclid", RegexOption.IGNORE_CASE),
        Regex("hash[_\\-]?id", RegexOption.IGNORE_CASE),
        Regex("keyword", RegexOption.IGNORE_CASE),
        Regex("(lead|session|transaction)[-_]?id", RegexOption.IGNORE_CASE),
        Regex("(msclkid|sid)", RegexOption.IGNORE_CASE),
        Regex("ref", RegexOption.IGNORE_CASE),
        Regex("referral[_\\-]?code", RegexOption.IGNORE_CASE),
        Regex("referrer[_\\-]?id", RegexOption.IGNORE_CASE),
        Regex("session[_\\-]?id", RegexOption.IGNORE_CASE),
        Regex("source[_\\-]?id", RegexOption.IGNORE_CASE),
        Regex("tracking_id", RegexOption.IGNORE_CASE),
        Regex("trk_\\w+", RegexOption.IGNORE_CASE),
        Regex("utm_(adgroup|campaign|content|medium|ref|source|term|term_id)", RegexOption.IGNORE_CASE),
        Regex("user_id", RegexOption.IGNORE_CASE),
        Regex("visitor_id", RegexOption.IGNORE_CASE)
    )
}