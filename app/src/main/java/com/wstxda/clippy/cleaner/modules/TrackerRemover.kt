package com.wstxda.clippy.cleaner.modules

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.providers.TrackingParametersProvider
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.utils.Logcat

object TrackerRemover {

    fun process(url: String): String {
        Logcat.d(Constants.TRACKER_REMOVER, "Processing URL: $url")

        val uri = url.toUri()
        val initialParams = uri.queryParameterNames.size

        val result = uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filterNot {
                TrackingParametersProvider.trackingParameters.contains(
                    it
                )
            }.forEach { param ->
                uri.getQueryParameter(param)?.let { value ->
                    appendQueryParameter(param, value)
                }
            }
        }.build().toString()

        val finalParams = result.toUri().queryParameterNames.size
        val removed = initialParams - finalParams

        if (removed > 0) {
            Logcat.i(Constants.TRACKER_REMOVER, "Removed $removed tracking parameter(s)")
        } else {
            Logcat.d(Constants.TRACKER_REMOVER, "No tracking parameters found")
        }

        return result
    }
}