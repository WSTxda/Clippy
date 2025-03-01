package com.wstxda.clippy.cleaner.providers

object UrlSchemeProvider {
    data class SchemeInfo(val scheme: String, val needsCleaning: Boolean)

    val supportedSchemes = listOf(
        SchemeInfo("http", true),
        SchemeInfo("https", true),
        SchemeInfo("magnet", false),
        SchemeInfo("ftp", false),
        SchemeInfo("ftps", false),
        SchemeInfo("rtsp", false),
        SchemeInfo("rtmp", false),
        SchemeInfo("file", false),
        SchemeInfo("git", false),
        SchemeInfo("svn", false),
        SchemeInfo("ssh", false),
        SchemeInfo("irc", false),
        SchemeInfo("news", false),
        SchemeInfo("nntp", false),
        SchemeInfo("sftp", false),
        SchemeInfo("sip", false),
        SchemeInfo("sips", false),
        SchemeInfo("xmpp", false),
        SchemeInfo("vnc", false),
        SchemeInfo("ldap", false),
        SchemeInfo("ldaps", false)
    )

    fun isSupported(scheme: String) = supportedSchemes.any { it.scheme == scheme }
    fun needsCleaning(scheme: String) =
        supportedSchemes.find { it.scheme == scheme }?.needsCleaning ?: false
}