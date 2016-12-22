package cz.chali.advent.year2016.day12

import cz.chali.advent.InputParser
import cz.chali.advent.input.Reader
import java.util.*

class ExecutionContext(val processLine: Int, val registers: Map<String, Long>) {
    fun loadRegister(register: String): Long = registers.getOrElse(register) {
        throw IllegalStateException("Register $register doesn't exist")
    }
}

abstract class Instruction {
    abstract fun evaluate(context: ExecutionContext): ExecutionContext
}

fun modifyRegister(register: String, context: ExecutionContext, operation: (Long) -> Long): ExecutionContext {
    val oldValue = context.loadRegister(register)
    val newValue = operation(oldValue)
    val newRegisters = HashMap(context.registers)
    newRegisters[register] = newValue
    return ExecutionContext(context.processLine + 1, newRegisters)
}

class Copy(val sourceRegisterOrValue: String, val targetRegister: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        val value = if (sourceRegisterOrValue.matches(Regex("\\d+")))
            sourceRegisterOrValue.toLong()
        else
            context.loadRegister(sourceRegisterOrValue)
        return modifyRegister(targetRegister, context) { oldValue ->  value}
    }
}

class Increment(val register: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return modifyRegister(register, context) { oldValue -> oldValue + 1}
    }
}

class Decrement(val register: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return modifyRegister(register, context) { oldValue -> oldValue - 1}
    }
}

class JumpNotZero(val registerOrValue: String, val offset: Int) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        val condition = if (registerOrValue.matches(Regex("\\d+")))
            registerOrValue.toLong()
        else
            context.loadRegister(registerOrValue)

        return if (condition != 0L) {
            ExecutionContext(context.processLine + offset, context.registers)
        } else {
            ExecutionContext(context.processLine + 1, context.registers)
        }
    }
}

class Computer {

    val parser = InputParser(mapOf(
            Regex("inc ([a-zA-Z])") to { result: MatchResult ->
                val (register) = result.destructured
                Increment(register)
            },
            Regex("dec ([a-zA-Z])") to { result: MatchResult ->
                val (register) = result.destructured
                Decrement(register)
            },
            Regex("cpy ([a-zA-Z]|\\d+) ([a-zA-Z])") to { result: MatchResult ->
                val (sourceRegisterOrValue, targetRegister) = result.destructured
                Copy(sourceRegisterOrValue, targetRegister)
            },
            Regex("jnz ([a-zA-Z]|\\d+) (-\\d+|\\d+)") to { result: MatchResult ->
                val (register, offset) = result.destructured
                JumpNotZero(register, offset.toInt())
            }
    ))

    fun processInstructions(rawInstructions: List<String>, registers: Map<String, Long>): Map<String, Long> {
        val program: List<Instruction> = parser.parseAll(rawInstructions.map(String::trim))
        var context = ExecutionContext(0, registers)
        while (0 <= context.processLine && context.processLine < program.size) {
            val instruction = program[context.processLine]
            context = instruction.evaluate(context)
        }
        return context.registers
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2016/day12/instructions.txt")
    val finalRegisters = Computer().processInstructions(instructions, mapOf("a" to 0L, "b" to 0L, "c" to 1L, "d" to 0L))
    println(finalRegisters["a"])
}
