package com.nezuko.domain.md



enum class Header(val level: Int) {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5),
    SIXTH(6);

    companion object {
        fun from(level: Int) : Header {
            return when(level) {
                1 -> FIRST
                2 -> SECOND
                3 -> THIRD
                4 -> FOURTH
                5 -> FIFTH
                6 -> SIXTH
                else -> throw IllegalArgumentException()
            }
        }
    }
}

sealed class MdBlock {
    data class MdText(
        val text: String,
        var italicIndexes: List<IntArray> = arrayListOf(),
        var boldIndexes: List<IntArray> = arrayListOf(),
        var crossedOutIndexes: List<IntArray> = arrayListOf(),
        var header: Header? = null
    ) : MdBlock()
    class MdEmptyLine : MdBlock()

    data class MdImage(val ref: String) : MdBlock()
    data class MdTable(val content: Map<MdText, List<MdText>>): MdBlock()
}