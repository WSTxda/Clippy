package com.wstxda.clippy.cleaner.processor

import com.wstxda.clippy.cleaner.data.UrlCleaningModule
import com.wstxda.clippy.cleaner.data.LinkProcessorResult
import com.wstxda.clippy.cleaner.data.ValidationResult
import com.wstxda.clippy.cleaner.tools.TextCleaner
import com.wstxda.clippy.cleaner.tools.UrlCleaner
import com.wstxda.clippy.cleaner.tools.UrlValidator
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object LinkProcessor {

    suspend fun extractAndValidate(
        input: String
    ): Pair<List<String>, List<Pair<String, ValidationResult.Invalid>>> =
        withContext(Dispatchers.Default) {
            Logcat.d(Constants.LINK_PROCESSOR, "Extracting and validating URLs from input")

            // Extract URLs
            val urls = TextCleaner.extract(input)
            Logcat.i(Constants.LINK_PROCESSOR, "Extracted ${urls.size} URL(s)")

            // Validate each URL using functional approach with partition
            val (validUrls, invalidUrls) = urls.map { url -> url to UrlValidator.validate(url) }
                .partition { (_, result) -> result is ValidationResult.Valid }
                .let { (valid, invalid) ->
                    valid.map { it.first } to invalid.map { (url, result) ->
                        url to (result as ValidationResult.Invalid)
                    }
                }

            Logcat.i(
                Constants.LINK_PROCESSOR,
                "Validation complete: ${validUrls.size} valid, ${invalidUrls.size} invalid"
            )
            logInvalidUrls(invalidUrls)

            validUrls to invalidUrls
        }

    suspend fun process(
        url: String, modules: Set<UrlCleaningModule> = UrlCleaningModule.entries.toSet()
    ): LinkProcessorResult {
        Logcat.logUrlProcessingStart(Constants.LINK_PROCESSOR, url)

        // Clean URL with modules
        val cleanedUrl = UrlCleaner.clean(url, modules)
        Logcat.d(Constants.LINK_PROCESSOR, "Cleaned URL: $cleanedUrl")

        // Validate cleaned URL
        val validationResult = UrlValidator.validate(cleanedUrl)

        Logcat.logUrlProcessingSuccess(Constants.LINK_PROCESSOR, cleanedUrl)

        return LinkProcessorResult(
            cleanedUrl = cleanedUrl, validationResult = validationResult
        )
    }

    private fun logInvalidUrls(invalidUrls: List<Pair<String, ValidationResult.Invalid>>) {
        if (invalidUrls.isEmpty()) return

        invalidUrls.forEach { (url, result) ->
            Logcat.w(Constants.LINK_PROCESSOR, "Invalid URL: '$url' (${result.reason})")
        }
    }
}