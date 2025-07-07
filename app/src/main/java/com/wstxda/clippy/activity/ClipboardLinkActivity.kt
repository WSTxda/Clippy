package com.wstxda.clippy.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.lifecycleScope
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.processor.LinkProcessor
import com.wstxda.clippy.activity.utils.getSharedLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

abstract class ClipboardLinkActivity : AppCompatActivity() {

    protected abstract val cleanLinks: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shared = intent.getSharedLink()
        if (shared.isNullOrBlank()) {
            finishWithToast(getString(R.string.copy_failure))
            return
        }

        lifecycleScope.launch {
            val (valids, invalids) = LinkProcessor.extractAndValidateLinks(shared)
            if (valids.isEmpty()) {
                if (invalids.isNotEmpty()) LinkProcessor.logInvalids(invalids)
                finishWithToast(getString(R.string.copy_failure_no_valid_links))
                return@launch
            }

            val output = if (cleanLinks) {
                showToast(getString(R.string.copy_process))
                val cleaned = withContext(Dispatchers.IO) { LinkProcessor.cleanLinks(valids) }
                if (cleaned.isEmpty()) {
                    finishWithToast(getString(R.string.copy_failure_empty_after_cleaning))
                    return@launch
                }
                cleaned
            } else {
                valids
            }

            copyToClipboard(output.joinToString("\n"))
            showToast(getString(R.string.copy_success))
            finish()
        }
    }

    private fun copyToClipboard(text: String) {
        getSystemService<ClipboardManager>()?.setPrimaryClip(ClipData.newPlainText("link", text))
            ?: showToast(getString(R.string.copy_failure))
    }

    protected fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    private fun finishWithToast(msg: String) {
        showToast(msg)
        finish()
    }
}