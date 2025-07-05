package com.nezuko.data.md.render

import android.content.Context
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import com.nezuko.domain.md.MdBlock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TableViewRender @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val textViewRender: TextViewRender
) : Render {
    override suspend fun create(block: MdBlock): View {
        block as MdBlock.MdTable
        return TableLayout(context).apply {
            block.content.forEach { (hdr, row) ->
                val tr = TableRow(context)
                textViewRender.create(hdr).also {
                    tr.addView(it)
                }

                row.forEach { cell ->
                    textViewRender.create(cell).also {
                        tr.addView(it)
                    }
                }
                addView(tr)
            }
        }
    }
}