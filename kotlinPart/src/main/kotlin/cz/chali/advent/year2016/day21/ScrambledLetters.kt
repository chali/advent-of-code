package cz.chali.advent.year2016.day21

import cz.chali.advent.InputParser
import cz.chali.advent.input.Reader

abstract class Instruction {
    abstract fun scramble(word: String): String
    abstract fun unscramble(word: String): String
}

class SwapPosition(val x: Int, val y: Int) : Instruction() {

    override fun scramble(word: String): String {
        val list = word.toMutableList()
        val oldValue = list[x]
        list[x] = list[y]
        list[y] = oldValue
        return list.joinToString(separator = "")
    }

    override fun unscramble(word: String): String {
        return SwapPosition(y, x).scramble(word)
    }
}

class SwapLetter(val x: String, val y: String) : Instruction() {

    override fun scramble(word: String): String {
        return SwapPosition(word.indexOf(x), word.indexOf(y)).scramble(word)
    }

    override fun unscramble(word: String): String {
        return SwapLetter(y, x).scramble(word)
    }
}

class Rotate(val steps: Int, val direction: Direction) : Instruction() {

    override fun scramble(word: String): String {
        if (direction == Direction.LEFT) {
            return rotateRight(word.reversed()).reversed()
        } else {
            return rotateRight(word)
        }
    }

    override fun unscramble(word: String): String {
        return Rotate(steps, if (direction == Direction.LEFT) Direction.RIGHT else Direction.LEFT).scramble(word)
    }

    private fun rotateRight(word: String): String {
        val times = steps % word.length
        return word.takeLast(times) + word.take(word.length - times)
    }

    enum class Direction {
        LEFT, RIGHT
    }
}

class RotateBasedOnLetter(val letter: String) : Instruction() {

    override fun scramble(word: String): String {
        val index = word.indexOf(letter)
        val steps = 1 + index + if (index >= 4) 1 else 0
        return Rotate(steps, Rotate.Direction.RIGHT).scramble(word)
    }

    override fun unscramble(word: String): String {
        val index = word.indexOf(letter)
        val steps = if (index % 2 == 1 || index == 0) {
            (index / 2) + 1
        } else {
            (word.length + index) / 2 + 1
        }
        return Rotate(steps, Rotate.Direction.LEFT).scramble(word)
    }
}

class Reverse(val start: Int, val end: Int) : Instruction() {

    override fun scramble(word: String): String {
        val forReverse = word.substring(start, end + 1)
        return word.replace(forReverse, forReverse.reversed())
    }

    override fun unscramble(word: String): String {
        return scramble(word)
    }
}

class Move(val source: Int, val target: Int) : Instruction() {

    override fun scramble(word: String): String {
        val charForMove = word[source]
        val targetChar = word[target]
        val wordWithoutSource = word.replace(charForMove.toString(), "")
        if (source < target)
            return wordWithoutSource.replace(targetChar.toString(), "$targetChar$charForMove")
        else if (target < source)
            return wordWithoutSource.replace(targetChar.toString(), "$charForMove$targetChar")
        else
            return word
    }

    override fun unscramble(word: String): String {
        return Move(target, source).scramble(word)
    }
}


class ScrambledLetters {

    val parser = InputParser(mapOf(
            Regex("swap position (\\d+) with position (\\d+)") to { result: MatchResult ->
                val (x, y) = result.destructured
                SwapPosition(x.toInt(), y.toInt())
            },
            Regex("swap letter ([a-z]) with letter ([a-z])") to { result: MatchResult ->
                val (x, y) = result.destructured
                SwapLetter(x, y)
            },
            Regex("rotate (left|right) (\\d+) steps?") to { result: MatchResult ->
                val (direction, steps) = result.destructured
                Rotate(steps.toInt(), Rotate.Direction.valueOf(direction.toUpperCase()))
            },
            Regex("rotate based on position of letter ([a-z])") to { result: MatchResult ->
                val letter = result.destructured.component1()
                RotateBasedOnLetter(letter)
            },
            Regex("reverse positions (\\d+) through (\\d+)") to { result: MatchResult ->
                val (start, end) = result.destructured
                Reverse(start.toInt(), end.toInt())
            },
            Regex("move position (\\d+) to position (\\d+)") to { result: MatchResult ->
                val (source, target) = result.destructured
                Move(source.toInt(), target.toInt())
            }
    ))

    fun scramble(initialWord: String, rawInstructions: List<String>): String {
        val instructions = parser.parseAll(rawInstructions)
        val result = instructions.fold(initialWord, { word, instruction ->
            instruction.scramble(word)
        })
        return result
    }

    fun unscramble(initialWord: String, rawInstructions: List<String>): String {
        val instructions = parser.parseAll(rawInstructions).reversed()
        val result = instructions.fold(initialWord, { word, instruction ->
            instruction.unscramble(word)
        })
        return result
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2016/day21/instructions.txt")
    val word = ScrambledLetters().scramble("abcdefgh", instructions)
    println(word)

    val password = ScrambledLetters().unscramble("fbgdceah", instructions)
    println(password)
}