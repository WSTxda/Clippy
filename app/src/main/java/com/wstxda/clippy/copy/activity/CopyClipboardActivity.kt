package com.wstxda.clippy.copy.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
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
            finish()
        }
    }

    protected abstract fun processLink(link: String)

    protected fun copyLinkToClipboard(link: CharSequence) {
        getSystemService<ClipboardManager>()?.setPrimaryClip(
            ClipData.newPlainText("link", link)
        )
    }

    protected fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun completeActivity() {
        finish()
    }
}