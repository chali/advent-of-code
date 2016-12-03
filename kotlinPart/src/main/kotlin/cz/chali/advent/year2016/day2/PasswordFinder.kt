package cz.chali.advent.year2016.day2

import cz.chali.advent.scan
import cz.chali.advent.year2015.input.Reader

data class Coordinate(val x: Int, val y: Int)

enum class Direction(val move: (KeyPad, Coordinate) -> Coordinate) {
    U({ keypad, start ->
        if (0 < start.y && keypad.keys[start.y - 1][start.x] != "")
            Coordinate(start.x, start.y - 1)
        else
            start
    }),
    D({ keypad, start ->
        if (start.y < keypad.keys.size - 1 && keypad.keys[start.y + 1][start.x] != "")
            Coordinate(start.x, start.y + 1)
        else
            start
    }),
    L({ keypad, start ->
        if (0 < start.x && keypad.keys[start.y][start.x - 1] != "")
            Coordinate(start.x - 1, start.y)
        else
            start
    }),
    R({ keypad, start ->
        if (start.x < keypad.keys.size -1 && keypad.keys[start.y][start.x + 1] != "")
            Coordinate(start.x + 1, start.y)
        else
            start
    })
}

interface KeyPad {
    val keys: Array<Array<String>>
    val startingKey: Coordinate

    fun getPassword(coordinates: List<Coordinate>) = coordinates.subList(1, coordinates.size)
            .map({ keys[it.y][it.x] })
            .joinToString(separator = "")
}

class EntranceKeyPad : KeyPad {
    override val keys = arrayOf(
            arrayOf("1", "2", "3"),
            arrayOf("4", "5", "6"),
            arrayOf("7", "8", "9")
    )

    override val startingKey = Coordinate(1,1)
}

class BathroomKeyPad : KeyPad {
    override val keys = arrayOf(
            arrayOf("",  "",  "1",  "", ""),
            arrayOf("",  "2", "3", "4", ""),
            arrayOf("5", "6", "7", "8", "9"),
            arrayOf("",  "A", "B", "C", ""),
            arrayOf("",  "",  "D", "",  "")
    )

    override val startingKey = Coordinate(0,2)
}

class PasswordFinder {

    fun find(rawAllDirections: List<String>, keyPad: KeyPad): String {
        val allDirections = parse(rawAllDirections)
        return findPassword(allDirections, keyPad)
    }

    fun findPassword(allDirections: List<List<Direction>>, keyPad: KeyPad): String {
        val coordinates = allDirections.scan(keyPad.startingKey, { directions, previous ->
            directions.fold(previous, { start, direction -> direction.move(keyPad, start)})
        })
        return keyPad.getPassword(coordinates)
    }

    private fun parse(rawAllDirections: List<String>): List<List<Direction>> {
        return rawAllDirections.map { rawDirections ->
            rawDirections.map { char ->
                Direction.valueOf(char.toString())
            }
        }
    }
}

fun main(args: Array<String>) {
    val steps = Reader().readFile("/cz/chali/advent/year2016/day2/instructions.txt")
    val password = PasswordFinder().find(steps, BathroomKeyPad())
    println(password)
}