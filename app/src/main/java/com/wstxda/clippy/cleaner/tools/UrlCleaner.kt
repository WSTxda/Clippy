package com.wstxda.clippy.cleaner.tools

import com.wstxda.clippy.cleaner.modules.TrackerRemover
import com.wstxda.clippy.cleaner.modules.RedirectionHandler
import com.wstxda.clippy.cleaner.modules.ShortenerRemover
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlCleaner {
    suspend fun startUrlCleanerModules(url: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val redirectedRemover = RedirectionHandler.resolveRedirectionParams(url)
                val shortenerRemover = ShortenerRemover.removeShortenerParams(redirectedRemover)
                val trackerRemover = TrackerRemover.removeTrackersParams(shortenerRemover)
                trackerRemover
            } catch (e: Exception) {
                url
            }
        }
    }
}