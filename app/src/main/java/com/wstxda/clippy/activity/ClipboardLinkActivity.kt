package com.wstxda.clippy.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.data.UrlCleaningModule
import com.wstxda.clippy.cleaner.processor.LinkProcessor
import com.wstxda.clippy.logic.PreferenceHelper
import com.wstxda.clippy.ui.component.LinkCleanerBottomSheet
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Intents.getSharedLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ClipboardLinkActivity : BaseActivity() {

    protected abstract val cleanLinks: Boolean
    private val preferenceHelper by lazy { PreferenceHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRootView()
        processSharedLink()
    }

    private fun setupRootView() {
        val rootView = View(this)
        setContentView(rootView)
        applySystemBarInsets(target = rootView)
    }

    private fun processSharedLink() {
        val sharedText = intent.getSharedLink()

        if (sharedText.isNullOrBlank()) {
            finishWithToast(getString(R.string.copy_failure_general))
            return
        }

        lifecycleScope.launch {
            // Run URL extraction and validation on Default dispatcher
            val (validUrls) = withContext(Dispatchers.Default) {
                LinkProcessor.extractAndValidate(sharedText)
            }

            // Check lifecycle before updating UI
            if (isFinishing || isDestroyed) {
                return@launch
            }

            if (validUrls.isEmpty()) {
                finishWithToast(getString(R.string.copy_failure_no_valid_links))
                return@launch
            }

            val skipDialog = preferenceHelper.getBoolean(Constants.SKIP_DIALOG_PREF_KEY, false)
            if (skipDialog) {
                processAndCopyDirectly(validUrls)
            } else {
                showLinkCleanerBottomSheet(validUrls)
            }
        }
    }

    private fun processAndCopyDirectly(links: List<String>) {
        lifecycleScope.launch {
            val modules = if (cleanLinks) {
                preferenceHelper.getStringSet(
                    Constants.CLEAN_MODULES_PREF_KEY,
                    UrlCleaningModule.entries.map { it.name }.toSet()
                ).map { UrlCleaningModule.valueOf(it) }.toSet()
            } else {
                emptySet()
            }

            val processedLinks = withContext(Dispatchers.Default) {
                links.map { url ->
                    async {
                        LinkProcessor.process(url, modules).cleanedUrl
                    }
                }.awaitAll()
            }

            val textToCopy = processedLinks.joinToString("\n")
            copyToClipboard(textToCopy)

            val openInBrowser =
                preferenceHelper.getBoolean(Constants.OPEN_IN_BROWSER_PREF_KEY, false)
            if (openInBrowser && processedLinks.isNotEmpty()) {
                openInBrowser(processedLinks.first())
            }

            finishWithToast(getString(R.string.copy_success_clipboard))
        }
    }

    private fun copyToClipboard(text: String) {
        getSystemService<ClipboardManager>()?.setPrimaryClip(
            ClipData.newPlainText("link", text)
        )
    }

    private fun openInBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (_: Exception) {

            finishWithToast(getString(R.string.copy_failure_open_browser))
        }
    }

    private fun showLinkCleanerBottomSheet(links: List<String>) {
        val dialog = LinkCleanerBottomSheet().apply {
            arguments = Bundle().apply {
                putStringArrayList(Constants.ARG_LINKS, ArrayList(links))
                putBoolean(Constants.ARG_CLEAN, cleanLinks)
            }
        }

        dialog.onFinishedWithToast = { message ->
            if (message != null) {
                finishWithToast(message)
            } else {
                finish()
            }
        }
        dialog.show(supportFragmentManager, Constants.LINK_CLEANER_BOTTOM_SHEET)
    }
}