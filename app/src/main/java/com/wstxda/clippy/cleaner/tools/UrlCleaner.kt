package com.wstxda.clippy.cleaner.tools

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.data.UrlCleaningModule
import com.wstxda.clippy.cleaner.modules.BuiltinRulesResolver
import com.wstxda.clippy.cleaner.modules.RedirectionHandler
import com.wstxda.clippy.cleaner.modules.ShortenerRemover
import com.wstxda.clippy.cleaner.modules.TrackerRemover
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlCleaner {

    suspend fun clean(
        url: String, modules: Set<UrlCleaningModule> = UrlCleaningModule.entries.toSet()
    ): String = withContext(Dispatchers.IO) {
        Logcat.logToolExecution(Constants.URL_CLEANER, "clean")
        Logcat.logUrlProcessingStart(Constants.URL_CLEANER, url)

        val uri = url.toUri()
        val scheme = uri.scheme

        val result = if (scheme != null && UrlSchemeProvider.needsCleaning(scheme)) {
            applyModules(url, modules)
        } else {
            Logcat.d(Constants.URL_CLEANER, "Scheme doesn't need cleaning: $scheme")
            url
        }

        Logcat.logUrlProcessingSuccess(Constants.URL_CLEANER, result)
        result
    }

    private suspend fun applyModules(
        url: String, modules: Set<UrlCleaningModule>
    ): String {
        val moduleActions = buildModuleActions(modules)

        Logcat.d(Constants.URL_CLEANER, "Applying ${moduleActions.size} module(s)")

        return moduleActions.fold(url) { currentUrl, action ->
            try {
                action(currentUrl)
            } catch (error: Exception) {
                Logcat.e(
                    Constants.URL_CLEANER, "Error applying module: ${error.message}", error
                )
                currentUrl
            }
        }
    }

    private fun buildModuleActions(
        modules: Set<UrlCleaningModule>
    ): List<suspend (String) -> String> {
        val actions = mutableListOf<suspend (String) -> String>()

        if (modules.contains(UrlCleaningModule.REDIRECTION)) {
            actions.add { url ->
                Logcat.logModuleExecution(Constants.URL_CLEANER, "REDIRECTION")
                RedirectionHandler.process(url)
            }
        }

        if (modules.contains(UrlCleaningModule.SHORTENERS)) {
            actions.add { url ->
                Logcat.logModuleExecution(Constants.URL_CLEANER, "SHORTENERS")
                ShortenerRemover.process(url)
            }
        }

        if (modules.contains(UrlCleaningModule.TRACKERS)) {
            actions.add { url ->
                Logcat.logModuleExecution(Constants.URL_CLEANER, "TRACKERS")
                TrackerRemover.process(url)
            }
        }

        if (modules.contains(UrlCleaningModule.BUILTIN)) {
            actions.add { url ->
                Logcat.logModuleExecution(Constants.URL_CLEANER, "BUILTIN")
                BuiltinRulesResolver.process(url)
            }
        }

        return actions
    }
}