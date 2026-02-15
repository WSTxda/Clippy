package com.wstxda.clippy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wstxda.clippy.clipboard.data.ClipboardCategory
import com.wstxda.clippy.clipboard.data.ClipboardLinkItem
import com.wstxda.clippy.clipboard.utils.ClipboardHelper
import com.wstxda.clippy.clipboard.utils.ClipboardStorage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ClipboardViewModel(
    private val clipboardHelper: ClipboardHelper, private val storage: ClipboardStorage
) : ViewModel() {

    private val _currentClipboard = MutableStateFlow<ClipboardLinkItem?>(null)
    val currentClipboard: StateFlow<ClipboardLinkItem?> = _currentClipboard.asStateFlow()

    private val _savedLinks = MutableStateFlow<List<ClipboardLinkItem>>(emptyList())
    val savedLinks: StateFlow<List<ClipboardLinkItem>> = _savedLinks.asStateFlow()

    private val _historyLinks = MutableStateFlow<List<ClipboardLinkItem>>(emptyList())
    val historyLinks: StateFlow<List<ClipboardLinkItem>> = _historyLinks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    init {
        observeSystemClipboard()
        refreshAllData()
    }

    private fun observeSystemClipboard() {
        viewModelScope.launch {
            clipboardHelper.currentClipboard.collect { clipboardText ->
                handleNewClipboardContent(clipboardText)
            }
        }
    }

    private fun handleNewClipboardContent(text: String?) {
        val newCurrent = text?.takeIf { it.isNotBlank() }?.let {
            ClipboardLinkItem(
                id = it, url = it, category = ClipboardCategory.CURRENT
            )
        }

        if (_currentClipboard.value?.url == newCurrent?.url) return

        _currentClipboard.value = newCurrent

        newCurrent?.let { item ->
            viewModelScope.launch {
                storage.addToHistory(item.url)
                _historyLinks.value = storage.getHistoryLinks()
            }
        }
    }

    fun refreshAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _savedLinks.value = storage.getSavedLinks()
                _historyLinks.value = storage.getHistoryLinks()

                clipboardHelper.getCurrentClipboardText()?.takeIf { it.isNotBlank() }?.let {
                    _currentClipboard.value = ClipboardLinkItem(
                        id = it, url = it, category = ClipboardCategory.CURRENT
                    )
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearClipboard() {
        clipboardHelper.clearClipboard()
        _currentClipboard.value = null
    }

    fun clearHistory() {
        viewModelScope.launch {
            storage.clearHistory()
            _historyLinks.value = emptyList()
        }
    }

    fun copyToClipboard(url: String) {
        clipboardHelper.copyToClipboard(url)
    }

    fun saveLink(item: ClipboardLinkItem) {
        viewModelScope.launch {
            storage.saveLink(item.url)
            _savedLinks.value = storage.getSavedLinks()
        }
    }

    fun deleteFromHistory(item: ClipboardLinkItem) {
        viewModelScope.launch {
            storage.removeFromHistory(item.id)
            _historyLinks.value = storage.getHistoryLinks()
        }
    }

    fun deleteFromSaved(item: ClipboardLinkItem) {
        viewModelScope.launch {
            storage.removeFromSaved(item.id)
            _savedLinks.value = storage.getSavedLinks()
        }
    }
}