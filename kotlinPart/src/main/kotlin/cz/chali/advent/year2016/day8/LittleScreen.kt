package cz.chali.advent.year2016.day8

import cz.chali.advent.InputParser
import cz.chali.advent.transpose
import cz.chali.advent.input.Reader

data class Display(val lights: List<List<Boolean>>) {
    companion object Factory {
        fun create(width: Int, height: Int): Display {
            val lights = (1..height).map { y ->
                (1..width).map { x -> false }
            }
            return Display(lights)
        }
    }

    fun print() {
        lights.forEach { list ->
            println(list.map { if (it) '#' else '.' }.joinToString(separator = " "))
        }
        println()
    }
}

abstract class Instruction {
    abstract fun evaluate(display: Display): Display
}

class TurnOnRectangle(val x: Int, val y: Int) : Instruction() {
    override fun evaluate(display: Display): Display {
        val xRange = (0 .. x - 1)
        val yRange = (0 .. y - 1)
        val newLights = display.lights.mapIndexed { y, row ->
            if (yRange.contains(y)) {
                row.mapIndexed { x, light -> if (xRange.contains(x)) true else light }
            } else {
                row
            }
        }
        return Display(newLights)
    }
}

class RotateRow(val y: Int, val n: Int) : Instruction() {
    override fun evaluate(display: Display): Display {
        return Display(display.lights.mapIndexed { index, list ->
            if (index == y)
                rotateRight(list, n)
            else
                list
        })
    }
}

class RotateColumn(val x: Int, val n: Int) : Instruction() {
    override fun evaluate(display: Display): Display {
        return Display(display.lights.transpose().mapIndexed { index, list ->
            if (index == x)
                rotateRight(list, n)
            else
                list
        }.transpose())
    }
}

fun <T> rotateRight(list: List<T>, n: Int): List<T> {
    return list.takeLast(n) + list.take(list.size - n)
}


class LittleScreen(val width: Int, val height: Int) {

    val parser = InputParser(mapOf(
            Regex("rect (\\d+)x(\\d+)") to { result: MatchResult ->
                val (x, y) = result.destructured
                TurnOnRectangle(x.toInt(), y.toInt())
            },
            Regex("rotate row y=(\\d+) by (\\d+)") to { result: MatchResult ->
                val (y, n) = result.destructured
                RotateRow(y.toInt(), n.toInt())
            },
            Regex("rotate column x=(\\d+) by (\\d+)") to { result: MatchResult ->
                val (x, n) = result.destructured
                RotateColumn(x.toInt(), n.toInt())
            }
    ))

    fun countEnabledLights(rawInstructions: List<String>): Int {
        val instructions = parser.parseAll(rawInstructions)
        val finalState = instructions.fold(Display.create(width, height), { display, instruction ->
            val newDisplay = instruction.evaluate(display)
            newDisplay.print()
            newDisplay
        })
        return finalState.lights.sumBy { it.count { it } }
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2016/day8/instructions.txt")
    val count = LittleScreen(width = 50, height = 6).countEnabledLights(instructions)
    println(count)
}