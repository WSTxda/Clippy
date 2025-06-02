package com.wstxda.clippy.activity.copy

import androidx.activity.viewModels
import com.wstxda.clippy.activity.ClipboardLinkActivity
import com.wstxda.clippy.viewmodel.ClipboardLinkViewModel

class CopyLinkCleanerActivity : ClipboardLinkActivity() {
    override val viewModel: ClipboardLinkViewModel by viewModels()

    override fun onLinkReceived(sharedLink: String) {
        viewModel.processSharedLinks(sharedLink, cleanLinks = true)
    }
}