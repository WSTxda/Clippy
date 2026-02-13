package com.wstxda.clippy.cleaner.tools

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.data.ValidationResult
import com.wstxda.clippy.cleaner.providers.UrlSchemeProvider
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat

object UrlValidator {

    fun validate(url: String): ValidationResult {
        Logcat.logToolExecution(Constants.URL_VALIDATOR, "validate")

        if (url.isBlank()) {
            val reason = "URL is empty"
            Logcat.w(Constants.URL_VALIDATOR, reason)
            return ValidationResult.Invalid(reason)
        }

        return try {
            val uri = url.toUri()
            val scheme = uri.scheme

            when {
                scheme == null -> {
                    val reason = "URL scheme is null: $url"
                    Logcat.w(Constants.URL_VALIDATOR, reason)
                    ValidationResult.Invalid(reason)
                }

                !UrlSchemeProvider.isSupported(scheme) -> {
                    val reason = "Scheme '$scheme' is not supported: $url"
                    Logcat.w(Constants.URL_VALIDATOR, reason)
                    ValidationResult.Invalid(reason)
                }

                else -> {
                    Logcat.d(Constants.URL_VALIDATOR, "Valid URL: $url")
                    ValidationResult.Valid
                }
            }
        } catch (e: IllegalArgumentException) {
            val reason = "Malformed URL: $url"
            Logcat.e(Constants.URL_VALIDATOR, reason, e)
            ValidationResult.Invalid(reason, e)
        } catch (e: Exception) {
            val reason = "Unexpected error validating URL: $url"
            Logcat.e(Constants.URL_VALIDATOR, reason, e)
            ValidationResult.Invalid(reason, e)
        }
    }
}