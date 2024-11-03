package com.wstxda.clippy.cleaner.providers

import android.net.Uri
import com.wstxda.clippy.cleaner.modules.BuiltinRulesResolver
import com.wstxda.clippy.cleaner.modules.utils.BuiltinRulesData

object UrlBuiltinRulesProvider {
    private val douban = Regex("www\\.douban\\.com")
    private val zhihu = Regex("link\\.zhihu\\.com")
    private val smzdm = Regex(".+\\.smzdm\\.com")
    private val stackexchange = Regex("(.+\\.stackexchange|askubuntu|serverfault|stackoverflow|superuser)\\.com")
    private val taobao = Regex(".+\\.(taobao|tmall)\\.com")
    private val tiktok = Regex(".+\\.tiktok\\.com")
    private val twitter = Regex("(twitter|x)\\.com")
    private val xiaohongshu = Regex(".+\\.xiaohongshu\\.com")
    private val youtube = Regex("youtu\\.be|((www|music)\\.)?youtube\\.com")
    private val spotify = Regex("open\\.spotify\\.com")
    private val urlShorteners = Regex("bit\\.ly|tinyurl\\.com|goo\\.gl|ow\\.ly|rebrand\\.ly|snip\\.ly")
    private val facebook = Regex("www\\.facebook\\.com|m\\.facebook\\.com")
    private val instagram = Regex("www\\.instagram\\.com")
    private val linkedin = Regex("www\\.linkedin\\.com")
    private val pinterest = Regex("www\\.pinterest\\.com|api\\.pinterest\\.com")
    private val reddit = Regex("www\\.reddit\\.com")
    private val quora = Regex("www\\.quora\\.com")
    private val aliexpress = Regex("www\\.aliexpress\\.com|m\\.aliexpress\\.com")

    val builtinRulesData: List<BuiltinRulesData> =
        listOf(createRule(douban, "/link2/", ".*\\burl=.+") { url ->
            BuiltinRulesResolver.extractQueryParameter(url, "url") ?: url
        }, createRule(zhihu, "/", ".*\\btarget=.+") { url ->
            BuiltinRulesResolver.extractQueryParameter(url, "target") ?: url
        }, createRule(smzdm) { url ->
            BuiltinRulesResolver.clearQuery(url)
        }, createRule(stackexchange, "/[aq]/[0-9]+/[0-9]+/?") { url ->
            BuiltinRulesResolver.updatePathWithoutTrailingId(
                url, Uri.parse(url).encodedPath?.replace("/[0-9]+/?$".toRegex(), "") ?: ""
            )
        }, createRule(taobao) { url ->
            BuiltinRulesResolver.retainSpecificQueryParameters(url, "id")
        }, createRule(tiktok) { url ->
            BuiltinRulesResolver.clearQuery(url)
        }, createRule(twitter) { url ->
            BuiltinRulesResolver.clearQuery(url)
        }, createRule(xiaohongshu) { url ->
            BuiltinRulesResolver.clearQuery(url)
        }, createRule(youtube) { url ->
            BuiltinRulesResolver.retainSpecificQueryParameters(url, "index|list|t|v")
        }, createRule(spotify) { url ->
            BuiltinRulesResolver.setEncodedQuery(url, "")
        }, createRule(urlShorteners) { url ->
            BuiltinRulesResolver.clearQuery(url)
        }, createRule(facebook) { url ->
            BuiltinRulesResolver.retainSpecificQueryParameters(url, "fbclid|refid|source")
        }, createRule(instagram) { url ->
            BuiltinRulesResolver.retainSpecificQueryParameters(url, "igshid|utm_source|utm_medium")
        }, createRule(linkedin) { url ->
            BuiltinRulesResolver.retainSpecificQueryParameters(url, "trk")
        }, createRule(pinterest) { url ->
            BuiltinRulesResolver.retainSpecificQueryParameters(
                url, "utm_source|utm_medium|utm_campaign|pin"
            )
        }, createRule(reddit) { url ->
            BuiltinRulesResolver.retainSpecificQueryParameters(
                url, "utm_source|utm_medium|utm_campaign"
            )
        }, createRule(quora) { url ->
            BuiltinRulesResolver.retainSpecificQueryParameters(
                url, "utm_source|utm_medium|utm_campaign"
            )
        }, createRule(aliexpress) { url ->
            BuiltinRulesResolver.clearQuery(url)
        })

    private fun createRule(
        pattern: Regex,
        pathPattern: String? = null,
        queryPattern: String? = null,
        apply: (String) -> String
    ): BuiltinRulesData {
        return BuiltinRulesData(
            pattern = pattern, pathPattern = pathPattern, queryPattern = queryPattern, apply = apply
        )
    }
}