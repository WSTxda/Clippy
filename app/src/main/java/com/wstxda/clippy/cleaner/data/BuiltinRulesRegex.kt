package com.wstxda.clippy.cleaner.data

data class BuiltinRulesRegex(

    val pattern: Regex,
    val pathPattern: Regex? = null,
    val queryPattern: Regex? = null,
    val apply: (String) -> String
)