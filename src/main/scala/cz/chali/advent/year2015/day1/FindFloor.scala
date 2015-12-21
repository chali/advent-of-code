package cz.chali.advent.year2015.day1

import java.io.InputStream

import scala.io.Source

object FindFloor extends App {
    def inputFile: InputStream = getClass.getResourceAsStream("floorInput")
    def fileContent = Source.fromInputStream(inputFile).mkString
    def result = fileContent.map(char => if (char == '(') 1 else -1).sum
    println(s"Result: $result")
}
