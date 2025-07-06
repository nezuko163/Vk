package com.nezuko.data.md.render

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.nezuko.domain.md.MdBlock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TableViewRender @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val textViewRender: TextViewRender
) : Render {
    override suspend fun create(block: MdBlock): View {
        block as MdBlock.MdTable

        val tableLayout = TableLayout(context).apply {
            // Растягиваем колонки равномерно
            isStretchAllColumns = true
        }

        fun createCellBorder(): GradientDrawable {
            return GradientDrawable().apply {
                setStroke(1.dpToPx(), Color.LTGRAY)
                setColor(Color.TRANSPARENT)
            }
        }

        block.content.forEach { (header, cells) ->
            val tr = TableRow(context)

            textViewRender.create(header).apply {
                this as TextView
                background = createCellBorder()
                setPadding(8.dpToPx(), 4.dpToPx(), 8.dpToPx(), 4.dpToPx())
                setTypeface(typeface, Typeface.BOLD)
            }.also { tr.addView(it) }

            cells.forEach { cell ->
                textViewRender.create(cell).apply {
                    this as TextView
                    background = createCellBorder()
                    setPadding(8.dpToPx(), 4.dpToPx(), 8.dpToPx(), 4.dpToPx())
                }.also { tr.addView(it) }
            }

            tableLayout.addView(tr)
        }

        return HorizontalScrollView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(tableLayout, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
    }

    private fun Int.dpToPx(): Int = (this * context.resources.displayMetrics.density).toInt()
}