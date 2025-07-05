package com.nezuko.data.md.render

import android.content.Context
import android.view.View
import android.widget.TextView
import com.nezuko.data.md.utils.TextStyler
import com.nezuko.domain.md.Header
import com.nezuko.domain.md.MdBlock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TextViewRender @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val textStyler: TextStyler
) : Render  {
    override suspend fun create(block: MdBlock): View {
        block as MdBlock.MdText
        return TextView(context).apply {
            text = textStyler.style(block)
            textSize = when (block.header) {
                Header.FIRST -> 24f
                Header.SECOND -> 22f
                Header.THIRD -> 20f
                Header.FOURTH -> 18f
                Header.FIFTH -> 16f
                Header.SIXTH -> 14f
                null -> 14f
            }
            setLineSpacing(1.2f, 1.2f)
        }
    }
}