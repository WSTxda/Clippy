package com.wstxda.clippy.cleaner.modules

import android.net.Uri
import com.wstxda.clippy.cleaner.providers.TrackingParametersProvider
import com.wstxda.clippy.cleaner.providers.ShortenerRegexProvider

object TrackerRemover {

    private val trackingParameters: Set<String> = TrackingParametersProvider.getTrackingFilterList()

    fun removeTrackersParams(
        url: String, customTrackingParameters: Set<String> = emptySet()
    ): String {
        val uri = Uri.parse(url) ?: return url

        val allTrackingParameters = trackingParameters + customTrackingParameters

        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { it !in allTrackingParameters && !isTrackingParameter(it) }
                .forEach { param ->
                    uri.getQueryParameter(param)?.let { value ->
                        appendQueryParameter(param, value)
                    }
                }
        }.build().toString()
    }

    private fun isTrackingParameter(param: String): Boolean {
        return ShortenerRegexProvider.shortenerRegexes.any { it.matches(param) }
    }
}