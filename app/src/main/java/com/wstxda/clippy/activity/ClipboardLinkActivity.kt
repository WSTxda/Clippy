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
        val extractedLink = sharedLink?.let { TextCleaner.extractUrl(it) }

        if (!extractedLink.isNullOrEmpty()) {
            processLink(extractedLink)
        } else {
            showToast(getString(R.string.copy_failure))
            finishActivity()
        }
    }

    protected abstract fun processLink(link: String)

    protected fun copyLinkToClipboard(link: CharSequence) {
        getSystemService<ClipboardManager>()?.setPrimaryClip(
            ClipData.newPlainText("link", link)
        )
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun finishActivity() {
        finish()
    }
}