package com.wstxda.clippy.activity.copy

import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.activity.ClipboardLinkActivity
import com.wstxda.clippy.cleaner.tools.UrlCleaner
import kotlinx.coroutines.launch

class CopyLinkCleanerActivity : ClipboardLinkActivity() {

    override fun processLink(link: String) {
        lifecycleScope.launch {
            val cleanedLinks = link.split("\\s+".toRegex()).map { url ->
                UrlCleaner.startUrlCleanerModules(url)
            }
            val finalCleanedLinks = cleanedLinks.joinToString("\n")
            copyLinkToClipboard(finalCleanedLinks)
            showToast(getString(R.string.copy_success))
            finishActivity()
        }
    }
}