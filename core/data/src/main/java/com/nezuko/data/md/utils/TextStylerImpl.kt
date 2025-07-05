package com.nezuko.data.md.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import com.nezuko.domain.md.MdBlock
import javax.inject.Inject

class TextStylerImpl @Inject constructor() : TextStyler {
    override fun style(mdText: MdBlock.MdText): SpannableString {
        val spannable = SpannableString(mdText.text)

        mdText.italicIndexes.forEach { (start, end) ->
            spannable.setSpan(
                StyleSpan(Typeface.ITALIC), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (mdText.header == null) {
            mdText.boldIndexes.forEach { (start, end) ->
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        } else {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD), 0, mdText.text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        mdText.crossedOutIndexes.forEach { (start, end) ->
            spannable.setSpan(
                StrikethroughSpan(), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannable
    }
}
