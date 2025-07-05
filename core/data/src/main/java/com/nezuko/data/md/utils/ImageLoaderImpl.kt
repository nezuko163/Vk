package com.nezuko.data.md.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.LruCache
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.nezuko.data.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageLoaderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ImageLoader {
    private val cacheSize = (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()
    private val imageCache = object : LruCache<Uri, Bitmap>(cacheSize) {
        override fun sizeOf(key: Uri, value: Bitmap) = value.byteCount / 1024
    }

    override suspend fun load(uri: Uri, into: ImageView) {
        val bitmap = withContext(Dispatchers.IO) {
            imageCache.get(uri) ?: run {
                val bm = loadBitmap(uri, reqWidth = 1080, reqHeight = 1920)
                if (bm != null) imageCache.put(uri, bm)
                bm
            }
        }

        withContext(Dispatchers.Main) {
            into.apply {
                adjustViewBounds = true
                scaleType = ImageView.ScaleType.FIT_CENTER
                setPadding(0, dpToPx(8), 0, dpToPx(8))
            }
            if (bitmap != null) {
                into.setImageBitmap(bitmap)
                val screenW = context.resources.displayMetrics.widthPixels
                val ratio = bitmap.height.toFloat() / bitmap.width
                val h = (screenW * ratio).toInt()
                into.layoutParams = LinearLayout.LayoutParams(screenW, h)
            } else {
                into.setImageResource(R.drawable.close)
                into.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    override fun clearCache() {
        imageCache.evictAll()
    }

    private fun loadBitmap(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        try {
            val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, opts)
            }
            opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight)
            opts.inJustDecodeBounds = false
            return context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, opts)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (h, w) = options.outHeight to options.outWidth
        var inSample = 1
        if (h > reqHeight || w > reqWidth) {
            val halfH = h / 2
            val halfW = w / 2
            while (halfH / inSample >= reqHeight && halfW / inSample >= reqWidth) {
                inSample *= 2
            }
        }
        return inSample
    }

    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}