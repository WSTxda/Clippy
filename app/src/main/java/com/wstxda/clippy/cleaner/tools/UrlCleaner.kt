package com.wstxda.clippy.cleaner.tools

import android.net.Uri
import com.wstxda.clippy.cleaner.modules.*
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlCleaner {
    private val applyCleaningModules = listOf(
        RedirectionHandler::resolveRedirectionParams,
        BuiltinRulesResolver::applyBuiltinRules,
        ShortenerRemover::removeShortenerParams,
        TrackerRemover::removeTrackersParams
    )

    suspend fun startUrlCleanerModules(url: String): String = withContext(Dispatchers.IO) {
        val uri = Uri.parse(url)
        val scheme = uri.scheme
        if (scheme != null && UrlSchemeProvider.needsCleaning(scheme)) {
            applyCleaningModules.fold(url) { currentUrl, cleaner ->
                cleaner(currentUrl)
            }
        } else {
            url
        }
    }
}