package cz.chali.advent.year2015.day1

import java.io.InputStream

import scala.io.Source

object FindBasement extends App {
    def inputFile: InputStream = getClass.getResourceAsStream("floorInput")
    def fileContent: String = Source.fromInputStream(inputFile).mkString
    def floorChanges: IndexedSeq[Int] = fileContent.map(char => if (char == '(') 1 else -1)

    def firstBasementHit: Int = floorChanges.scanLeft(0)((a: Int, b: Int) => a + b).indexOf(-1)

    println(s"Basement in steps: $firstBasementHit")
}
