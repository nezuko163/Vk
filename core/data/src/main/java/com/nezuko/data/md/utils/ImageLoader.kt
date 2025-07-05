package com.nezuko.data.md.utils

import android.net.Uri
import android.widget.ImageView

interface ImageLoader {
    suspend fun load(uri: Uri, into: ImageView)
    fun clearCache()
}
