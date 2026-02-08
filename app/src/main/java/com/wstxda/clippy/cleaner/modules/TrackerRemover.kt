package com.wstxda.clippy.cleaner.modules

import androidx.core.net.toUri
import com.wstxda.clippy.cleaner.providers.TrackingParametersProvider

object TrackerRemover {

    private val trackingParameters by lazy {
        TrackingParametersProvider.getTrackingParameters()
    }

    fun removeTrackers(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filterNot { trackingParameters.contains(it) }.forEach { param ->
                uri.getQueryParameter(param)?.let { value ->
                    appendQueryParameter(param, value)
                }
            }
        }.build().toString()
    }
}