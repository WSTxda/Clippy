package com.wstxda.clippy.cleaner.provider

object TextRegexProvider {
    val urlRegex = """(https?://\S+)""".toRegex()
}