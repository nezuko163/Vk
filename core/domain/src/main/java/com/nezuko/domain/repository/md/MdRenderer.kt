package com.nezuko.domain.repository.md

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nezuko.domain.md.MdBlock

interface MdRenderer {
    suspend fun render(blocks: List<MdBlock>): View
}