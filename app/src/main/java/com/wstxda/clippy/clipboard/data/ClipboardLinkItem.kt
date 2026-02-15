package com.wstxda.clippy.clipboard.data

data class ClipboardLinkItem(

    val id: String,
    val url: String,
    val timestamp: Long = System.currentTimeMillis(),
    val category: ClipboardCategory
)