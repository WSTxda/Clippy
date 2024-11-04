package com.wstxda.clippy.activity.copy

import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.activity.ClipboardLinkActivity
import com.wstxda.clippy.cleaner.tools.UrlCleaner
import kotlinx.coroutines.launch

class CopyLinkCleanerActivity : ClipboardLinkActivity() {

    override fun processLink(link: String) {
        lifecycleScope.launch {
            try {
                val cleanedLinks = cleanLinks(link)
                if (cleanedLinks.isNotEmpty()) {
                    copyLinkToClipboard(cleanedLinks)
                    showToast(getString(R.string.copy_success))
                } else {
                    handleFailure()
                }
            } catch (e: Exception) {
                handleFailure()
            }
        }
    }

    private suspend fun cleanLinks(link: String): String {
        val cleanedUrls = link.split("\\s+".toRegex()).map { url ->
            UrlCleaner.startUrlCleanerModules(url)
        }
        return cleanedUrls.joinToString("\n").takeIf { it.isNotBlank() } ?: ""
    }
}
