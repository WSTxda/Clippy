package com.wstxda.clippy.cleaner.utils

import androidx.core.net.toUri

object BuiltinRulesUri {

    private val trailingIdRegex = Regex("/[0-9]+/?$")

    fun extractParameter(url: String, paramName: String): String? {
        return url.toUri().getQueryParameter(paramName)
    }

    fun clearQuery(url: String): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().scheme(uri.scheme).authority(uri.authority)
            .path(uri.path).build().toString()
    }

    fun retainParameters(url: String, paramNameRegex: Regex): String {
        val uri = url.toUri()
        return uri.buildUpon().clearQuery().apply {
            uri.queryParameterNames.filter { paramNameRegex.matches(it) }.forEach { param ->
                uri.getQueryParameter(param)?.let { value ->
                    appendQueryParameter(param, value)
                }
            }
        }.scheme(uri.scheme).authority(uri.authority).path(uri.path).build().toString()
    }

    fun clearTrailingId(url: String): String {
        val uri = url.toUri()
        val modifiedPath = uri.path?.replace(trailingIdRegex, "") ?: uri.path
        return uri.buildUpon().path(modifiedPath).scheme(uri.scheme).authority(uri.authority)
            .build().toString()
    }
}