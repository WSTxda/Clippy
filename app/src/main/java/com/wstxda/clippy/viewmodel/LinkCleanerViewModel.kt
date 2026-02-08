package com.wstxda.clippy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.processor.LinkProcessor
import com.wstxda.clippy.cleaner.tools.UrlValidator
import com.wstxda.clippy.cleaner.data.CleaningModule
import com.wstxda.clippy.cleaner.data.LinkActionResult
import com.wstxda.clippy.data.LinkItem
import com.wstxda.clippy.data.LinkProcessState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LinkCleanerViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        LinkProcessState(
            isCleanAllEnabled = true, selectedModules = CleaningModule.entries.toSet()
        )
    )
    val state: StateFlow<LinkProcessState> = _state

    fun setInitialLinks(links: List<String>) {
        if (_state.value.links.isEmpty()) {
            _state.update { currentState ->
                currentState.copy(
                    links = links.map { url -> LinkItem(url, url) })
            }
        }
    }

    fun setCleanAllEnabled(enabled: Boolean) {
        _state.update { it.copy(isCleanAllEnabled = enabled) }
    }

    fun updateModuleSelection(module: CleaningModule, isChecked: Boolean) {
        _state.update { currentState ->
            val updatedModules = currentState.selectedModules.toMutableSet()
            if (isChecked) {
                updatedModules.add(module)
            } else {
                updatedModules.remove(module)
            }
            currentState.copy(selectedModules = updatedModules)
        }
    }

    fun getCopyResult(): LinkActionResult {
        val itemsToCopy = _state.value.links

        if (itemsToCopy.isEmpty()) {
            return LinkActionResult.Error(R.string.copy_failure_no_valid_links)
        }

        val firstError = itemsToCopy.firstNotNullOfOrNull { it.validationErrorRes }
        if (firstError != null) {
            return LinkActionResult.Error(firstError)
        }

        val text = buildCopyText(itemsToCopy)
        if (text.isBlank()) {
            return LinkActionResult.Error(R.string.copy_failure_empty_after_cleaning)
        }

        return LinkActionResult.Success(text)
    }

    fun processLinks() {
        viewModelScope.launch {
            setProcessingState()

            val modulesToUse = getModulesToApply()
            val processedLinks = processAllLinks(modulesToUse)

            _state.update { currentState ->
                currentState.copy(
                    links = processedLinks, isGlobalProcessing = false, isFinished = true
                )
            }
        }
    }

    private fun buildCopyText(items: List<LinkItem>): String {
        return items.joinToString("\n") { it.resultUrl ?: it.inputUrl }
    }

    private fun setProcessingState() {
        _state.update { currentState ->
            currentState.copy(
                isGlobalProcessing = true,
                links = currentState.links.map { it.copy(isProcessing = true) })
        }
    }

    private fun getModulesToApply(): Set<CleaningModule> {
        return if (_state.value.isCleanAllEnabled) {
            CleaningModule.entries.toSet()
        } else {
            _state.value.selectedModules
        }
    }

    private suspend fun processAllLinks(modules: Set<CleaningModule>): List<LinkItem> =
        coroutineScope {
            _state.value.links.map { item ->
                async { processLinkItem(item, modules) }
            }.awaitAll()
        }

    private suspend fun processLinkItem(
        item: LinkItem, modules: Set<CleaningModule>
    ): LinkItem {
        val result = LinkProcessor.cleanLink(item.inputUrl, modules)
        val errorRes = UrlValidator.getValidationErrorRes(result)

        return item.copy(
            resultUrl = result.ifBlank { item.inputUrl },
            validationErrorRes = errorRes,
            isProcessing = false
        )
    }
}