package com.wstxda.clippy.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.wstxda.clippy.R

abstract class ClipboardLinkActivity : AppCompatActivity() {

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