package com.wstxda.clippy.utils

object Constants {

    // Network Configuration //

    // RedirectionHandler timeouts
    const val REDIRECTION_TIMEOUT_MILLIS = 3000
    const val MAX_REDIRECTS = 3

    // Logging Tags //

    // UI/ViewModel
    const val LINK_CLEANER_BOTTOM_SHEET = "LinkCleanerBottomSheet"
    const val LINK_CLEANER_VIEW_MODEL = "LinkCleanerViewModel"
    // Modules
    const val BUILTIN_RULES_RESOLVER = "BuiltinRulesResolver"
    const val REDIRECTION_HANDLER = "RedirectionHandler"
    const val SHORTENER_REMOVER = "ShortenerRemover"
    const val TRACKER_REMOVER = "TrackerRemover"
    // Processor
    const val LINK_PROCESSOR = "LinkProcessor"
    // Tools
    const val TEXT_CLEANER = "TextCleaner"
    const val URL_CLEANER = "UrlCleaner"
    const val URL_VALIDATOR = "UrlValidator"

    // UI Configuration //

    // Arguments
    const val ARG_LINKS = "arg_links"
    const val ARG_CLEAN = "arg_clean"
    // Animation
    const val ANIMATION_DURATION_MILLIS = 400L
}