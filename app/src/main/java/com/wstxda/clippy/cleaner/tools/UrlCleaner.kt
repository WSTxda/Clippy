package com.wstxda.clippy.cleaner.tools

import com.wstxda.clippy.cleaner.modules.TrackerRemover
import com.wstxda.clippy.cleaner.modules.RedirectionHandler
import com.wstxda.clippy.cleaner.modules.ShortenerRemover
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlCleaner {
    suspend fun startUrlCleanerModules(url: String): String = withContext(Dispatchers.IO) {
        runCatching {
            url.let { RedirectionHandler.resolveRedirectionParams(it) }
                .let { ShortenerRemover.removeShortenerParams(it) }
                .let { TrackerRemover.removeTrackersParams(it) }
        }.getOrElse { url }
    }
}