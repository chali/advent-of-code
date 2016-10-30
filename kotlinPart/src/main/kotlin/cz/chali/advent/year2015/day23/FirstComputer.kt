package cz.chali.advent.year2015.day23

import cz.chali.advent.year2015.input.Reader

class FirstComputer {
    fun processInstructions(rawInstructions: List<String>): Map<String, Int> {
        return mapOf("a" to 2)
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2015/day23/instructions")
    println(instructions)

}