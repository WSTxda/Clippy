package com.wstxda.clippy.data

import androidx.annotation.StringRes

data class LinkItem(

    val originalUrl: String,
    val inputUrl: String,
    val resultUrl: String? = null,
    val isProcessing: Boolean = false,
    val validationError: String? = null,
    @param:StringRes val validationErrorRes: Int? = null
)