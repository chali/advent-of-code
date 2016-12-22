package cz.chali.advent.year2016.day15

import cz.chali.advent.InputParser
import cz.chali.advent.input.Reader

data class Disc(val size: Int, val starting: Int)

class Discs {

    val parser = InputParser(mapOf(
            Regex("Disc #\\d+ has (\\d+) positions; at time=0, it is at position (\\d+).") to { result: MatchResult ->
                val (size, starting) = result.destructured
                Disc(size.toInt(), starting.toInt())
            }
    ))

    fun timeWhenConsoleFallsThrough(rawDiscs: List<String>): Int {
        val discs = parser.parseAll(rawDiscs)
        return generateSequence(0, { it + 1})
                .filter { dropTime ->
                    discs.mapIndexed { i, disc -> i to disc }
                            .all { (dropTime + it.first + 1 + it.second.starting) % it.second.size == 0 }
                }.first()
    }
}

fun main(args: Array<String>) {
    val discs = Reader().readFile("/cz/chali/advent/year2016/day15/discs.txt")
    val time = Discs().timeWhenConsoleFallsThrough(discs)
    println(time)
}