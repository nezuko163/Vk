package com.nezuko.data.md

import com.nezuko.domain.md.Header
import com.nezuko.domain.md.MdBlock
import com.nezuko.domain.repository.md.MdParserRepository
import javax.inject.Inject
import kotlin.collections.iterator


class MdParserImpl @Inject constructor() : MdParserRepository {
    override fun parseMd(text: String): List<MdBlock> {
        val blocks = mutableListOf<MdBlock>()
        val lines = text.lines()
        var isPrevLineEmpty = false
        var i = 0

        val tableSepPattern = Regex(
            "^\\s*\\|?\\s*:?[-]+:?\\s*(\\|\\s*:?[-]+:?\\s*)+\\|?\\s*$"
        )

        while (i < lines.size) {
            val rawLine = lines[i]
            val trimmed = cleanSpaces(rawLine.trim())

            if (trimmed.isEmpty()) {
                if (!isPrevLineEmpty) {
                    blocks.add(MdBlock.MdEmptyLine())
                }
                isPrevLineEmpty = true
                i++
                continue
            }
            isPrevLineEmpty = false

            // Заголовки (#...)
            if (trimmed.startsWith("#")) {
                blocks.add(parseHeader(trimmed))
                i++
                continue
            }

            // Изображения
            val match = Regex("^!\\[.*?]\\((.*?)\\)").find(trimmed)
            if (match != null) {
                blocks.add(MdBlock.MdImage(match.groupValues[1]))
                i++
                continue
            }

            // Таблицы: проверка заголовка и следующей строки-сепаратора
            if (trimmed.contains("|") && i + 1 < lines.size && tableSepPattern.matches(lines[i + 1])) {
                // Парсим заголовки
                val headerCells = trimmed.trim().trim('|')
                    .split("|")
                    .map { cleanSpaces(it) }
                    .map { MdBlock.MdText(it) }
                // Пропускаем строку заголовка и сепаратора
                i += 2

                // Парсим строки данных
                val rows = mutableListOf<List<MdBlock.MdText>>()
                while (i < lines.size && lines[i].contains("|")) {
                    val rowTrim = cleanSpaces(lines[i].trim())
                    if (rowTrim.isEmpty()) break
                    val cells = rowTrim.trim().trim('|')
                        .split("|")
                        .map { cleanSpaces(it) }
                        .map { MdBlock.MdText(it) }
                    // Добавляем только если число ячеек совпадает с заголовками
                    if (cells.size == headerCells.size) {
                        rows.add(cells)
                    }
                    i++
                }

                // Собираем в Map<заголовок, список ячеек>
                val tableMap = headerCells.mapIndexed { idx, header ->
                    header to rows.map { it[idx] }
                }.toMap()
                blocks.add(MdBlock.MdTable(tableMap))
                continue
            }

            // Обычный текст
            blocks.add(parseText(trimmed))
            i++
        }

        return blocks
    }


    fun parseText(text1: String): MdBlock.MdText {
        val symbols = linkedMapOf<String, MutableList<Int>>()
        val markers = listOf("~~~", "***", "~~", "**", "*", "~")
        val text = cleanSpaces(text1)
        var i = 0
        while (i < text.length) {
            var matched = false
            for (marker in markers) {
                if (text.startsWith(marker, i)) {
                    symbols.getOrPut(marker) { mutableListOf() }.add(i)
                    i += marker.length
                    matched = true
                    break
                }
            }
            if (!matched) {
                i++
            }
        }
        val symbolMap: Map<String, List<Int>> = symbols.mapValues { it.value.sorted() }
        return formatText(text, symbolMap)
    }

    fun formatText(
        text: String,
        symbols: Map<String, List<Int>>,
    ): MdBlock.MdText {
        val italicRanges = mutableListOf<IntArray>()
        val boldRanges = mutableListOf<IntArray>()
        val strikeRanges = mutableListOf<IntArray>()

        for ((sym, positions) in symbols) {
            if (positions.size % 2 != 0) continue
            for (j in positions.indices step 2) {
                val open = positions[j]
                val close = positions[j + 1]
                when (sym) {
                    "***", "~~~" -> {
                        italicRanges += intArrayOf(open + sym.length, close)
                        boldRanges += intArrayOf(open + sym.length, close)
                    }

                    "**" -> {
                        boldRanges += intArrayOf(open + sym.length, close)
                    }

                    "*" -> {
                        italicRanges += intArrayOf(open + sym.length, close)
                    }

                    "~~" -> {
                        strikeRanges += intArrayOf(open + sym.length, close)
                    }

                    "~" -> {
                        strikeRanges += intArrayOf(open + sym.length, close)
                    }
                }
            }
        }

        val toRemove = BooleanArray(text.length)
        for ((sym, positions) in symbols) {
            if (positions.size % 2 != 0) continue
            for (j in positions.indices step 2) {
                val open = positions[j]
                val close = positions[j + 1]
                for (k in open until open + sym.length) toRemove[k] = true
                for (k in close until close + sym.length) toRemove[k] = true
            }
        }

        val sb = StringBuilder()
        val origToNew = IntArray(text.length)
        var newIdx = 0
        for (idx in text.indices) {
            origToNew[idx] = newIdx
            if (!toRemove[idx]) {
                sb.append(text[idx])
                newIdx++
            }
        }

        fun toNew(orig: Int, end: Int): IntArray {
            return intArrayOf(origToNew[orig], origToNew[end])
        }

        val italicFinal = italicRanges.map { toNew(it[0], it[1]) }
        val boldFinal = boldRanges.map { toNew(it[0], it[1]) }
        val strikeFinal = strikeRanges.map { toNew(it[0], it[1]) }

        return MdBlock.MdText(
            text = sb.toString(),
            italicIndexes = italicFinal,
            boldIndexes = boldFinal,
            crossedOutIndexes = strikeFinal,
            header = null
        )
    }


    fun parseHeader(line: String): MdBlock.MdText {
        for (level in 0..5) {
            if (level >= line.length) return MdBlock.MdText("", header = Header.from(level + 1))
            if (line[level] != '#') {
                return parseText(line.substring(level).trimStart()).also {
                    it.header = Header.from(level)
                }
            }
        }
        return parseText(line.substring(6).trimStart()).also {
            it.header = Header.from(6)
        }
    }

    fun cleanSpaces(text: String): String {
        return text
            .replace(Regex(" {2,}"), " ")
            .trim()
    }

    fun normalizeNewlines(text: String): String {
        return text
            .replace(Regex("(?<!\n)\n(?!\n)"), " ")
            .replace(Regex("\n{2,}"), "\n")
    }
}





