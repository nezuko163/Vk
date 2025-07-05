package com.nezuko.domain.md

interface ParserMd {
    fun parseMd(text: String): List<MdBlock>
}