package com.nezuko.data.md

import android.R.attr.level
import android.util.Log.i
import com.nezuko.domain.md.Header
import com.nezuko.domain.md.MdBlock
import com.nezuko.domain.md.ParserMd
import javax.inject.Inject


class ParserMdImpl @Inject constructor() : ParserMd {
    override fun parseMd(text: String): List<MdBlock> {
        return emptyList()
    }

}
