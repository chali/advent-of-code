package cz.chali.advent.year2015.day1

import cz.chali.advent.input.InputReader

object FindBasement {

    def findFirstBasementHit(fileContent: String): Int = {
        def floorChanges: IndexedSeq[Int] = fileContent.map(char => if (char == '(') 1 else -1)
        floorChanges.scanLeft(0)((a: Int, b: Int) => a + b).indexOf(-1)
    }

    def main(args: Array[String]) {
        val fileContent: String = InputReader.readText("/cz/chali/advent/year2015/day1/floorInput")
        val firstBasementHit: Int = findFirstBasementHit(fileContent)
        println(s"Basement in steps: $firstBasementHit")
    }
}
