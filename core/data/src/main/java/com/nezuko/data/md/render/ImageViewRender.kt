package com.nezuko.data.md.render

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import com.nezuko.data.md.utils.ImageLoader
import com.nezuko.domain.md.MdBlock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageViewRender @Inject constructor(
    private val imageLoader: ImageLoader,
    @param:ApplicationContext private val context: Context
) : Render {
    suspend fun create(block: MdBlock.MdImage): ImageView = ImageView(context).also { img ->
        imageLoader.load(block.ref.toUri(), img)
    }

    override suspend fun create(block: MdBlock): View {
        block as MdBlock.MdImage
        return ImageView(context).also { img ->
            imageLoader.load(block.ref.toUri(), img)
        }
    }
}
