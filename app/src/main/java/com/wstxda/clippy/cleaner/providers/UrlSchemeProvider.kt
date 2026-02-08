package com.wstxda.clippy.cleaner.providers

object UrlSchemeProvider {

    data class SchemeInfo(
        val scheme: String, val needsCleaning: Boolean
    )

    private val schemes = listOf(
        SchemeInfo("http", needsCleaning = true),
        SchemeInfo("https", needsCleaning = true),
        SchemeInfo("magnet", needsCleaning = false),
        SchemeInfo("ftp", needsCleaning = false),
        SchemeInfo("ftps", needsCleaning = false),
        SchemeInfo("rtsp", needsCleaning = false),
        SchemeInfo("rtmp", needsCleaning = false),
        SchemeInfo("file", needsCleaning = false),
        SchemeInfo("git", needsCleaning = false),
        SchemeInfo("svn", needsCleaning = false),
        SchemeInfo("ssh", needsCleaning = false),
        SchemeInfo("irc", needsCleaning = false),
        SchemeInfo("news", needsCleaning = false),
        SchemeInfo("nntp", needsCleaning = false),
        SchemeInfo("sftp", needsCleaning = false),
        SchemeInfo("sip", needsCleaning = false),
        SchemeInfo("sips", needsCleaning = false),
        SchemeInfo("xmpp", needsCleaning = false),
        SchemeInfo("vnc", needsCleaning = false),
        SchemeInfo("ldap", needsCleaning = false),
        SchemeInfo("ldaps", needsCleaning = false)
    )

    val supportedSchemes: List<SchemeInfo> = schemes

    fun isSupported(scheme: String): Boolean = schemes.any { it.scheme == scheme }

    fun needsCleaning(scheme: String): Boolean =
        schemes.find { it.scheme == scheme }?.needsCleaning == true
}