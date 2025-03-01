package com.wstxda.clippy.cleaner.providers

object TextRegexProvider {
    private val schemesRegex = UrlSchemeProvider.supportedSchemes.joinToString("|") { it.scheme }
    val urlRegex = """($schemesRegex):\S+""".toRegex()
}