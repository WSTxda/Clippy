package com.wstxda.clippy.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.tools.TextCleaner
import com.wstxda.clippy.cleaner.tools.UrlValidator

abstract class ClipboardLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val action = intent.action
        val sharedLink = when (action) {
            Intent.ACTION_PROCESS_TEXT -> intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)?.trim()
            Intent.ACTION_SEND -> intent.getStringExtra(Intent.EXTRA_TEXT)?.trim()
                ?: intent.getStringExtra(Intent.EXTRA_SUBJECT)?.trim()

            else -> null
        }

        handleLink(sharedLink)
    }

    private fun handleLink(sharedLink: String?) {
        val links = sharedLink?.split("\\s+".toRegex()) ?: emptyList()
        val validLinks = links.mapNotNull { url ->
            TextCleaner.extractUrl(url)?.takeIf { UrlValidator.isValidUrl(it) }
        }

        if (validLinks.isNotEmpty()) {
            processLink(validLinks)
        } else {
            handleFailure()
        }
    }

    protected fun handleFailure(message: String = getString(R.string.copy_failure)) {
        showToast(message)
        finishActivity()
    }

    protected fun handleSuccess(link: CharSequence) {
        copyLinkToClipboard(link)
        showToast(getString(R.string.copy_success))
        finishActivity()
    }

    protected abstract fun processLink(validLinks: List<String>)

    private fun copyLinkToClipboard(link: CharSequence) {
        getSystemService<ClipboardManager>()?.setPrimaryClip(
            ClipData.newPlainText("link", link)
        ) ?: showToast(getString(R.string.copy_failure))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun finishActivity() {
        finish()
    }
}