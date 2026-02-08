package com.wstxda.clippy.cleaner.tools

import android.util.Log
import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.modules.BuiltinRulesResolver
import com.wstxda.clippy.cleaner.modules.RedirectionHandler
import com.wstxda.clippy.cleaner.modules.ShortenerRemover
import com.wstxda.clippy.cleaner.modules.TrackerRemover
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider
import com.wstxda.clippy.cleaner.data.CleaningModule
import com.wstxda.clippy.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlCleaner {

    suspend fun cleanUrl(
        url: String, modules: Set<CleaningModule> = CleaningModule.entries.toSet()
    ): String = withContext(Dispatchers.IO) {
        val uri = url.toUri()
        val scheme = uri.scheme

        if (scheme != null && UrlSchemeProvider.needsCleaning(scheme)) {
            applyCleaningModules(url, modules)
        } else {
            url
        }
    }

    private suspend fun applyCleaningModules(
        url: String, modules: Set<CleaningModule>
    ): String {
        val moduleActions = buildModuleActions(modules)

        return moduleActions.fold(url) { currentUrl, cleaner ->
            try {
                cleaner(currentUrl)
            } catch (error: Exception) {
                Log.e(
                    Constants.URL_CLEANER,
                    "Error applying cleaner module to URL: ${error.message}",
                    error
                )
                currentUrl
            }
        }
    }

    private fun buildModuleActions(
        modules: Set<CleaningModule>
    ): List<suspend (String) -> String> {
        val actions = mutableListOf<suspend (String) -> String>()

        if (modules.contains(CleaningModule.SHORTENERS)) {
            actions.add { url -> ShortenerRemover.removeShortenerParams(url) }
        }
        if (modules.contains(CleaningModule.TRACKERS)) {
            actions.add { url -> TrackerRemover.removeTrackers(url) }
        }
        if (modules.contains(CleaningModule.REDIRECTION)) {
            actions.add { url -> RedirectionHandler.resolveRedirects(url) }
        }
        if (modules.contains(CleaningModule.BUILTIN)) {
            actions.add { url -> BuiltinRulesResolver.applyBuiltinRules(url) }
        }

        return actions
    }
}