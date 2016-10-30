package cz.chali.advent.year2015.input

import java.io.InputStreamReader

class Reader {
    fun readFile(resourcePath: String): List<String> = InputStreamReader(javaClass.getResourceAsStream(resourcePath)).readLines()
}
