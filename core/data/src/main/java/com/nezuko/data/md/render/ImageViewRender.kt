package com.nezuko.data.md.render

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.nezuko.data.utils.ImageLoader
import com.nezuko.data.utils.dpToPx
import com.nezuko.domain.md.MdBlock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageViewRender @Inject constructor(
    private val imageLoader: ImageLoader,
    @param:ApplicationContext private val context: Context
) : Render {
    override suspend fun create(block: MdBlock): View {
        block as MdBlock.MdImage

        val container = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val imageView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val progressBar = ProgressBar(context).apply {
            isIndeterminate = true
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(48, context),
                dpToPx(48, context)
            ).apply {
                gravity = Gravity.CENTER
            }
            visibility = View.VISIBLE
        }

        container.addView(imageView)
        container.addView(progressBar)

        imageLoader.loadWithProgress(block.ref, imageView, progressBar)


        return container
    }

}
