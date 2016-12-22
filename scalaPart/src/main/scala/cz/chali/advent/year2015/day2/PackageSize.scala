package cz.chali.advent.year2015.day2

import cz.chali.advent.input.InputReader

object PackageSize {

    def computeSize(fileLines: List[String]): Int = {
        fileLines.map(parseLine).map(computeOnePackageSize).sum
    }

    def parseLine(line: String): List[Int] = {
        line.split("x").map(_.toInt).toList
    }

    /*
     Surface area of the box, which is 2*l*w + 2*w*h + 2*h*l.
     The elves also need a little extra paper for each present: the area of the smallest side.
     */
    def computeOnePackageSize(packageSize: List[Int]): Int = {
        val sideAreas: List[Int] = sidePairs(packageSize).map(_.product)
        val minArea = sideAreas.min
        sideAreas.map(2 * _).sum + minArea
    }

    def sidePairs(packageSize: List[Int]) : List[List[Int]] = {
        packageSize match {
            case length :: width :: height :: _ => List(List(length, width), List(length, height), List(width, height))
            case _ => throw new IllegalArgumentException("Package doesn't have" +
                " proper size format")
        }
    }

    def main(args: Array[String]) {
        val fileContent: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day2/packageSizes")
        val totalSize = computeSize(fileContent)
        println(s"Total size is: $totalSize")
    }
}
