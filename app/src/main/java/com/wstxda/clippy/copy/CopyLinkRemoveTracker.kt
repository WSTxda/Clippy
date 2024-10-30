package com.wstxda.clippy.copy

import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.copy.activity.CopyClipboardActivity
import com.wstxda.clippy.tracker.cleaner.TrackerCleaner
import com.wstxda.clippy.tracker.utils.CustomTrackerRemover
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CopyLinkRemoveTracker : CopyClipboardActivity() {

    override fun processLink(link: String) {
        lifecycleScope.launch {
            try {
                val cleanedLinks = withContext(Dispatchers.IO) {
                    link.split("\\s+".toRegex()).map { url ->
                        try {
                            val cleanedUrl = TrackerCleaner.cleanUrlOfTrackers(url)
                            CustomTrackerRemover.removeCustomTrackers(cleanedUrl)
                        } catch (e: Exception) {
                            url
                        }
                    }
                }

                val finalCleanedLinks = cleanedLinks.joinToString("\n")
                copyLinkToClipboard(finalCleanedLinks)
                showToastMessage(getString(R.string.copy_success))
            } catch (e: Exception) {
                e.printStackTrace()
                showToastMessage(getString(R.string.copy_failure))
            } finally {
                completeActivity()
            }
        }
    }
}