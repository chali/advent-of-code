package cz.chali.advent.year2015.day3

import cz.chali.advent.input.InputReader

object VisitedHouses {

    def countVisitedHouses(fileContent: String): Int = {
        def path: IndexedSeq[(Int, Int)] = fileContent.scanLeft((0, 0))( (lastPosition, move) => {
            move match {
                case '<' => (lastPosition._1 - 1, lastPosition._2)
                case '>' => (lastPosition._1 + 1, lastPosition._2)
                case 'v' => (lastPosition._1, lastPosition._2 - 1)
                case '^' => (lastPosition._1, lastPosition._2 + 1)
            }
        })
        path.toSet.size
    }

    def main(args: Array[String]) {
        val fileContent: String = InputReader.readText("/cz/chali/advent/year2015/day3/santasPath")
        val visitedHoused: Int = countVisitedHouses(fileContent)
        println(s"Number of visited houses during Santa's trip: $visitedHoused")
    }
}
