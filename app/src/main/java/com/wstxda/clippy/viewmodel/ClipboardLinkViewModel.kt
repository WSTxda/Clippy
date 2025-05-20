package com.wstxda.clippy.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.modules.utils.ClipboardLinkState
import com.wstxda.clippy.cleaner.tools.TextCleaner
import com.wstxda.clippy.cleaner.tools.UrlValidator
import com.wstxda.clippy.cleaner.tools.UrlCleaner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClipboardLinkViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<ClipboardLinkState>(ClipboardLinkState.Idle)
    val state: StateFlow<ClipboardLinkState> = _state

    fun processSharedLinks(input: String) {
        val links = input.split("\\s+".toRegex()).mapNotNull { url ->
            TextCleaner.extractUrl(url)?.takeIf { UrlValidator.isValidUrl(it) }
        }

        if (links.isEmpty()) {
            _state.value = ClipboardLinkState.Error(
                getApplication<Application>().getString(R.string.copy_failure_no_valid_links)
            )
            return
        }
        copyLinks(links)
    }

    fun processSharedLinksAndClean(input: String) {
        val links = input.split("\\s+".toRegex()).mapNotNull { url ->
            TextCleaner.extractUrl(url)?.takeIf { UrlValidator.isValidUrl(it) }
        }

        if (links.isEmpty()) {
            _state.value = ClipboardLinkState.Error(
                getApplication<Application>().getString(R.string.copy_failure_no_valid_links)
            )
            return
        }
        cleanAndCopyLinks(links)
    }

    fun copyLinks(links: List<String>) {
        _state.value = ClipboardLinkState.Success(links)
    }

    fun cleanAndCopyLinks(links: List<String>) {
        viewModelScope.launch {
            _state.value = ClipboardLinkState.Loading
            try {
                val cleanedLinks = links.map { url ->
                    async(Dispatchers.IO) { UrlCleaner.startUrlCleanerModules(url) }
                }.awaitAll().filter { it.isNotBlank() }

                if (cleanedLinks.isNotEmpty()) {
                    _state.value = ClipboardLinkState.Success(cleanedLinks)
                } else {
                    _state.value = ClipboardLinkState.Error(
                        getApplication<Application>().getString(R.string.copy_failure_no_valid_links)
                    )
                }
            } catch (e: Exception) {
                Log.e("ClipboardLinkViewModel", "Error cleaning links: ${e.message}", e)
                _state.value = ClipboardLinkState.Error(
                    getApplication<Application>().getString(R.string.copy_failure_error_cleaning)
                )
            }
        }
    }
}