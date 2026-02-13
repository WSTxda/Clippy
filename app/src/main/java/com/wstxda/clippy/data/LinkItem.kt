package com.wstxda.clippy.data

data class LinkItem(

    val inputUrl: String,
    val resultUrl: String? = null,
    val validationErrorRes: Int? = null,
    val isProcessing: Boolean = false
)