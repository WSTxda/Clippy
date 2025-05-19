package com.wstxda.clippy.cleaner.modules

import com.wstxda.clippy.cleaner.providers.TrackingParametersProvider
import androidx.core.net.toUri

object TrackerRemover {
    private val trackingParameters = TrackingParametersProvider.getTrackingFilterList()

    fun removeTrackersParams(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filterNot { trackingParameters.contains(it) }.forEach { param ->
                uri.getQueryParameter(param)?.let { appendQueryParameter(param, it) }
            }
        }.build().toString()
    }
}