package com.wstxda.clippy.copy

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.copy.activity.CopyClipboardActivity
import com.wstxda.clippy.tracker.cleaner.TrackerCleaner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CopyLinkRemoveTracker : CopyClipboardActivity() {

    override fun processLink(link: String) {
        if (!isInternetAvailable()) {
            showToastMessage(getString(R.string.copy_no_internet))
            completeActivity()
            return
        }

        lifecycleScope.launch {
            try {
                val cleanedLinks = withContext(Dispatchers.IO) {
                    link.split("\\s+".toRegex()).map { url ->
                        try {
                            TrackerCleaner.cleanUrlOfTrackers(url)
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

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}