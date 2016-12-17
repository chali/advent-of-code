package cz.chali.advent.year2016.day17

import java.security.MessageDigest
import java.util.*
import javax.xml.bind.DatatypeConverter

data class Location(val x: Int, val y: Int)
data class Exploration(val location: Location, val path: String)

class Labyrinth {

    val labyrinthSize = 4
    val directionInHashPosition = mapOf('U' to 0, 'D' to 1, 'L' to 2, 'R' to 3)
    val directionLimitation = mapOf(
            'U' to { loc: Location -> loc.y > 0 },
            'D' to { loc: Location -> loc.y < labyrinthSize - 1},
            'L' to { loc: Location -> loc.x > 0 },
            'R' to { loc: Location -> loc.x < labyrinthSize - 1}
    )
    val directionMoves = mapOf(
            'U' to { loc: Location -> Location(loc.x, loc.y - 1) },
            'D' to { loc: Location -> Location(loc.x, loc.y + 1) },
            'L' to { loc: Location -> Location(loc.x - 1, loc.y) },
            'R' to { loc: Location -> Location(loc.x + 1, loc.y) }
    )

    fun findPath(initialPassword: String): String {
        val queue = PriorityQueue<Exploration>({ a, b -> a.path.length.compareTo(b.path.length)})
        var temp = Exploration(Location(0, 0), "")
        while (temp.location != Location(labyrinthSize - 1, labyrinthSize - 1)) {
            val nextStates = nextStates(initialPassword, temp)
            queue.addAll(nextStates)
            temp = queue.poll()
        }
        return temp.path
    }

    fun  findLongestPathLength(initialPassword: String): Int {
        val queue = PriorityQueue<Exploration>({ a, b -> a.path.length.compareTo(b.path.length)})
        queue.add(Exploration(Location(0, 0), ""))
        var longest: Exploration? = null
        while (queue.isNotEmpty()) {
            val temp = queue.poll()
            if (temp.location == Location(labyrinthSize - 1, labyrinthSize - 1)) {
                longest = temp
            } else {
                val nextStates = nextStates(initialPassword, temp)
                queue.addAll(nextStates)
            }
        }
        return longest?.path?.length ?: throw IllegalStateException("Longest path not found")
    }

    private fun nextStates(initialPassword: String, current: Exploration): List<Exploration> {
        return possibleDirections(current.location, initialPassword + current.path)
                .map {
                    val move = directionMoves[it] ?: throw IllegalArgumentException("Unknown direction $it")
                    Exploration(move(current.location), current.path + it)
                }
    }

    private fun possibleDirections(currentLocation: Location, password: String): List<Char> {
        return openDirections(password).filter {
            val limitation = directionLimitation[it] ?: throw IllegalArgumentException("Unknown direction $it")
            limitation(currentLocation)
        }
    }

    private fun openDirections(password: String): List<Char> {
        val hash = hash(password)
        return directionInHashPosition.filter { isOpen(hash[it.value]) }.map { it.key }
    }

    private fun isOpen(char: Char): Boolean = 'b' <= char && char <= 'f'

    private fun hash(input: String): String {
        return DatatypeConverter.printHexBinary(
                MessageDigest.getInstance("MD5").digest(input.toByteArray())
        ).toLowerCase()
    }


}

fun main(args: Array<String>) {
    val length = Labyrinth().findLongestPathLength("udskfozm")
    println(length)
}