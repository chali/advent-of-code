package cz.chali.advent.input

import java.io.InputStreamReader

class Reader {
    fun readFile(resourcePath: String): List<String> = InputStreamReader(javaClass.getResourceAsStream(resourcePath)).readLines()

    fun readFileAsText(resourcePath: String): String = InputStreamReader(javaClass.getResourceAsStream(resourcePath)).readText()
}
