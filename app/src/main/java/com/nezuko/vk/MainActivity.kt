package com.nezuko.vk

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nezuko.domain.repository.Navigation
import com.nezuko.main.MainFragmentDirections
import com.nezuko.mdreader.ReaderFragmentDirections
import com.nezuko.mdwriter.WriterFragmentDirections
import com.nezuko.vk.databinding.MainLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainLayoutBinding
    private lateinit var navController: NavController
    private val TAG = "MainActivity"

    @Inject
    lateinit var navigation: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate: zxc")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i(TAG, "onCreate: ASDASD")
        binding.root.addView(statusBarOverlay(), 0)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false


        val callback = InsetsWithKeyboardCallback(window)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root, callback)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = navHostFragment!!.findNavController()

        navigation.apply {
            navigateBack = navController::popBackStack
            navigateFromMainToMdReader = {
                navController.navigate(MainFragmentDirections.fromMainToReader(it))
            }
            navigateFromMainToMdWriter = {
                navController.navigate(MainFragmentDirections.fromMainToWriter(it))
            }
            navigateFromReaderToMdWriter = {
                navController.navigate(ReaderFragmentDirections.fromReaderToWriter(it))
            }
            navigateFromWriterToMdReader = {
                navController.navigate(WriterFragmentDirections.fromWriterToReader(it))
            }
        }
    }

    private fun statusBarOverlay(@ColorRes colorRes: Int = com.nezuko.mdwriter.R.color.light_blue): View  {
        val statusBarHeight = getStatusBarHeight()
        return View(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight
            )
            setBackgroundColor(ContextCompat.getColor(applicationContext, colorRes))
        }
    }


    private fun getStatusBarHeight(): Int {
        val resourceId =
            applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) applicationContext.resources.getDimensionPixelSize(resourceId) else 24
    }


}