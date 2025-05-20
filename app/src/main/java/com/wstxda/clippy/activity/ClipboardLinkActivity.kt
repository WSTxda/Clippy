package com.wstxda.clippy.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.modules.utils.ClipboardLinkState
import com.wstxda.clippy.viewmodel.ClipboardLinkViewModel
import kotlinx.coroutines.launch

abstract class ClipboardLinkActivity : AppCompatActivity() {

    abstract val viewModel: ClipboardLinkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeState()
        intent.getSharedLink()?.let { sharedLink ->
            onLinkReceived(sharedLink)
        } ?: handleFailure()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ClipboardLinkState.Success -> handleSuccess(state.links.joinToString("\n"))
                        is ClipboardLinkState.Error -> handleFailure(state.message)
                        else -> Unit
                    }
                }
            }
        }
    }

    protected open fun handleFailure(message: String = getString(R.string.copy_failure)) {
        showToast(message)
        finish()
    }

    protected open fun handleSuccess(link: CharSequence) {
        copyLinkToClipboard(link)
        showToast(getString(R.string.copy_success))
        finish()
    }

    private fun copyLinkToClipboard(link: CharSequence) {
        getSystemService<ClipboardManager>()?.setPrimaryClip(
            ClipData.newPlainText("link", link)
        ) ?: showToast(getString(R.string.copy_failure))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected open fun onLinkReceived(sharedLink: String) {
        viewModel.processSharedLinks(sharedLink)
    }
}

fun Intent.getSharedLink(): String? {
    return when (action) {
        Intent.ACTION_PROCESS_TEXT -> getStringExtra(Intent.EXTRA_PROCESS_TEXT)?.trim()
        Intent.ACTION_SEND -> getStringExtra(Intent.EXTRA_TEXT)?.trim()
            ?: getStringExtra(Intent.EXTRA_SUBJECT)?.trim()

        else -> null
    }
}