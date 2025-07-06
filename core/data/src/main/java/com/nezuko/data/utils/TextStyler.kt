package com.nezuko.data.utils

import android.text.SpannableString
import com.nezuko.domain.md.MdBlock

interface TextStyler {
    fun style(mdText: MdBlock.MdText): SpannableString
}