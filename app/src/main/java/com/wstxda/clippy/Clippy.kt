package com.wstxda.clippy

import android.app.Application
import androidx.preference.PreferenceManager
import com.wstxda.clippy.ui.ThemeManager
import com.wstxda.clippy.utils.Constants

class Clippy : Application() {

    override fun onCreate() {
        super.onCreate()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val selectedTheme = prefs.getString(Constants.THEME_PREF_KEY, Constants.THEME_SYSTEM)
        selectedTheme?.let { ThemeManager.applyTheme(it) }
    }
}