package com.wstxda.clippy.cleaner.tools

import android.net.Uri
import com.wstxda.clippy.cleaner.modules.BuiltinRulesResolver
import com.wstxda.clippy.cleaner.modules.RedirectionHandler
import com.wstxda.clippy.cleaner.modules.ShortenerRemover
import com.wstxda.clippy.cleaner.modules.TrackerRemover
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlCleaner {
    private val applyCleaningModules =
        listOf<suspend (String) -> String>(
            { url -> RedirectionHandler.resolveRedirectionParams(url) },
            { url -> TrackerRemover.removeTrackersParams(url) },
            { url -> ShortenerRemover.removeShortenerParams(url) },
            { url -> BuiltinRulesResolver.applyBuiltinRules(url) })

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