package com.nezuko.vk

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

class InsetsWithKeyboardCallback(
    private val window: Window
) : OnApplyWindowInsetsListener {
    init {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // For better support for devices API 29 and lower
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            @Suppress("DEPRECATION")
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        // System Bars' Insets
        val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //  System Bars' and Keyboard's insets combined
        val systemBarsIMEInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() + WindowInsetsCompat.Type.ime())

        // We use the combined bottom inset of the System Bars and Keyboard to move the view so it doesn't get covered up by the keyboard
        v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsIMEInsets.bottom)
        return WindowInsetsCompat.CONSUMED
    }

}