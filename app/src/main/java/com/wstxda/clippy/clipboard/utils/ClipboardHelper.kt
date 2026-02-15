package com.wstxda.clippy.clipboard.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClipboardHelper private constructor(context: Context) {

    private val clipboardManager: ClipboardManager? = context.getSystemService()

    private val _currentClipboard = MutableStateFlow<String?>(null)
    val currentClipboard: StateFlow<String?> = _currentClipboard.asStateFlow()

    init {
        updateCurrentClipboard()
    }

    fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText("Clippy", text)
        clipboardManager?.setPrimaryClip(clip)
        _currentClipboard.value = text
    }

    fun clearClipboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            clipboardManager?.clearPrimaryClip()
        } else {
            val clip = ClipData.newPlainText("", "")
            clipboardManager?.setPrimaryClip(clip)
        }
        _currentClipboard.value = null
    }

    fun getCurrentClipboardText(): String? {
        return clipboardManager?.primaryClip?.let { clip ->
            if (clip.itemCount > 0) {
                clip.getItemAt(0).text?.toString()
            } else null
        }
    }

    private fun updateCurrentClipboard() {
        _currentClipboard.value = getCurrentClipboardText()
    }
}