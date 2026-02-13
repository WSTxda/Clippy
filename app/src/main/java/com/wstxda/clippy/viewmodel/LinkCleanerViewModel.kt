package com.wstxda.clippy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.data.UrlCleaningModule
import com.wstxda.clippy.cleaner.data.LinkActionResult
import com.wstxda.clippy.cleaner.data.ValidationResult
import com.wstxda.clippy.cleaner.processor.LinkProcessor
import com.wstxda.clippy.data.LinkItem
import com.wstxda.clippy.data.LinkProcessState
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LinkCleanerViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(
        LinkProcessState(
            isCleanAllEnabled = true, selectedModules = UrlCleaningModule.entries.toSet()
        )
    )

    val state: StateFlow<LinkProcessState> = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LinkProcessState(
            isCleanAllEnabled = true, selectedModules = UrlCleaningModule.entries.toSet()
        )
    )

    fun setInitialLinks(links: List<String>) {
        if (_state.value.links.isNotEmpty()) {
            Logcat.d(Constants.LINK_CLEANER_VIEW_MODEL, "Links already set, ignoring")
            return
        }

        Logcat.d(Constants.LINK_CLEANER_VIEW_MODEL, "Setting ${links.size} initial link(s)")

        val items = links.map { url ->
            LinkItem(inputUrl = url, resultUrl = url)
        }
        _state.update { currentState ->
            currentState.copy(links = items)
        }
    }

    fun setCleanAllEnabled(enabled: Boolean) {
        _state.update { currentState ->
            currentState.copy(isCleanAllEnabled = enabled)
        }
    }

    fun updateModuleSelection(module: UrlCleaningModule, isChecked: Boolean) {
        _state.update { currentState ->
            val updatedModules = currentState.selectedModules.toMutableSet().apply {
                if (isChecked) add(module) else remove(module)
            }
            currentState.copy(selectedModules = updatedModules)
        }
    }

    fun processLinks() {
        viewModelScope.launch {
            try {
                setProcessingState()

                val modulesToUse = getModulesToApply()
                Logcat.i(
                    Constants.LINK_CLEANER_VIEW_MODEL,
                    "Processing links with ${modulesToUse.size} module(s): ${modulesToUse.joinToString { it.name }}"
                )

                val processedLinks = processAllLinks(modulesToUse)

                _state.update { currentState ->
                    currentState.copy(
                        links = processedLinks, isGlobalProcessing = false, isFinished = true
                    )
                }

                Logcat.i(Constants.LINK_CLEANER_VIEW_MODEL, "All links processed successfully")
            } catch (e: Exception) {
                Logcat.e(Constants.LINK_CLEANER_VIEW_MODEL, "Error processing links", e)
                _state.update { currentState ->
                    currentState.copy(isGlobalProcessing = false)
                }
            }
        }
    }

    fun getCopyResult(): LinkActionResult {
        val itemsToCopy = _state.value.links

        if (itemsToCopy.isEmpty()) {
            return LinkActionResult.Error(R.string.copy_failure_no_valid_links)
        }

        // Check if there's any validation error
        val firstError = itemsToCopy.firstNotNullOfOrNull { it.validationErrorRes }
        if (firstError != null) {
            return LinkActionResult.Error(firstError)
        }

        val text = itemsToCopy.joinToString("\n") { link ->
            link.resultUrl ?: link.inputUrl
        }

        return if (text.isBlank()) {
            LinkActionResult.Error(R.string.copy_failure_empty_after_cleaning)
        } else {
            LinkActionResult.Success(text)
        }
    }

    private fun setProcessingState() {
        _state.update { currentState ->
            currentState.copy(
                isGlobalProcessing = true, links = currentState.links.map { link ->
                    link.copy(isProcessing = true)
                })
        }
    }

    private fun getModulesToApply(): Set<UrlCleaningModule> {
        return if (_state.value.isCleanAllEnabled) {
            UrlCleaningModule.entries.toSet()
        } else {
            _state.value.selectedModules
        }
    }

    private suspend fun processAllLinks(
        modules: Set<UrlCleaningModule>
    ): List<LinkItem> = coroutineScope {
        _state.value.links.map { item ->
            async {
                processLinkItem(item, modules)
            }
        }.awaitAll()
    }

    private suspend fun processLinkItem(
        item: LinkItem, modules: Set<UrlCleaningModule>
    ): LinkItem {
        return try {
            val result = LinkProcessor.process(item.inputUrl, modules)

            item.copy(
                resultUrl = result.cleanedUrl,
                validationErrorRes = if (result.validationResult is ValidationResult.Invalid) {
                    R.string.copy_failure_error_cleaning
                } else {
                    null
                },
                isProcessing = false
            )
        } catch (e: Exception) {
            Logcat.e(
                Constants.LINK_CLEANER_VIEW_MODEL, "Error processing link: ${item.inputUrl}", e
            )
            item.copy(
                validationErrorRes = R.string.copy_failure_error_cleaning, isProcessing = false
            )
        }
    }
}