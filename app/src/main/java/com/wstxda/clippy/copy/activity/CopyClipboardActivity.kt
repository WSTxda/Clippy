package com.wstxda.clippy.copy.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.getSystemService
import com.wstxda.clippy.R

abstract class CopyClipboardActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedLink = intent.getStringExtra(Intent.EXTRA_TEXT)?.trim() ?: intent.getStringExtra(
            Intent.EXTRA_SUBJECT
        )?.trim()

        handleLink(sharedLink)
    }

    private fun handleLink(sharedLink: String?) {
        if (!sharedLink.isNullOrEmpty()) {
            processLink(sharedLink)
        } else {
            showToastMessage(getString(R.string.copy_failure))
            completeActivity()
        }
    }

    protected abstract fun processLink(link: String)

    protected fun copyLinkToClipboard(link: CharSequence) {
        val clipboardManager = getSystemService<ClipboardManager>()
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(LINK_LABEL, link))
            showToastMessage(getString(R.string.copy_success))
        } else {
            showToastMessage(getString(R.string.copy_failure_clipboard))
        }
    }

    protected fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun completeActivity() {
        finish()
    }

    companion object {
        const val LINK_LABEL = "link"
    }
}