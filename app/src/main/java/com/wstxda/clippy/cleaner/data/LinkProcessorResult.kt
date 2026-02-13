package com.wstxda.clippy.cleaner.data

data class LinkProcessorResult(

    val cleanedUrl: String,
    val validationResult: ValidationResult
)