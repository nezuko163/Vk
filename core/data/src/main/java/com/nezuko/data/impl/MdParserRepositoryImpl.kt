package com.nezuko.data.impl

import com.nezuko.domain.md.MdBlock
import com.nezuko.domain.repository.md.MdParserRepository
import javax.inject.Inject

class MdParserRepositoryImpl @Inject constructor(): MdParserRepository {
    override fun parseMd(text: String): List<MdBlock> {
        return emptyList()
    }
}