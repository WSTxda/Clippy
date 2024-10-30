package com.wstxda.clippy.tracker.utils

import android.net.Uri
import com.wstxda.clippy.tracker.provider.TrackingParametersProvider

object CustomTrackerRemover {

    private val trackingParameters: Set<String> = TrackingParametersProvider.getTrackingFilterList()

    fun removeCustomTrackers(
        url: String, customTrackingParameters: Set<String> = emptySet()
    ): String {
        val uri = Uri.parse(url) ?: return url

        val allTrackingParameters = trackingParameters + customTrackingParameters

        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.forEach { param ->
                if (param !in allTrackingParameters && !isTrackingParameter(param)) {
                    uri.getQueryParameter(param)?.let { value ->
                        appendQueryParameter(param, value)
                    }
                }
            }
        }.build().toString()
    }

    private fun isTrackingParameter(param: String): Boolean {
        return ShortenerRemover.shortenerRegexes.any { it.matches(param) }
    }
}