package cz.chali.advent.year2016.day9

import cz.chali.advent.year2015.input.Reader


class TextDecompressor {

    val templateRegex = Regex("(\\((\\d+)x(\\d+)\\))")

    fun lengthOfDecompressedText(text: String, useNestedMarkers: Boolean): Long {
        return lengthOfDecompression(text.filter { it != ' ' && it != '\n' }, useNestedMarkers)
    }

    private fun lengthOfDecompression(text: String, useNestedMarkers: Boolean): Long {
        val indexOfMarker = text.indexOf('(')
        if (indexOfMarker < 0) {
            return text.length.toLong()
        } else {
            val (wholeTemplate, numberOfChars, times) = templateRegex.find(text)?.destructured
                    ?: throw IllegalArgumentException("Could not find full template in $text")
            val repeatedStringBeginning = indexOfMarker + wholeTemplate.length
            val repeatedStringEnd =  repeatedStringBeginning + numberOfChars.toInt()
            val repeatedString = text.substring(repeatedStringBeginning, repeatedStringEnd)
            val lengthOfRepeatedSections = if (useNestedMarkers)
                lengthOfDecompression(repeatedString, useNestedMarkers)
            else
                numberOfChars.toLong()
            return indexOfMarker.toLong() + times.toInt() * lengthOfRepeatedSections + lengthOfDecompression(text.drop(repeatedStringEnd), useNestedMarkers)
        }
    }
}

fun main(args: Array<String>) {
    val text = Reader().readFileAsText("/cz/chali/advent/year2016/day9/text.txt")
    val count = TextDecompressor().lengthOfDecompressedText(text, true)
    println(count)
}