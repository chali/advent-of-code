package cz.chali.advent.year2015.day2

import cz.chali.advent.input.InputReader

object RibbonLength {

    def computeLength(fileLines: List[String]): Int = {
        fileLines.map(parseLine).map(computeOnePackageLength).sum
    }

    def parseLine(line: String): List[Int] = {
        line.split("x").map(_.toInt).toList
    }

    /*
     A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap
     the present plus 2*3*4 = 24 feet of ribbon for the bow, for a total of 34 feet.
     */
    def computeOnePackageLength(packageSize: List[Int]): Int = {
        val doubleSizes = packageSize.map(_ * 2)
        doubleSizes.sum - doubleSizes.max + packageSize.product
    }

    def main(args: Array[String]) {
        val fileContent: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day2/packageSizes")
        val totalLength = computeLength(fileContent)
        println(s"Total length is: $totalLength")
    }
}
