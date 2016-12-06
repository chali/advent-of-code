package cz.chali.advent.year2016.day3

import cz.chali.advent.transpose
import cz.chali.advent.year2015.input.Reader

class TrianglesVerification {

    fun numberOfCorrectTriangles(rawTriangles: List<String>): Int {
        val sides = parseTriangles(rawTriangles)
        val sortedSides = sides.map { it.sorted() }
        return sortedSides.count { it[0] + it[1] > it[2] }
    }

    fun numberOfCorrectTrianglesByColumn(rawTriangles: List<String>): Int {
        val sidesByColumn = parseTriangles(rawTriangles)
        val sidesByRow = convertToRows(sidesByColumn)
        val sortedSides = sidesByRow.map { it.sorted() }
        return sortedSides.count { it[0] + it[1] > it[2] }
    }

    private fun parseTriangles(rawTriangles: List<String>): List<List<Int>> {
        val parsingRegex = Regex(" +(\\d+) +(\\d+) +(\\d+)")
        return rawTriangles.map { row ->
            val matchResult = parsingRegex.matchEntire(row)
            val groupValues = matchResult?.groupValues?.drop(1)
            groupValues?.map(String::toInt)
                    ?: throw IllegalArgumentException("Could not parse $row")
        }
    }

    private fun convertToRows(sidesByColumn: List<List<Int>>): List<List<Int>> {
        val ranges = (1..(sidesByColumn.size / 3)).map { ((it - 1) * 3) .. (it * 3) -1 }
        val groupsOfColumnOrientedTriangles = ranges.map { sidesByColumn.slice(it) }
        val groupsOfRowOrientedTriangles = groupsOfColumnOrientedTriangles.map { it.transpose() }
        return groupsOfRowOrientedTriangles.flatten()
    }
}

fun main(args: Array<String>) {
    val triangleSides = Reader().readFile("/cz/chali/advent/year2016/day3/triangles.txt")
    val possibleTriangles = TrianglesVerification().numberOfCorrectTrianglesByColumn(triangleSides)
    println(possibleTriangles)
}