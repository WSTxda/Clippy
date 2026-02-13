package com.wstxda.clippy.cleaner.tools

import com.wstxda.clippy.cleaner.providers.TextRegexProvider
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat

object TextCleaner {

    fun extract(text: String): List<String> {
        Logcat.logToolExecution(Constants.TEXT_CLEANER, "extract")
        Logcat.d(
            Constants.TEXT_CLEANER, "Extracting URLs from text: ${text.take(100)}..."
        )

        val urls = TextRegexProvider.urlRegex.findAll(text).map { it.value }.toList()

        Logcat.i(Constants.TEXT_CLEANER, "Extracted ${urls.size} URL(s)")
        return urls
    }
}