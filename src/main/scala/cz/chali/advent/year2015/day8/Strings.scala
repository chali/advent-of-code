package cz.chali.advent.year2015.day8

import cz.chali.advent.year2015.input.InputReader

object Strings {

    val SINGLE_LENGTH_CHAR = "h"

    def countLengthDifferences(strings: List[String], transformation: (String) => String): Int = {
        val characterLength = strings.map(_.length).sum
        val memoryLength = strings
            .map(transformation)
            .map(_.length).sum
        Math.abs(characterLength - memoryLength)
    }

    def removeEscaping(string: String): String = {
        removeFirstAndLastDoubleQuote(string).replaceAllLiterally("\\\\", "\\").replaceAllLiterally("\\\"", "\"").replaceAll("\\\\[x][a-f,0-9]{2}", SINGLE_LENGTH_CHAR)
    }

    def removeFirstAndLastDoubleQuote(string: String): String = string.substring(1, string.length - 1)

    def escape(string: String): String = {
        "\"" + string.replaceAllLiterally("\\", "\\\\").replaceAllLiterally("\"", "\\\"") + "\""
    }

    def main(args: Array[String]) {
        val fileContent: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day8/strings")
        val difference: Int = countLengthDifferences(fileContent, escape)
        println(s"Difference between character length and memory length is: $difference")
    }
}
