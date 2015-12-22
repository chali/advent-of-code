package cz.chali.advent.year2015.day2

import cz.chali.advent.year2015.input.InputReader

object RibbonLength {
    val fileContent: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day2/packageSizes")
    val totalLength = computeLength(fileContent)
    println(s"Total length is: $totalLength")

    def computeLength(fileLines: List[String]): Int = {
        fileLines.map(parseLine).map(computeOnePackageLength).sum
    }

    def parseLine(line: String): List[Int] = {
        line.split("x").map(_.toInt).toList
    }

    def computeOnePackageLength(packageSize: List[Int]): Int = {
        val sideAreas: List[Int] = sidePairs(packageSize).map(_.product)
        val minArea = sideAreas.min
        sideAreas.map(2 * _).sum + minArea
    }

    def sidePairs(packageSize: List[Int]) : List[List[Int]] = {
        packageSize match {
            case length :: width :: height :: _ => List(List(length, width), List(length, height), List(width, height))
        }
    }
}
