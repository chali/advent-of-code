package cz.chali.advent.year2016.day4

import cz.chali.advent.input.Reader
import java.util.*

data class Room(val name: String, val code: Int, val checkSum: String) {

    fun isValid(): Boolean {
        val nameWithoutDash = name.filter { it != '-' }
        val charOccurrences = nameWithoutDash.groupBy { it }.map { it.key to it.value.size }
        val orderedMostOccurredChars = charOccurrences.sortedWith(NameComparator)
                .take(checkSum.length)
                .map { it.first }
                .joinToString(separator = "")
        return orderedMostOccurredChars == checkSum
    }

    fun decryptName(): String {
        return name.map { char ->
            if (char == '-') {
                ' '
            }
            else {
                val currentIndex = Alphabet.chars.indexOf(char)
                val newIndex = (currentIndex + (code)) % Alphabet.chars.size
                Alphabet.chars[newIndex]
            }
        }.joinToString(separator = "")
    }
}

object Alphabet {
    val chars = listOf<Char>('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
}

object NameComparator : Comparator<Pair<Char, Int>> {

    override fun compare(o1: Pair<Char, Int>?, o2: Pair<Char, Int>?): Int {
        val left = o1 ?: throw IllegalArgumentException("We don't expect null pair o1")
        val right = o2 ?: throw IllegalArgumentException("We don't expect null pair o2")
        val sizeComparison = right.second.compareTo(left.second)
        if (sizeComparison != 0) {
            return sizeComparison
        } else {
            return left.first.toChar().compareTo(right.first.toChar())
        }
    }

}

class RoomNames {
    fun sumValidRoomIds(rawRoomNames: List<String>): Int {
        val rooms = parseRooms(rawRoomNames)
        return rooms.filter(Room::isValid).sumBy(Room::code)
    }

    fun findCodeOfRoom(rawRoomNames: List<String>, decryptedRoomName: String): Int {
        val rooms = parseRooms(rawRoomNames)
        val validRooms = rooms.filter(Room::isValid)
        return validRooms.first { it.decryptName() == decryptedRoomName }.code
    }

    private fun parseRooms(rawRoomNames: List<String>): List<Room> {
        val parsingRegex = Regex("(.+)-(\\d+)\\[(.+)\\]")
        return rawRoomNames.map { rawRoomName ->
            val (name, code, checkSum) = parsingRegex.matchEntire(rawRoomName)?.destructured
                    ?: throw IllegalArgumentException("Could not parse $rawRoomName")
            Room(name, code.toInt(), checkSum)
        }
    }
}

fun main(args: Array<String>) {
    val rooms = Reader().readFile("/cz/chali/advent/year2016/day4/rooms.txt")
    val sum = RoomNames().sumValidRoomIds(rooms)
    val code = RoomNames().findCodeOfRoom(rooms, "northpole object storage")
    println(sum)
    println(code)
}