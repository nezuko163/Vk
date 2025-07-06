package com.nezuko.data.md

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.nezuko.data.md.render.ImageViewRender
import com.nezuko.data.md.render.SpacerViewRender
import com.nezuko.data.md.render.TableViewRender
import com.nezuko.data.md.render.TextViewRender
import com.nezuko.domain.md.MdBlock
import com.nezuko.domain.repository.md.MdRenderer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MdRendererImpl @Inject constructor(
    private val textFactory: TextViewRender,
    private val imageFactory: ImageViewRender,
    private val tableFactory: TableViewRender,
    private val spacerFactory: SpacerViewRender,
    @param:ApplicationContext private val context: Context
) : MdRenderer {
    override suspend fun render(blocks: List<MdBlock>): View {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            blocks.forEach { block ->
                val child: View = when (block) {
                    is MdBlock.MdText      -> textFactory.create(block)
                    is MdBlock.MdImage     -> imageFactory.create(block)
                    is MdBlock.MdTable     -> tableFactory.create(block)
                    is MdBlock.MdEmptyLine -> spacerFactory.create(block)
                }
                addView(child)
            }
        }

        return ScrollView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(container, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
    }

}
