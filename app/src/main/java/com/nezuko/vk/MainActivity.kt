package com.nezuko.vk

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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

        window.statusBarColor =
            ContextCompat.getColor(applicationContext, com.nezuko.main.R.color.light_blue)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }


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
}