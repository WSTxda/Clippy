package com.wstxda.clippy.cleaner.providers

object TextRegexProvider {

    private val schemesPattern: String by lazy {
        UrlSchemeProvider.supportedSchemes.joinToString("|") { it.scheme }
    }

    val urlRegex: Regex by lazy {
        """($schemesPattern):\S+""".toRegex()
    }
}