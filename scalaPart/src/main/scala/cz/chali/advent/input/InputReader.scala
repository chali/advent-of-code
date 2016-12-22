package cz.chali.advent.input

import java.io.InputStream

import scala.io.Source

object InputReader {
    def readText(resourcePath: String): String = {
        def inputFile: InputStream = getClass.getResourceAsStream(resourcePath)
        Source.fromInputStream(inputFile).mkString
    }

    def readLines(resourcePath: String): List[String] = {
        def inputFile: InputStream = getClass.getResourceAsStream(resourcePath)
        Source.fromInputStream(inputFile).getLines().toList
    }
}
