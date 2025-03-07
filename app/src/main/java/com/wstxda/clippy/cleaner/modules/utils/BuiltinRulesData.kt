package com.wstxda.clippy.cleaner.modules.utils

data class BuiltinRulesData(
    val pattern: Regex,
    val pathPattern: Regex? = null,
    val queryPattern: Regex? = null,
    val apply: (String) -> String
)