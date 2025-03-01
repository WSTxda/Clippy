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
        val cleanedLinks = links.mapNotNull { url ->
            TextCleaner.extractUrl(url)?.takeIf { UrlValidator.isValidUrl(it) }
        }

        if (cleanedLinks.isNotEmpty()) {
            processLink(cleanedLinks.joinToString("\n"))
        } else {
            handleFailure()
        }
    }

    fun handleFailure() {
        showToast(getString(R.string.copy_failure))
        finishActivity()
    }

    protected abstract fun processLink(link: String)

    protected fun copyLinkToClipboard(link: CharSequence) {
        getSystemService<ClipboardManager>()?.setPrimaryClip(
            ClipData.newPlainText("link", link)
        ) ?: showToast(getString(R.string.copy_failure))
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun finishActivity() {
        finish()
    }
}