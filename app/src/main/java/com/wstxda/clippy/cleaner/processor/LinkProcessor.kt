package com.wstxda.clippy.cleaner.processor

import android.util.Log
import com.wstxda.clippy.cleaner.modules.utils.ValidationResult
import com.wstxda.clippy.cleaner.tools.TextCleaner
import com.wstxda.clippy.cleaner.tools.UrlCleaner
import com.wstxda.clippy.cleaner.tools.UrlValidator
import com.wstxda.clippy.utils.Constants

object LinkProcessor {

    fun extractAndValidateLinks(input: String): Pair<List<String>, List<Pair<String, ValidationResult.Invalid>>> {
        val tokens = input.split("\\s+".toRegex())
        val valids = mutableListOf<String>()
        val invalids = mutableListOf<Pair<String, ValidationResult.Invalid>>()

        for (token in tokens) {
            TextCleaner.extractUrl(token)?.let { url ->
                when (val result = UrlValidator.validate(url)) {
                    is ValidationResult.Valid -> valids += url
                    is ValidationResult.Invalid -> invalids += url to result
                }
            }
        }
        return valids to invalids
    }

    suspend fun cleanLinks(urls: List<String>): List<String> =
        urls.map { UrlCleaner.startUrlCleanerModules(it) }.filter { it.isNotBlank() }

    fun logInvalids(invalids: List<Pair<String, ValidationResult.Invalid>>) {
        invalids.forEach { (url, res) ->
            Log.w(Constants.LINK_PROCESSOR, "Invalid URL: '$url' (${res.reason})")
        }
    }
}