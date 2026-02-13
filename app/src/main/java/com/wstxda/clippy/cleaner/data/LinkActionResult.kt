package com.wstxda.clippy.cleaner.data

sealed class LinkActionResult {

    data class Success(val text: String) : LinkActionResult()
    data class Error(val errorRes: Int) : LinkActionResult()
}