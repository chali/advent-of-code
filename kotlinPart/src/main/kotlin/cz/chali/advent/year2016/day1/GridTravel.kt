package cz.chali.advent.year2016.day1

import cz.chali.advent.year2015.input.Reader
import cz.chali.advent.scan

enum class Turn {
    RIGHT, LEFT
}

enum class Direction(val move: (Point, Int) -> Point) {
    NORTH({ point, distance -> Point(point.x, point.y + distance)}),
    SOUTH({ point, distance -> Point(point.x, point.y - distance)}),
    EAST({ point, distance -> Point(point.x + distance, point.y)}),
    WEST({ point, distance -> Point(point.x - distance, point.y)});

    fun nextDirection(turn: Turn): Direction {
        return when (this) {
            NORTH -> if (turn == Turn.RIGHT) EAST else WEST
            SOUTH -> if (turn == Turn.RIGHT) WEST else EAST
            EAST -> if (turn == Turn.RIGHT) SOUTH else NORTH
            WEST -> if (turn == Turn.RIGHT) NORTH else SOUTH
        }
    }
}

data class Step(val turn: Turn, val distance: Int)

data class Point(val x: Int, val y: Int)

data class Move(val direction: Direction, val distance: Int) {
    fun from(point: Point): List<Point> {
        return (1 .. distance).map { traveledDistance -> direction.move(point, traveledDistance) }
    }
}

class GridTravel {

    fun shortestDistance(rawSteps: String): Int {
        val steps = parseSteps(rawSteps)
        val moves = stepsToMoves(Move(Direction.NORTH, 0),steps)
        val path = movesToPath(Point(0, 0), moves.subList(1, moves.size))
        return computeDistance(path.first(), path.last())
    }

    fun shortestDistanceToFirstCommonPointOnPath(rawSteps: String): Int {
        val steps = parseSteps(rawSteps)
        val moves = stepsToMoves(Move(Direction.NORTH, 0),steps)
        val initialPoint = Point(0, 0)
        val path = movesToPath(initialPoint, moves.subList(1, moves.size))
        val firstCommonPoint = findFirstCommonPoint(path)
        return computeDistance(initialPoint, firstCommonPoint)
    }

    private fun findFirstCommonPoint(path: List<Point>): Point {
        for (point in path) {
            val currentPointIndex = path.indexOf(point)
            val nextSamePointIndex = path.subList(currentPointIndex + 1, path.size).indexOf(point)
            if (nextSamePointIndex >= 0)
                return point
        }
        throw IllegalArgumentException("Path doesn't have any intersection")
    }

    private fun computeDistance(a: Point, b: Point): Int {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y)
    }

    private fun movesToPath(initialPoint: Point, steps: List<Move>): List<Point> {
        return steps.scan(listOf(initialPoint), {move, previous -> move.from(previous.last()) }).flatten()
    }

    private fun stepsToMoves(startPoint: Move, steps: List<Step>): List<Move> {
        return steps.scan(startPoint, { step, previous -> Move(previous.direction.nextDirection(step.turn), step.distance) })
    }

    private fun parseSteps(rawSteps: String): List<Step> {
        val splitSteps = rawSteps.split(",")
        val parsingRegex = Regex("([R,L])(\\d+)")
        return splitSteps.map(String::trim).map { rawStep: String ->
            val matchResult = parsingRegex.matchEntire(rawStep)
            val (turn, distance) = matchResult?.destructured ?: throw IllegalArgumentException("Parsing failed")
            Step(if (turn == "R") Turn.RIGHT else Turn.LEFT, distance.toInt())
        }
    }
}

fun main(args: Array<String>) {
    val steps = Reader().readFileAsText("/cz/chali/advent/year2016/day1/steps")
    val distance = GridTravel().shortestDistanceToFirstCommonPointOnPath(steps)
    println(distance)
}