package com.nezuko.domain.repository.md

import com.nezuko.domain.md.MdBlock

interface MdParserRepository {
    fun parseMd(text: String): List<MdBlock>
}