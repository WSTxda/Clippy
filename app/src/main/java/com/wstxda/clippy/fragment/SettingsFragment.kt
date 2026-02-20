package com.wstxda.clippy.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.wstxda.clippy.R
import com.wstxda.clippy.activity.LibraryActivity
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.viewmodel.SettingsViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        setupInitialVisibility()
        setupPreferences()
    }

    private fun setupInitialVisibility() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            findPreference<Preference>(Constants.CLEAR_CLIPBOARD_PREF_KEY)?.isVisible = false
        }
    }

    private fun setupPreferences() {
        setupClearClipboardPreference()
        setupThemePreference()
        setupLibraryPreference()
        setupLinkPreferences()
    }

    private fun setupClearClipboardPreference() {
        findPreference<Preference>(Constants.CLEAR_CLIPBOARD_PREF_KEY)?.setOnPreferenceClickListener {
            clearClipboard()
            true
        }
    }

    private fun setupThemePreference() {
        findPreference<ListPreference>(Constants.THEME_PREF_KEY)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.applyTheme(newValue.toString())
            true
        }
    }

    private fun setupLibraryPreference() {
        findPreference<Preference>(Constants.LIBRARY_PREF_KEY)?.setOnPreferenceClickListener {
            val intent = Intent(requireContext(), LibraryActivity::class.java)
            startActivity(intent)
            true
        }
    }

    private fun setupLinkPreferences() {
        links.forEach { (key, url) ->
            findPreference<Preference>(key)?.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                true
            }
        }
    }

    private fun clearClipboard() {
        val context = requireContext()
        val clipboard = context.getSystemService<ClipboardManager>()

        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("", ""))

            // Android 12+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                clipboard.clearPrimaryClip()
            }

            Snackbar.make(
                requireView(), R.string.clipboard_success_clear, Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private val links = mapOf(
        "developer" to "https://github.com/WSTxda",
        "github_repository" to "https://github.com/WSTxda/Clippy",
    )
}