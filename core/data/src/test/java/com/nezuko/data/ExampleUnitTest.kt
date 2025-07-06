package com.nezuko.data

import com.nezuko.data.md.MdParserImpl
import com.nezuko.domain.md.Header
import com.nezuko.domain.md.MdBlock
import org.junit.Assert.assertEquals
import org.junit.Test
import org.slf4j.LoggerFactory

class MdParserTest {
    val log = LoggerFactory.getLogger(javaClass)
    val mdParser = MdParserImpl()

    @Test
    fun headerTest() {
        assertEquals(mdParser.parseHeader("# 1"), MdBlock.MdText("1", header = Header.FIRST))
        assertEquals(mdParser.parseHeader("###### 1"), MdBlock.MdText("1", header = Header.SIXTH))
        assertEquals(mdParser.parseHeader("####### 1"), MdBlock.MdText("# 1", header = Header.SIXTH))
    }



    @Test
    fun spaceTest() {
        val a = mdParser.cleanSpaces("   aaaa       a   a a a    a   ")
        assertEquals("aaaa a a a a a", a)
    }

    @Test
    fun normalizeTest() {
        val a = mdParser.normalizeNewlines("\n\n\nzxc\nasd\n\n")
        assertEquals("\nzxc asd\n", a)
    }

    @Test
    fun parseTestText() {

    }

    @Test
    fun formatTest__bold() {
        var a = hashMapOf(
            Pair("**", listOf(0, 5)),
        )
        var res = mdParser.formatText("**asd**", a)
        var expected = MdBlock.MdText(
            text = "asd",
            italicIndexes = arrayListOf(),
            boldIndexes = arrayListOf(intArrayOf(0, 3)),
            crossedOutIndexes = arrayListOf(),
            header = null
        )
        assertEquals(expected, res)
    }

    @Test
    fun formatTestItalics() {
        var a = hashMapOf(
            Pair("*", listOf(0, 4)),
        )
        var res = mdParser.formatText("*asd*", a)
        var expected = MdBlock.MdText(
            text = "asd",
            italicIndexes = arrayListOf(intArrayOf(0, 3)),
            boldIndexes = arrayListOf(),
            crossedOutIndexes = arrayListOf(),
            header = null
        )
        assertEquals(expected, res)
    }

    @Test
    fun formatTestItalicsAndBold() {
        var a = hashMapOf(
            Pair("***", listOf(0, 6)),
        )
        var res = mdParser.formatText("***asd***", a)
        var expected = MdBlock.MdText(
            text = "asd",
            italicIndexes = arrayListOf(intArrayOf(0, 3)),
            boldIndexes = arrayListOf(intArrayOf(0, 3)),
            crossedOutIndexes = arrayListOf(),
            header = null
        )
        assertEquals(expected, res)
    }

    @Test
    fun formatTestCrossed() {
        var a = hashMapOf(
            Pair("~~", listOf(0, 5)),
        )
        var res = mdParser.formatText("~~asd~~", a)
        var expected = MdBlock.MdText(
            text = "asd",
            italicIndexes = arrayListOf(),
            boldIndexes = arrayListOf(),
            crossedOutIndexes = arrayListOf(intArrayOf(0, 3)),
            header = null
        )
        assertEquals(expected, res)
    }

    @Test
    fun formatTestCrossed__withOne() {
        var a = hashMapOf(
            Pair("~", listOf(0, 4)),
        )
        var res = mdParser.formatText("~asd~", a)
        var expected = MdBlock.MdText(
            text = "asd",
            italicIndexes = arrayListOf(),
            boldIndexes = arrayListOf(),
            crossedOutIndexes = arrayListOf(intArrayOf(0, 3)),
            header = null
        )
        assertEquals(expected, res)
    }


    @Test
    fun formatTestCrossedItalicsBold() {
        var a = hashMapOf(
            Pair("~~", listOf(0, 11)),
            Pair("***", listOf(2, 8)),
        )
        var res = mdParser.formatText("~~***asd***~~", a)
        var expected = MdBlock.MdText(
            text = "asd",
            italicIndexes = arrayListOf(intArrayOf(0, 3)),
            boldIndexes = arrayListOf(intArrayOf(0, 3)),
            crossedOutIndexes = arrayListOf(intArrayOf(0, 3)),
            header = null
        )
        assertEquals(expected, res)
    }


    @Test
    fun formatTestCrossedItalicsBoldWithSpecSymb() {
        var a = hashMapOf(
            Pair("***", listOf(3, 9)),
            Pair("~~", listOf(0, 12)),
        )


        var res = mdParser.formatText("~~~***asd***~~", a)

        var expected = MdBlock.MdText(
            text = "~asd",
            italicIndexes = arrayListOf(intArrayOf(1, 4)),
            boldIndexes = arrayListOf(intArrayOf(1, 4)),
            crossedOutIndexes = arrayListOf(intArrayOf(0, 4)),
            header = null
        )
        assertEquals(expected, res)
    }

    @Test
    fun parseTextTest_ItalicsBold() {
        val a = "***asd***"
        val res = mdParser.parseText(a)
        val expected = MdBlock.MdText(
            "asd",
            italicIndexes = arrayListOf(intArrayOf(0, 3)),
            boldIndexes = arrayListOf(intArrayOf(0, 3))
        )

        assertEquals(expected, res)
    }

    @Test
    fun parseTextTest_ItalicsBoldWithOtherWords() {
        val a = "asd***asd***asd"
        val res = mdParser.parseText(a)
        val expected = MdBlock.MdText(
            "asdasdasd",
            italicIndexes = arrayListOf(intArrayOf(3, 6)),
            boldIndexes = arrayListOf(intArrayOf(3, 6))
        )

        assertEquals(expected, res)
    }

    @Test
    fun parseTextTest_ItalicsBoldWithOtherWordsWithItalics() {
        val a = "*asd****asd***asd"
        val res = mdParser.parseText(a)
        val expected = MdBlock.MdText(
            "asdasdasd",
            italicIndexes = arrayListOf(intArrayOf(0, 3), intArrayOf(3, 6)),
            boldIndexes = arrayListOf(intArrayOf(3, 6))
        )

        assertEquals(expected, res)
    }

    @Test
    fun parseTextTest_StrangeeSituation() {
        val a = "*a**a*"
        val res = mdParser.parseText(a)
        val expected = MdBlock.MdText("a**a", italicIndexes = arrayListOf(intArrayOf(0, 4)))

        assertEquals(expected, res)
    }


    @Test
    fun parseMdText() {
        val a = """# pinteres

|123|321|
|--|--|
|123|123|

## auth
`./gradlew :auth:update --no-configuration-cache` - auth update
`./gradlew :auth:generateProto` 


## users
`./gradlew :users:update --no-configuration-cache` - users update
`./gradlew :users:generateProto` 


## posts
`./gradlew :posts:update --no-configuration-cache` - posts update
`./gradlew :posts:generateProto` 

# Проект «Markdown Demo»

Добро пожаловать в демонстрационный документ на языке разметки **Markdown**. Здесь вы найдёте примеры основных элементов, расширенные списки, таблицы, блоки кода, цитаты и многое другое.

---

## Оглавление

1. [Введение](#введение)  
2. [Форматирование текста](#форматирование-текста)  
3. [Списки](#списки)  
4. [Кодовые блоки](#кодовые-блоки)  
5. [Цитаты и примечания](#цитаты-и-примечания)  
6. [Ссылки и изображения](#ссылки-и-изображения)  
7. [Таблицы](#таблицы)  
8. [Дополнительные возможности](#дополнительные-возможности)  
9. [Заключение](#заключение)  

---

## Введение

Markdown — лёгкий и человеко‑читаемый язык разметки, который широко используется для документации, блогов, GitHub README и вёрстки текстов.

---

## Форматирование текста

- **Жирный текст**: `**текст**`  
- *Курсив*: `*текст*`  
- ***Жирный курсив***: `***текст***`  
- ~~Зачёркнутый текст~~: `~~текст~~`  
- `Моноширинный inline-код`: `` `код` ``

![Фото](https://avatars.mds.yandex.net/i?id=2828c9d7433500e5bd496abd893a77ab_l-5349316-images-thumbs&n=13 "Фото")"""
        mdParser.parseMd(a).forEach {
            println(it)
        }
    }
}