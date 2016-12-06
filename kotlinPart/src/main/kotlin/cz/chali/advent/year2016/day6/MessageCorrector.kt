package cz.chali.advent.year2016.day6

import cz.chali.advent.transpose
import cz.chali.advent.year2015.input.Reader


class MessageCorrector {
    fun correct(rawMessages: List<String>): String {
        return rawMessages.map(String::toList)
                .transpose()
                .map { it.groupBy { it }.maxBy { it.value.size }?.key
                        ?: throw IllegalStateException("Could not find max occurrences in $it")
                }
                .joinToString(separator = "")
    }

    fun correctLeastCommon(rawMessages: List<String>): String {
        return rawMessages.map(String::toList)
                .transpose()
                .map { it.groupBy { it }.minBy { it.value.size }?.key
                        ?: throw IllegalStateException("Could not find max occurrences in $it")
                }
                .joinToString(separator = "")
    }
}

fun main(args: Array<String>) {
    val messages = Reader().readFile("/cz/chali/advent/year2016/day6/messages.txt")
    val correctMessage = MessageCorrector().correctLeastCommon(messages)
    println(correctMessage)
}