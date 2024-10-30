package com.wstxda.clippy.activity.copy

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.activity.ClipboardLinkActivity
import com.wstxda.clippy.tracker.tools.SharedUrlResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CopyLinkCleanerActivity : ClipboardLinkActivity() {

    override fun processLink(link: String) {
        if (!isNetworkAvailable()) {
            showToast(getString(R.string.copy_no_internet))
            finishActivity()
            return
        }

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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}