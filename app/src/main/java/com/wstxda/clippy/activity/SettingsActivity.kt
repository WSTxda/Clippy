package com.wstxda.clippy.activity

import android.os.Bundle
import android.view.MenuItem
import com.wstxda.clippy.R
import com.wstxda.clippy.databinding.ActivitySettingsBinding
import com.wstxda.clippy.ui.component.TutorialBottomSheet

class SettingsActivity : BaseActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        applySystemBarInsets(binding.navHostContainer)

        setupToolbar(binding.toolbar, showBackButton = false)
        binding.collapsingToolbar.title = getString(R.string.app_name)
    }

    override fun getMenuResId(): Int = R.menu.main_menu

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_tutorial -> {
                TutorialBottomSheet.show(supportFragmentManager)
                true
            }

            else -> false
        }
    }
}