package com.wstxda.clippy.tracker.tools

import com.wstxda.clippy.tracker.utils.CustomTrackerRemover
import com.wstxda.clippy.tracker.utils.RedirectionHandler
import com.wstxda.clippy.tracker.utils.ShortenerRemover
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SharedUrlResolver {
    suspend fun startResolveUrlUtils(url: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val redirectedUrl = RedirectionHandler.handleRedirection(url)
                val cleanedUrl = ShortenerRemover.removeShortenerParamsFromUrl(redirectedUrl)
                CustomTrackerRemover.removeCustomTrackers(cleanedUrl)
            } catch (e: Exception) {
                url
            }
        }
    }
}