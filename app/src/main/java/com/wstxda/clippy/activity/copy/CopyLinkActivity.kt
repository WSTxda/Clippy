package com.wstxda.clippy.activity.copy

import com.wstxda.clippy.R
import com.wstxda.clippy.activity.ClipboardLinkActivity

class CopyLinkActivity : ClipboardLinkActivity() {

    override fun processLink(link: String) {
        copyLinkToClipboard(link)
        showToast(getString(R.string.copy_success))
        finishActivity()
    }
}