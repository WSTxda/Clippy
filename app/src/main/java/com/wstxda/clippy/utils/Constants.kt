package com.wstxda.clippy.utils

object Constants {

    // Preferences keys //

    const val LIBRARY_PREF_KEY = "library"
    const val CLEAR_CLIPBOARD_PREF_KEY = "clear_clipboard"
    const val THEME_PREF_KEY = "select_theme"
    const val SKIP_DIALOG_PREF_KEY = "skip_dialog"
    const val CLEAN_MODULES_PREF_KEY = "clean_modules"
    const val OPEN_IN_BROWSER_PREF_KEY = "open_in_browser"

    // Theme values //

    const val THEME_SYSTEM = "system"
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"

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

    // LinkCleanerBottomSheet //

    // Arguments
    const val ARG_LINKS = "arg_links"
    const val ARG_CLEAN = "arg_clean"
    // Animation
    const val ANIMATION_DURATION_MILLIS = 400L

    // GitHub API releases URL //

    const val GITHUB_RELEASE_URL = "https://api.github.com/repos/WSTxda/Clippy/releases/latest"
}