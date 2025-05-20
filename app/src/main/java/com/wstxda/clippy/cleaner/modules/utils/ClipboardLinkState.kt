package com.wstxda.clippy.cleaner.modules.utils

sealed class ClipboardLinkState {
    object Idle : ClipboardLinkState()
    object Loading : ClipboardLinkState()
    data class Success(val links: List<String>) : ClipboardLinkState()
    data class Error(val message: String) : ClipboardLinkState()
}