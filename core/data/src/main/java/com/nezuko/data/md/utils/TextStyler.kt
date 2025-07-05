package com.nezuko.data.md.utils

import android.text.SpannableString
import com.nezuko.domain.md.MdBlock

interface TextStyler {
    fun style(mdText: MdBlock.MdText): SpannableString
}