package com.wstxda.clippy.cleaner.data

import androidx.annotation.StringRes

sealed class LinkActionResult {

    data class Success(val text: String) : LinkActionResult()
    data class Error(@param:StringRes val errorRes: Int) : LinkActionResult()
}