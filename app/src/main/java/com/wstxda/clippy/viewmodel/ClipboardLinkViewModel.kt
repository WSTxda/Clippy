package com.wstxda.clippy.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.modules.utils.ClipboardLinkState
import com.wstxda.clippy.cleaner.modules.utils.ValidationResult
import com.wstxda.clippy.cleaner.tools.TextCleaner
import com.wstxda.clippy.cleaner.tools.UrlCleaner
import com.wstxda.clippy.cleaner.tools.UrlValidator
import com.wstxda.clippy.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ClipboardLinkViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<ClipboardLinkState>(ClipboardLinkState.Idle)
    val state: StateFlow<ClipboardLinkState> = _state

    private fun extractAndValidateLinks(input: String): Pair<List<String>, List<Pair<String, ValidationResult.Invalid>>> {
        val potentialLinks = input.split("\\s+".toRegex())
        val validLinks = mutableListOf<String>()
        val invalidResults = mutableListOf<Pair<String, ValidationResult.Invalid>>()

        potentialLinks.forEach { item ->
            TextCleaner.extractUrl(item)?.let { extractedUrl ->
                when (val validationResult = UrlValidator.validate(extractedUrl)) {
                    is ValidationResult.Valid -> validLinks.add(extractedUrl)
                    is ValidationResult.Invalid -> {
                        invalidResults.add(Pair(extractedUrl, validationResult))
                    }
                }
            }
        }

        return Pair(validLinks, invalidResults)
    }

    fun processSharedLinks(input: String, cleanLinks: Boolean = false) {
        val (validLinks, invalidResults) = extractAndValidateLinks(input)

        if (validLinks.isEmpty()) {
            if (invalidResults.isNotEmpty()) {
                invalidResults.forEach { (url, result) ->
                    Log.w(
                        Constants.CLIPBOARD_LINK_VIEW_MODEL, "Invalid URL '$url': ${result.reason}"
                    )
                }
                Log.w(
                    Constants.CLIPBOARD_LINK_VIEW_MODEL,
                    "No valid links found. ${invalidResults.size} invalid URLs were detected."
                )
            }
            _state.value = ClipboardLinkState.Error(
                getApplication<Application>().getString(R.string.copy_failure_no_valid_links)
            )
            return
        }

        if (cleanLinks) {
            cleanAndCopyLinks(validLinks)
        } else {
            updateStateWithProcessedLinks(validLinks)
        }
    }

    fun updateStateWithProcessedLinks(links: List<String>) {
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
                        getApplication<Application>().getString(R.string.copy_failure_empty_after_cleaning)
                    )
                }
            } catch (e: Exception) {
                Log.e(Constants.CLIPBOARD_LINK_VIEW_MODEL, "Error cleaning links: ${e.message}", e)
                _state.value = ClipboardLinkState.Error(
                    getApplication<Application>().getString(R.string.copy_failure_error_cleaning)
                )
            }
        }
    }
}