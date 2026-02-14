package com.wstxda.clippy.cleaner.providers

import com.wstxda.clippy.cleaner.data.UrlSchemeInfo

object UrlSchemeProvider {

    private val schemes = listOf(
        UrlSchemeInfo("android-app", needsCleaning = true),
        UrlSchemeInfo("bitcoin", needsCleaning = false),
        UrlSchemeInfo("data", needsCleaning = false),
        UrlSchemeInfo("discord", needsCleaning = true),
        UrlSchemeInfo("file", needsCleaning = false),
        UrlSchemeInfo("ftp", needsCleaning = false),
        UrlSchemeInfo("ftps", needsCleaning = false),
        UrlSchemeInfo("geo", needsCleaning = false),
        UrlSchemeInfo("git", needsCleaning = false),
        UrlSchemeInfo("http", needsCleaning = true),
        UrlSchemeInfo("https", needsCleaning = true),
        UrlSchemeInfo("intent", needsCleaning = true),
        UrlSchemeInfo("ios-app", needsCleaning = true),
        UrlSchemeInfo("itms", needsCleaning = true),
        UrlSchemeInfo("itms-apps", needsCleaning = true),
        UrlSchemeInfo("magnet", needsCleaning = false),
        UrlSchemeInfo("mailto", needsCleaning = false),
        UrlSchemeInfo("maps", needsCleaning = true),
        UrlSchemeInfo("market", needsCleaning = true),
        UrlSchemeInfo("netflix", needsCleaning = true),
        UrlSchemeInfo("sftp", needsCleaning = false),
        UrlSchemeInfo("slack", needsCleaning = true),
        UrlSchemeInfo("sms", needsCleaning = false),
        UrlSchemeInfo("spotify", needsCleaning = true),
        UrlSchemeInfo("steam", needsCleaning = true),
        UrlSchemeInfo("tel", needsCleaning = false),
        UrlSchemeInfo("telegram", needsCleaning = false),
        UrlSchemeInfo("twitch", needsCleaning = true),
        UrlSchemeInfo("webcal", needsCleaning = true),
        UrlSchemeInfo("whatsapp", needsCleaning = false),
        UrlSchemeInfo("youtube", needsCleaning = true)
    )

    val supportedSchemes: List<UrlSchemeInfo> = schemes

    fun isSupported(scheme: String): Boolean =
        schemes.any { it.scheme.equals(scheme, ignoreCase = true) }

    fun needsCleaning(scheme: String): Boolean =
        schemes.find { it.scheme.equals(scheme, ignoreCase = true) }?.needsCleaning == true
}