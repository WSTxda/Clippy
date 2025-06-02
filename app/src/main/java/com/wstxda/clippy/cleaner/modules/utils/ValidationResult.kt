package com.wstxda.clippy.cleaner.modules.utils

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val reason: String, val cause: Throwable? = null) : ValidationResult()
}