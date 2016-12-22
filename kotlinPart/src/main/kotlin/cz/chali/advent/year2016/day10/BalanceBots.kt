package cz.chali.advent.year2016.day10

import cz.chali.advent.InputParser
import cz.chali.advent.input.Reader
import java.util.*

class World(val bots: Map<Int, Set<Int>>, val outputs: Map<Int, Set<Int>>, val rules: Map<Int, Balance>) {
    companion object {
        fun create(instructions: List<Instruction>): World {
            val (setValues, balanceRules) = instructions.partition { it is SetValue }
            val bots = setValues.map { it as SetValue }
                    .groupBy({ it.botId }, {it.chip})
                    .mapValues { it.value.toSet() }
            val rules = balanceRules.map { it as Balance }.associateBy { it.botId }
            return World(bots, hashMapOf(), rules)
        }
    }

    fun balance(): World {
        val forBalancing = bots.filterValues { it.size == 2 }
        val mutableBots = HashMap<Int, HashSet<Int>>(bots.mapValues { HashSet<Int>(it.value) })
        val mutableOutputs = HashMap<Int, HashSet<Int>>(outputs.mapValues { HashSet<Int>(it.value) })
        forBalancing.forEach { entry ->
            val rule = rules[entry.key] ?: throw IllegalArgumentException("missing rule for ${entry.key}")
            val low = entry.value.min() ?: throw IllegalArgumentException("could not find min in ${entry.value}")
            val high = entry.value.max() ?: throw IllegalArgumentException("could not find max in ${entry.value}")
            val lowTarget = if (rule.lowTarget == TargetType.BOT) mutableBots else mutableOutputs
            val highTarget = if (rule.highTarget == TargetType.BOT) mutableBots else mutableOutputs
            highTarget.merge(rule.highTargetId, hashSetOf(high), {old, new ->
                old.addAll(new)
                old
            })
            lowTarget.merge(rule.lowTargetId, hashSetOf(low), {old, new ->
                old.addAll(new)
                old
            })
            mutableBots[entry.key] = HashSet<Int>()

        }
        return World(mutableBots, mutableOutputs, rules)
    }
}

enum class TargetType {
    BOT, OUTPUT
}

interface Instruction

class SetValue(val botId: Int, val chip: Int) : Instruction
class Balance(val botId: Int, val lowTarget: TargetType, val lowTargetId: Int,
              val highTarget: TargetType, val highTargetId: Int) : Instruction

class BalanceBots {

    val parser = InputParser(mapOf(
            Regex("value (\\d+) goes to bot (\\d+)") to { result: MatchResult ->
                val (chip, botId) = result.destructured
                SetValue(botId.toInt(), chip.toInt())
            },
            Regex("bot (\\d+) gives low to (output|bot) (\\d+) and high to (output|bot) (\\d+)") to { result: MatchResult ->
                val (sourceBotId, lowTarget, lowTargetId, highTarget, highTargetId) = result.destructured
                Balance(sourceBotId.toInt(), TargetType.valueOf(lowTarget.toUpperCase()), lowTargetId.toInt(),
                        TargetType.valueOf(highTarget.toUpperCase()), highTargetId.toInt())
            }))

    fun findBotWithGivenChips(chips: Set<Int>, rawInstructions: List<String>): Int {
        val instructions = parser.parseAll(rawInstructions)
        val initialWorld = World.create(instructions)
        val finalState = generateSequence(initialWorld, World::balance)
                .first { it.bots.containsValue(chips) }
        return finalState.bots.toList().first { it.second.containsAll(chips) }.first
    }

    fun findProductOfGivenOutput(outputs: Set<Int>, rawInstructions: List<String>): Int {
        val instructions = parser.parseAll(rawInstructions)
        val initialWorld = World.create(instructions)
        val finalState = generateSequence(initialWorld, World::balance)
                .first { world -> outputs.all { world.outputs.containsKey(it) } }
        return finalState.outputs.filterKeys { key -> outputs.contains(key) }
                .mapValues { it.value.first() }
                .values.reduce { a, b ->  a * b}
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2016/day10/instructions.txt")
    val botId = BalanceBots().findProductOfGivenOutput(setOf(0, 1, 2), instructions)
    println(botId)
}