package com.wstxda.clippy.cleaner.tools

import android.util.Log
import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.modules.utils.ValidationResult
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider
import com.wstxda.clippy.utils.Constants

object UrlValidator {

    fun validate(url: String): ValidationResult {
        if (url.isBlank()) {
            val reason = "URL is blank."
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
}