package cz.chali.advent.year2015.day18

import cz.chali.advent.year2015.input.InputReader

object LightsOfLife {

    def countOnLights(rawGrid: List[String], steps: Int): Int = {
        val parsedGrid: Vector[Boolean] = parseGrid(rawGrid)
        val dimensionSize = rawGrid.size
        val brokenGrid = updateToBrokenGrid(parsedGrid, dimensionSize)
        val finalGrid: Vector[Boolean] = animateGrid(brokenGrid, dimensionSize, steps)
        countLights(finalGrid)
    }

    def parseGrid(rawGrid: List[String]): Vector[Boolean] = {
        rawGrid.flatMap(gridLine => {
            gridLine.map(_ == '#')
        }).toVector
    }

    //corners of grid are always on
    def updateToBrokenGrid(originalGrid: Vector[Boolean], dimensionSize: Int): Vector[Boolean] = {
        val indexCalculator = twoDimToOneDim(dimensionSize) _
        val maxIndex = dimensionSize - 1
        originalGrid.updated(indexCalculator(0, 0), true)
            .updated(indexCalculator(0, maxIndex), true)
            .updated(indexCalculator(maxIndex, 0), true)
            .updated(indexCalculator(maxIndex, maxIndex), true)
    }

    def animateGrid(grid: Vector[Boolean], dimensionSize: Int, steps: Int): Vector[Boolean] = (1 to steps).foldLeft(grid)(animationStep(dimensionSize))

    def animationStep(dimensionSize: Int)(previousGrid: Vector[Boolean], currentStep: Int): Vector[Boolean] = {
        val nextGrid = (0 until dimensionSize).flatMap( x => {
            (0 until dimensionSize).map( y => {
                val neighborsInOneDimension: List[Int] = neighborCoordinates(dimensionSize, x, y)
                    .map(twoDimToOneDim(dimensionSize))
                val neighborsStatus: List[Boolean] = neighborsInOneDimension.collect(previousGrid)
                val neighborhood: Int = countLights(neighborsStatus)
                val currentStatus: Boolean = previousGrid(twoDimToOneDim(dimensionSize)((x, y)))
                newStatus(currentStatus, neighborhood)
            })
        }).toVector
        updateToBrokenGrid(nextGrid, dimensionSize)
    }

    def neighborCoordinates(dimensionSize: Int, x: Int, y: Int): List[(Int, Int)] = List(
        (x + 1, y + 1), (x + 1, y), (x + 1, y -1),
        (x, y + 1), (x, y -1),
        (x - 1, y + 1), (x - 1, y), (x - 1, y - 1)
    ).filter( point =>
        point._1 >= 0 && point._1 < dimensionSize && point._2 >= 0 && point._2 < dimensionSize
    )

    def twoDimToOneDim(dimensionSize: Int)(coordinates: (Int, Int)): Int = coordinates._1 * dimensionSize + coordinates._2

    def newStatus(currentStatus: Boolean, neighborhood: Int): Boolean = {
        if (! currentStatus && neighborhood == 3)
            true
        else if (currentStatus && (neighborhood == 2 || neighborhood == 3))
            true
        else
            false
    }

    def countLights(grid: Seq[Boolean]): Int = grid.count(light => light)

    def main(args: Array[String]) {
        val grid: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day18/initialGrid")
        val count: Int = countOnLights(grid, 100)
        println(s"Number of lights on in grid: $count")
    }
}
