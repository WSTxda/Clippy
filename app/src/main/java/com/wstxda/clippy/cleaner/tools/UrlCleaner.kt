package com.wstxda.clippy.cleaner.tools

import com.wstxda.clippy.cleaner.modules.TrackerRemover
import com.wstxda.clippy.cleaner.modules.RedirectionHandler
import com.wstxda.clippy.cleaner.modules.ShortenerRemover
import com.wstxda.clippy.cleaner.modules.BuiltinRulesResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlCleaner {

    suspend fun startUrlCleanerModules(url: String): String = withContext(Dispatchers.IO) {
        val processedUrl = applyCleaningModules(url)
        processedUrl
    }

    private fun applyCleaningModules(url: String): String {
        val initialProcessedUrl = BuiltinRulesResolver.applyBuiltinRules(url)
        val redirectionUrl = RedirectionHandler.resolveRedirectionParams(initialProcessedUrl)
        val shortenerUrl = ShortenerRemover.removeShortenerParams(redirectionUrl)
        val trackerUrl = TrackerRemover.removeTrackersParams(shortenerUrl)
        return BuiltinRulesResolver.applyBuiltinRules(trackerUrl)
    }
}