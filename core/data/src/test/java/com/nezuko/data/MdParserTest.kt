package com.nezuko.data

import com.nezuko.data.md.ParserMdImpl
import com.nezuko.domain.md.Header
import com.nezuko.domain.md.MdBlock
import org.junit.Assert.assertEquals
import org.junit.Test

class MdParserTest {
    val mdParser = ParserMdImpl()
    @Test
    fun headerTest() {
        assertEquals(mdParser.parseMd("# 1").first(), MdBlock.MdText("1", header = Header.FIRST))
        assertEquals(mdParser.parseMd("###### 1").first(), MdBlock.MdText("1", header = Header.SIXTH))
        assertEquals(mdParser.parseMd("####### 1").first(), MdBlock.MdText("# 1", header = Header.SIXTH))
    }

    @Test
    fun paragraphTest() {
        val a = mdParser.parseMd("_1_").first()
        println(a)
        println((a as MdBlock.MdText).italicIndexes.first().contentToString())
        assertEquals(MdBlock.MdText("1", header = Header.FIRST), a)

    }
}