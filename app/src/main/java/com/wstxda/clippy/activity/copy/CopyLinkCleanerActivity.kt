package com.wstxda.clippy.activity.copy

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.activity.ClipboardLinkActivity
import com.wstxda.clippy.cleaner.tools.UrlCleaner
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CopyLinkCleanerActivity : ClipboardLinkActivity() {
    override fun processLink(validLinks: List<String>) {
        lifecycleScope.launch {
            try {
                val cleanedLinks = validLinks.map { url ->
                    async { UrlCleaner.startUrlCleanerModules(url) }
                }.awaitAll().filter { it.isNotBlank() }

                if (cleanedLinks.isNotEmpty()) {
                    handleSuccess(cleanedLinks.joinToString("\n"))
                } else {
                    handleFailure(getString(R.string.copy_failure_no_valid_links))
                }
            } catch (e: Exception) {
                Log.e("CopyLinkCleaner", "Error cleaning links: ${e.message}", e)
                handleFailure(getString(R.string.copy_failure_error_cleaning))
            }
        }
    }
}