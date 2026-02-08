package com.wstxda.clippy.cleaner.tools

import android.util.Log
import androidx.annotation.StringRes
import androidx.core.net.toUri
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.data.ValidationResult
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider
import com.wstxda.clippy.utils.Constants

object UrlValidator {

    fun validate(url: String): ValidationResult {
        if (url.isBlank()) {
            val reason = "URL is blank"
            Log.w(Constants.URL_VALIDATOR, reason)
            return ValidationResult.Invalid(reason)
        }

        return try {
            val uri = url.toUri()
            val scheme = uri.scheme

            when {
                scheme == null -> {
                    val reason = "URL scheme is null for: $url"
                    Log.w(Constants.URL_VALIDATOR, reason)
                    ValidationResult.Invalid(reason)
                }

                !UrlSchemeProvider.isSupported(scheme) -> {
                    val reason = "URL scheme '$scheme' is not supported for: $url"
                    Log.w(Constants.URL_VALIDATOR, reason)
                    ValidationResult.Invalid(reason)
                }

                else -> ValidationResult.Valid
            }
        } catch (e: IllegalArgumentException) {
            val reason = "Malformed URL: $url"
            Log.e(Constants.URL_VALIDATOR, reason, e)
            ValidationResult.Invalid(reason, e)
        } catch (e: Exception) {
            val reason = "Unexpected error during URL validation for: $url"
            Log.e(Constants.URL_VALIDATOR, reason, e)
            ValidationResult.Invalid(reason, e)
        }
    }

    @StringRes
    fun getValidationErrorRes(url: String, isEntry: Boolean = false): Int? {
        val result = validate(url)
        if (result is ValidationResult.Invalid) {
            return if (isEntry) {
                R.string.copy_failure_no_valid_links
            } else {
                if (url.isBlank()) {
                    R.string.copy_failure_empty_after_cleaning
                } else {
                    R.string.copy_failure_error_cleaning
                }
            }
        }
        return null
    }
}