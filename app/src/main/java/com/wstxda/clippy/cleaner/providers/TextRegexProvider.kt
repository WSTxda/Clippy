package com.wstxda.clippy.cleaner.providers

object TextRegexProvider {
    private val schemesRegex by lazy { UrlSchemeProvider.supportedSchemes.joinToString("|") { it.scheme } }
    val urlRegex = """($schemesRegex):\S+""".toRegex()
}