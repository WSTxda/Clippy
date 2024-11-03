package com.wstxda.clippy.cleaner.providers

object TextRegexProvider {
    val urlRegex = """(https?://\S+)""".toRegex()
}