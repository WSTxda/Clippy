package com.wstxda.clippy.tracker.cleaner

import com.wstxda.clippy.tracker.utils.BuiltinRules
import com.wstxda.clippy.tracker.utils.RedirectionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TrackerCleaner {
    suspend fun removeTrackers(url: String): String {
        return withContext(Dispatchers.IO) {
            val redirectedUrl = RedirectionHandler.handleRedirection(url)
            UrlCleaner.cleanUrlQueryParameters(redirectedUrl)
        }.let { cleanedUrl ->
            BuiltinRules.applyBuiltinRules(cleanedUrl)
        }
    }
}