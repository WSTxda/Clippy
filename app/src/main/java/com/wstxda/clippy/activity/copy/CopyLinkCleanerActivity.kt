package com.wstxda.clippy.activity.copy

import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.activity.ClipboardLinkActivity
import com.wstxda.clippy.tracker.tools.SharedUrlResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CopyLinkCleanerActivity : ClipboardLinkActivity() {

    override fun processLink(link: String) {
        lifecycleScope.launch {
            try {
                val cleanedLinks = withContext(Dispatchers.IO) {
                    link.split("\\s+".toRegex()).map { url ->
                        try {
                            SharedUrlResolver.startResolveUrlUtils(url)
                        } catch (e: Exception) {
                            url
                        }
                    }
                }

                val finalCleanedLinks = cleanedLinks.joinToString("\n")
                copyLinkToClipboard(finalCleanedLinks)
                showToast(getString(R.string.copy_success))
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(getString(R.string.copy_failure))
            } finally {
                finishActivity()
            }
        }
    }
}