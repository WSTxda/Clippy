package com.wstxda.clippy.activity.utils

import android.content.Intent

fun Intent.getSharedLink(): String? {
    return when (action) {
        Intent.ACTION_PROCESS_TEXT -> getStringExtra(Intent.EXTRA_PROCESS_TEXT)?.trim()
        Intent.ACTION_SEND -> getStringExtra(Intent.EXTRA_TEXT)?.trim()
            ?: getStringExtra(Intent.EXTRA_SUBJECT)?.trim()

        else -> null
    }
}