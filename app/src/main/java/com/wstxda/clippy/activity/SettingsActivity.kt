package com.wstxda.clippy.activity

import android.os.Bundle
import com.wstxda.clippy.R
import com.wstxda.clippy.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        applySystemBarInsets(binding.navHostContainer)

        setupToolbar(binding.toolbar, showBackButton = true)
        binding.collapsingToolbar.title = getString(R.string.app_settings)
    }
}