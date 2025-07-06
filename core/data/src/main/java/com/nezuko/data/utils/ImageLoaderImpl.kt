package com.nezuko.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.nezuko.data.R
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageLoaderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val httpClient: HttpClient,
) : ImageLoader {
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private val cacheSize = (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()
    private val imageCache = object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, value: Bitmap) = value.byteCount / 1024
    }

    override fun loadWithProgress(uri: String, into: ImageView, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        into.alpha = 0f
        into.apply {
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.FIT_CENTER
            setPadding(0, dpToPx(8, context), 0, dpToPx(8, context))
        }

        mainScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                imageCache.get(uri) ?: run {
                    val bm = loadBitmapFromUrl(uri)
                    if (bm != null) imageCache.put(uri, bm)
                    bm
                }
            }

            progressBar.visibility = View.GONE

            if (bitmap != null) {
                into.setImageBitmap(bitmap)
                val screenW = context.resources.displayMetrics.widthPixels
                val ratio = bitmap.height.toFloat() / bitmap.width
                val h = (screenW * ratio).toInt()
                into.layoutParams = FrameLayout.LayoutParams(screenW, h)
                into.animate().alpha(1f).setDuration(300).start()
            } else {
                into.setImageResource(R.drawable.close)
                into.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                into.alpha = 1f
            }
        }
    }


    override fun clearCache() {
        imageCache.evictAll()
    }

    private suspend fun loadBitmapFromUrl(url: String): Bitmap? {
        return try {
            val byteArray = httpClient.get(url).body<ByteArray>()
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
