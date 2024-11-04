package com.wstxda.clippy.cleaner.tools

import com.wstxda.clippy.cleaner.modules.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlCleaner {
    private val applyCleaningModules = listOf(
        BuiltinRulesResolver::applyBuiltinRules,
        RedirectionHandler::resolveRedirectionParams,
        ShortenerRemover::removeShortenerParams,
        TrackerRemover::removeTrackersParams
    )

    suspend fun startUrlCleanerModules(url: String): String = withContext(Dispatchers.IO) {
        applyCleaningModules.fold(url) { currentUrl, cleaner ->
            cleaner(currentUrl)
        }
    }
}