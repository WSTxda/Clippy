package com.wstxda.clippy.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeNoContrast()
    }

    private fun enableEdgeToEdgeNoContrast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            enableEdgeToEdge(
                navigationBarStyle = SystemBarStyle.auto(
                    lightScrim = Color.TRANSPARENT, darkScrim = Color.TRANSPARENT
                )
            )
            window.isNavigationBarContrastEnforced = false
        }
    }

    protected fun applySystemBarInsets(target: View) {
        ViewCompat.setOnApplyWindowInsetsListener(target) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun finishWithToast(message: String) {
        showToast(message)
        finish()
    }
}