package com.wstxda.clippy.cleaner.modules

import android.net.Uri
import com.wstxda.clippy.cleaner.providers.TrackingParametersProvider

object TrackerRemover {

    private val trackingParameters = TrackingParametersProvider.getTrackingFilterList()

    fun removeTrackersParams(url: String): String {
        val uri = Uri.parse(url) ?: return url

        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filterNot { trackingParameters.contains(it) }.forEach { param ->
                uri.getQueryParameter(param)?.let { appendQueryParameter(param, it) }
            }
        }.build().toString()
    }
}