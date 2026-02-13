package com.wstxda.clippy.cleaner.providers

import com.wstxda.clippy.cleaner.data.UrlSchemeInfo

object UrlSchemeProvider {

    private val schemes = listOf(
        UrlSchemeInfo("http", needsCleaning = true),
        UrlSchemeInfo("https", needsCleaning = true),
        UrlSchemeInfo("magnet", needsCleaning = false),
        UrlSchemeInfo("ftp", needsCleaning = false),
        UrlSchemeInfo("ftps", needsCleaning = false),
        UrlSchemeInfo("rtsp", needsCleaning = false),
        UrlSchemeInfo("rtmp", needsCleaning = false),
        UrlSchemeInfo("file", needsCleaning = false),
        UrlSchemeInfo("git", needsCleaning = false),
        UrlSchemeInfo("svn", needsCleaning = false),
        UrlSchemeInfo("ssh", needsCleaning = false),
        UrlSchemeInfo("irc", needsCleaning = false),
        UrlSchemeInfo("news", needsCleaning = false),
        UrlSchemeInfo("nntp", needsCleaning = false),
        UrlSchemeInfo("sftp", needsCleaning = false),
        UrlSchemeInfo("sip", needsCleaning = false),
        UrlSchemeInfo("sips", needsCleaning = false),
        UrlSchemeInfo("xmpp", needsCleaning = false),
        UrlSchemeInfo("vnc", needsCleaning = false),
        UrlSchemeInfo("ldap", needsCleaning = false),
        UrlSchemeInfo("ldaps", needsCleaning = false)
    )

    val supportedSchemes: List<UrlSchemeInfo> = schemes

    fun isSupported(scheme: String): Boolean = schemes.any { it.scheme == scheme }

    fun needsCleaning(scheme: String): Boolean =
        schemes.find { it.scheme == scheme }?.needsCleaning == true
}