package cz.chali.advent.year2016.day25

import cz.chali.advent.InputParser
import cz.chali.advent.input.Reader
import java.util.*


class ExecutionContext(val processLine: Int, val registers: Map<String, Long>,
                       val code: List<Instruction>, val output: List<Long>) {
    fun loadRegister(register: String): Long = registers.getOrElse(register) {
        throw IllegalStateException("Register $register doesn't exist")
    }

    fun execute(): ExecutionContext? {
        val instruction = code[processLine]
        if (! isEnd()) {
            val result = instruction.evaluate(this)
            return result
        } else
            return null
    }

    private fun isEnd(): Boolean {
        return 0 > processLine || processLine >= code.size
    }
}

abstract class Instruction {
    abstract fun evaluate(context: ExecutionContext): ExecutionContext
}

fun modifyRegister(register: String, context: ExecutionContext, incrementLine: Int = 1, operation: (Long) -> Long): ExecutionContext {
    val oldValue = context.loadRegister(register)
    val newValue = operation(oldValue)
    val newRegisters = HashMap(context.registers)
    newRegisters[register] = newValue
    return ExecutionContext(context.processLine + incrementLine, newRegisters, context.code, context.output)
}

fun valueOrRegisterContent(valueOrRegister: String, context: ExecutionContext): Long {
    return if (isValue(valueOrRegister))
        valueOrRegister.toLong()
    else
        context.loadRegister(valueOrRegister)
}

fun isValue(valueOrRegister: String) = valueOrRegister.matches(Regex("\\d+|-\\d+"))

class Copy(val sourceRegisterOrValue: String, val targetRegister: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        val value = valueOrRegisterContent(sourceRegisterOrValue, context)
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

class JumpNotZero(val registerOrValue: String, val offset: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        val condition = valueOrRegisterContent(registerOrValue, context)
        val offsetValue = valueOrRegisterContent(offset, context)
        return if (condition != 0L) {
            ExecutionContext(context.processLine + offsetValue.toInt(), context.registers, context.code, context.output)
        } else {
            ExecutionContext(context.processLine + 1, context.registers, context.code, context.output)
        }
    }
}

class Out(val registerOrValue: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        val value = valueOrRegisterContent(registerOrValue, context)
        return ExecutionContext(context.processLine + 1, context.registers, context.code, context.output + value)
    }
}

class ThirdComputer {

    val parser = InputParser(mapOf(
            Regex("inc ([a-zA-Z])") to { result: MatchResult ->
                val (register) = result.destructured
                Increment(register)
            },
            Regex("dec ([a-zA-Z])") to { result: MatchResult ->
                val (register) = result.destructured
                Decrement(register)
            },
            Regex("cpy ([a-zA-Z]|\\d+|-\\d+) ([a-zA-Z]|\\d+|-\\d+)") to { result: MatchResult ->
                val (sourceRegisterOrValue, targetRegister) = result.destructured
                Copy(sourceRegisterOrValue, targetRegister)
            },
            Regex("jnz ([a-zA-Z]|\\d+|-\\d+) ([a-zA-Z]|-\\d+|\\d+)") to { result: MatchResult ->
                val (register, offset) = result.destructured
                JumpNotZero(register, offset)
            },
            Regex("out ([a-zA-Z]|\\d+|-\\d+)") to { result: MatchResult ->
                val (registerOrValue) = result.destructured
                Out(registerOrValue)
            }
    ))

    fun findGenerator(rawInstructions: List<String>): Long {
        return generateSequence(0L, { it + 1L}).first { signalDescriptor ->
            val registers = mapOf("a" to signalDescriptor, "b" to 0L, "c" to 0L, "d" to 0L)
            val output = processInstructions(rawInstructions, registers)
            output.size > 10 && output.subList(0, 10) == listOf(0L, 1L, 0L, 1L, 0L, 1L, 0L, 1L, 0L, 1L)
        }
    }

    fun processInstructions(rawInstructions: List<String>, registers: Map<String, Long>): List<Long> {
        val program: List<Instruction> = parser.parseAll(rawInstructions.map(String::trim))
        val context = ExecutionContext(0, registers, program, emptyList())
        val result = generateSequence(context, ExecutionContext::execute).take(100000).last()
        return result.output
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2016/day25/instructions.txt")
    val generator = ThirdComputer().findGenerator(instructions)
    println(generator)
}