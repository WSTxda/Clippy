package com.wstxda.clippy.copy

import com.wstxda.clippy.R
import com.wstxda.clippy.copy.activity.CopyClipboardActivity

class CopyLinkStandard : CopyClipboardActivity() {

    override fun processLink(link: String) {
        copyLinkToClipboard(link)
        showToastMessage(getString(R.string.copy_success))
        completeActivity()
    }
}