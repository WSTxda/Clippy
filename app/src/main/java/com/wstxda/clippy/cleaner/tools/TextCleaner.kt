package com.wstxda.clippy.cleaner.tools

import com.wstxda.clippy.cleaner.providers.TextRegexProvider

object TextCleaner {

    fun extractUrls(text: String): List<String> {
        return TextRegexProvider.urlRegex.findAll(text).map { it.value }.toList()
    }
}