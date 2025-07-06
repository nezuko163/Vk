package com.nezuko.vk

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class InsetsWithKeyboardCallback(
    private val window: Window
) : OnApplyWindowInsetsListener {
    init {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            @Suppress("DEPRECATION")
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val systemBarsIMEInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() + WindowInsetsCompat.Type.ime())
        v.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsIMEInsets.bottom)
        return WindowInsetsCompat.CONSUMED
    }

}