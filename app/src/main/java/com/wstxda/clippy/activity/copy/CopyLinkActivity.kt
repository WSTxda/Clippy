package com.wstxda.clippy.activity.copy

import com.wstxda.clippy.activity.ClipboardLinkActivity

class CopyLinkActivity : ClipboardLinkActivity() {

    override fun processLink(validLinks: List<String>) {
        handleSuccess(validLinks.joinToString("\n"))
    }
}