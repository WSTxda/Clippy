package com.wstxda.clippy.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.processor.LinkProcessor
import com.wstxda.clippy.ui.component.LinkCleanerBottomSheet
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.IntentUtils.getSharedLink
import kotlinx.coroutines.launch

abstract class ClipboardLinkActivity : BaseActivity() {

    protected abstract val cleanLinks: Boolean

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
            finishWithToast(getString(R.string.copy_failure))
            return
        }

        lifecycleScope.launch {
            val (validUrls, invalidUrls) = LinkProcessor.extractAndValidateLinks(sharedText)

            if (validUrls.isEmpty()) {
                if (invalidUrls.isNotEmpty()) {
                    LinkProcessor.logInvalidUrls(invalidUrls)
                }
                finishWithToast(getString(R.string.copy_failure_no_valid_links))
                return@launch
            }

            showLinkCleanerBottomSheet(validUrls)
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