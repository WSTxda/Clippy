package com.wstxda.clippy.tracker.utils

import android.net.Uri
import com.wstxda.clippy.tracker.cleaner.UrlCleaner
import com.wstxda.clippy.tracker.provider.TrackersParametersProvider

object CustomTrackerRemover {

    private val trackingParameters: Set<String> = TrackersParametersProvider.getTrackersParameters()

    fun removeCustomTrackers(
        url: String, customTrackingParameters: Set<String> = emptySet()
    ): String {
        val uri = Uri.parse(url) ?: return url

        val allTrackingParameters = trackingParameters + customTrackingParameters

        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.forEach { param ->
                if (param !in allTrackingParameters && !matchesTrackingRegex(param)) {
                    uri.getQueryParameter(param)?.let { value ->
                        appendQueryParameter(param, value)
                    }
                }
            }
        }.build().toString()
    }

    private fun matchesTrackingRegex(param: String): Boolean {
        return UrlCleaner.trackingRegexes.any { it.matches(param) }
    }
}