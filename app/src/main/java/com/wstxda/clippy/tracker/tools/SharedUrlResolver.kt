package com.wstxda.clippy.tracker.tools

import com.wstxda.clippy.tracker.utils.CustomTrackerRemover
import com.wstxda.clippy.tracker.utils.RedirectionHandler
import com.wstxda.clippy.tracker.utils.ShortenerRemover
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SharedUrlResolver {
    suspend fun startResolveUrlUtils(url: String): String {
        return withContext(Dispatchers.IO) {
            val redirectedRemover = safeUrlProcessing(url) { RedirectionHandler.handleRedirection(it) }
            val shortenerRemover = ShortenerRemover.removeShortenerParamsFromUrl(redirectedRemover)
            CustomTrackerRemover.removeCustomTrackers(shortenerRemover)
        }
    }

    private fun safeUrlProcessing(url: String, process: (String) -> String): String {
        return try {
            process(url)
        } catch (e: Exception) {
            url
        }
    }
}