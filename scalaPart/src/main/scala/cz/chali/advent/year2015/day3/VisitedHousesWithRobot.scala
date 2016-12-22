package cz.chali.advent.year2015.day3

import cz.chali.advent.input.InputReader

object VisitedHousesWithRobot {
    def countVisitedHouses(fileContent: String): Int = {
        val movesWithIndexes = fileContent.zipWithIndex
        val santaMoves = movesWithIndexes.collect({case (move, index) if index % 2 == 0 => move})
        val robotMoves = movesWithIndexes.collect({case (move, index) if index % 2 == 1 => move})
        val santaPath = computePath(santaMoves)
        val robotPath = computePath(robotMoves)
        (santaPath ::: robotPath).toSet.size
    }

    def computePath(moves: IndexedSeq[Char]): List[(Int, Int)] = {
        moves.scanLeft((0, 0))( (lastPosition, move) => {
            move match {
                case '<' => (lastPosition._1 - 1, lastPosition._2)
                case '>' => (lastPosition._1 + 1, lastPosition._2)
                case 'v' => (lastPosition._1, lastPosition._2 - 1)
                case '^' => (lastPosition._1, lastPosition._2 + 1)
            }
        }).toList
    }

    def main(args: Array[String]) {
        val fileContent: String = InputReader.readText("/cz/chali/advent/year2015/day3/santasPath")
        val visitedHoused: Int = countVisitedHouses(fileContent)
        println(s"Number of visited houses during Santa's trip: $visitedHoused")
    }
}
