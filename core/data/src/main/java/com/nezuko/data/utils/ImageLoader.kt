package com.nezuko.data.utils

import android.net.Uri
import android.widget.ImageView
import android.widget.ProgressBar

interface ImageLoader {
    fun loadWithProgress(uri: String, into: ImageView, progressBar: ProgressBar)
    fun clearCache()
}
