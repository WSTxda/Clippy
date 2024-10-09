package com.wstxda.clippy.tracker.cleaner

import com.wstxda.clippy.tracker.utils.BuiltinRules
import com.wstxda.clippy.tracker.utils.RedirectionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TrackerCleaner {
    suspend fun cleanUrlOfTrackers(url: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val redirectedUrl = RedirectionHandler.handleRedirection(url)
                UrlCleaner.cleanUrlQueryParameters(redirectedUrl)
            } catch (e: Exception) {
                return@withContext url
            }
        }.let { cleanedUrl ->
            BuiltinRules.applyBuiltinRules(cleanedUrl)
        }
    }
}