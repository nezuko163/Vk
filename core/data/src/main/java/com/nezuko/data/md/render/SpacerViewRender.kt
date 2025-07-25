package com.nezuko.data.md.render

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import com.nezuko.data.utils.dpToPx
import com.nezuko.domain.md.MdBlock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SpacerViewRender @Inject constructor(
    @param:ApplicationContext private val context: Context
) : Render {

    override suspend fun create(block: MdBlock): View {
        return Space(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(16, context)
            )
        }
    }
}