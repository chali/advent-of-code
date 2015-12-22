package cz.chali.advent.year2015.day1

import cz.chali.advent.year2015.input.InputReader

object FindFloor extends App {
    val fileContent: String = InputReader.readText("/cz/chali/advent/year2015/day1/floorInput")
    val result = findFloor(fileContent)
    println(s"End floor is: $result")

    def findFloor(fileContent: String): Int = {
        fileContent.map(char => if (char == '(') 1 else -1).sum
    }
}
