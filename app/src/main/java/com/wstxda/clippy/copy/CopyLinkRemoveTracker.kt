package com.wstxda.clippy.copy

import android.util.Log
import com.wstxda.clippy.R
import com.wstxda.clippy.copy.activity.CopyClipboardActivity
import com.wstxda.clippy.tracker.cleaner.TrackerCleaner
import com.wstxda.clippy.tracker.utils.CustomTrackerRemover
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CopyLinkRemoveTracker(
    private val trackerCleaner: TrackerCleaner = TrackerCleaner(),
    private val customTrackerRemover: CustomTrackerRemover = CustomTrackerRemover()
) : CopyClipboardActivity() {

    override fun processLink(link: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val cleanedLinks = withContext(Dispatchers.IO) {
                    link.trim().split("\\s+".toRegex())
                        .filter { it.isNotEmpty() }
                        .map { url ->
                            try {
                                val cleanedUrl = trackerCleaner.cleanUrlOfTrackers(url)
                                customTrackerRemover.removeCustomTrackers(cleanedUrl)
                            } catch (e: Exception) {
                                Log.e("CopyLinkRemoveTracker", "Error cleaning URL: $url", e)
                                url
                            }
                        }
                }

                val finalCleanedLinks = cleanedLinks.joinToString("\n")
                copyLinkToClipboard(finalCleanedLinks)
                showToastMessage(getString(R.string.copy_success))
            } catch (e: Exception) {
                Log.e("CopyLinkRemoveTracker", "Error processing link", e)
                showToastMessage(getString(R.string.copy_failure))
            } finally {
                completeActivity()
            }
        }
    }
}