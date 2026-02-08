package com.wstxda.clippy.cleaner.providers

object ShortenerRegexProvider {

    private val patterns = listOf(
        "(ad|advertising|ads|campaign)[-_]?\\w+" to RegexOption.IGNORE_CASE,
        "ad[_\\-]?id" to RegexOption.IGNORE_CASE,
        "aff\\w+" to RegexOption.IGNORE_CASE,
        "affiliate[_\\-]?id" to RegexOption.IGNORE_CASE,
        "campid" to RegexOption.IGNORE_CASE,
        "campaignid" to RegexOption.IGNORE_CASE,
        "(clid|irclickid|click_id|clickid)" to RegexOption.IGNORE_CASE,
        "cookie[_\\-]?id" to RegexOption.IGNORE_CASE,
        "device[_\\-]?id" to RegexOption.IGNORE_CASE,
        "dclid" to RegexOption.IGNORE_CASE,
        "emci" to RegexOption.IGNORE_CASE,
        "emdi" to RegexOption.IGNORE_CASE,
        "feature" to RegexOption.IGNORE_CASE,
        "full_url" to RegexOption.IGNORE_CASE,
        "fbclid" to RegexOption.IGNORE_CASE,
        "gbraid" to RegexOption.IGNORE_CASE,
        "gclid" to RegexOption.IGNORE_CASE,
        "hash[_\\-]?id" to RegexOption.IGNORE_CASE,
        "hootPostID" to RegexOption.IGNORE_CASE,
        "keyword" to RegexOption.IGNORE_CASE,
        "(lead|session|transaction)[-_]?id" to RegexOption.IGNORE_CASE,
        "li_fat_id" to RegexOption.IGNORE_CASE,
        "mibextid" to RegexOption.IGNORE_CASE,
        "mkevt" to RegexOption.IGNORE_CASE,
        "mkcid" to RegexOption.IGNORE_CASE,
        "mkrid" to RegexOption.IGNORE_CASE,
        "(msclkid|sid)" to RegexOption.IGNORE_CASE,
        "oly[_\\-]?anon[_\\-]?id" to RegexOption.IGNORE_CASE,
        "oly[_\\-]?enc[_\\-]?id" to RegexOption.IGNORE_CASE,
        "ref" to RegexOption.IGNORE_CASE,
        "referral[_\\-]?code" to RegexOption.IGNORE_CASE,
        "referrer[_\\-]?id" to RegexOption.IGNORE_CASE,
        "session[_\\-]?id" to RegexOption.IGNORE_CASE,
        "source[_\\-]?id" to RegexOption.IGNORE_CASE,
        "ttclid" to RegexOption.IGNORE_CASE,
        "tracking_id" to RegexOption.IGNORE_CASE,
        "trk_\\w+" to RegexOption.IGNORE_CASE,
        "utm_(adgroup|campaign|content|medium|ref|source|term|term_id)" to RegexOption.IGNORE_CASE,
        "user_id" to RegexOption.IGNORE_CASE,
        "visitor_id" to RegexOption.IGNORE_CASE,
        "vero_id" to RegexOption.IGNORE_CASE
    )

    val shortenerRegexes: List<Regex> = patterns.map { (pattern, option) ->
        Regex(pattern, option)
    }
}