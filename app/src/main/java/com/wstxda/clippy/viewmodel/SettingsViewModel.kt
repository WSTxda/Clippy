package com.wstxda.clippy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wstxda.clippy.ui.ThemeManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    fun applyTheme(themeKey: String) {
        ThemeManager.applyTheme(themeKey)
    }
}