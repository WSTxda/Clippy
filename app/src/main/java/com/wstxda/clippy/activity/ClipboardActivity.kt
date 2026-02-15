package com.wstxda.clippy.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.wstxda.clippy.R
import com.wstxda.clippy.databinding.ActivityClipboardBinding

class ClipboardActivity : BaseActivity() {

    private val binding by lazy { ActivityClipboardBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        applySystemBarInsets(binding.navHostContainer)

        setupToolbar(binding.toolbar, showBackButton = false)
        binding.collapsingToolbar.title = getString(R.string.app_clipboard)
    }

    override fun getMenuResId(): Int = R.menu.clipboard_menu

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> false
        }
    }
}