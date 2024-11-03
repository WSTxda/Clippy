package com.wstxda.clippy.cleaner.modules.utils

data class BuiltinRulesData(
    val pattern: Regex,
    val pathPattern: String? = null,
    val queryPattern: String? = null,
    val apply: (String) -> String
)