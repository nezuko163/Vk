package com.nezuko.data.md.render

import android.view.View
import com.nezuko.domain.md.MdBlock

interface Render {
    suspend fun create(block: MdBlock): View
}