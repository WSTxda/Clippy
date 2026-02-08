package com.wstxda.clippy.cleaner.data

sealed class ValidationResult {

    data object Valid : ValidationResult()
    data class Invalid(val reason: String, val cause: Throwable? = null) : ValidationResult()
}