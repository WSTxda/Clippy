package com.wstxda.clippy.cleaner.processor

import android.util.Log
import com.wstxda.clippy.cleaner.data.ValidationResult
import com.wstxda.clippy.cleaner.tools.TextCleaner
import com.wstxda.clippy.cleaner.tools.UrlCleaner
import com.wstxda.clippy.cleaner.tools.UrlValidator
import com.wstxda.clippy.cleaner.data.CleaningModule
import com.wstxda.clippy.utils.Constants

object LinkProcessor {

    fun extractAndValidateLinks(
        input: String
    ): Pair<List<String>, List<Pair<String, ValidationResult.Invalid>>> {
        val urls = TextCleaner.extractUrls(input)
        val validUrls = mutableListOf<String>()
        val invalidUrls = mutableListOf<Pair<String, ValidationResult.Invalid>>()

        for (url in urls) {
            when (val result = UrlValidator.validate(url)) {
                is ValidationResult.Valid -> validUrls.add(url)
                is ValidationResult.Invalid -> invalidUrls.add(url to result)
            }
        }

        return validUrls to invalidUrls
    }

    suspend fun cleanLink(
        url: String, modules: Set<CleaningModule> = CleaningModule.entries.toSet()
    ): String {
        return UrlCleaner.cleanUrl(url, modules)
    }

    fun logInvalidUrls(invalidUrls: List<Pair<String, ValidationResult.Invalid>>) {
        invalidUrls.forEach { (url, result) ->
            Log.w(Constants.LINK_PROCESSOR, "Invalid URL: '$url' (${result.reason})")
        }
    }
}