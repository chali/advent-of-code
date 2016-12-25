package cz.chali.advent.year2016.day23

import cz.chali.advent.InputParser
import cz.chali.advent.input.Reader
import java.util.*

class ExecutionContext(val processLine: Int, val registers: Map<String, Long>, val code: List<Instruction>) {
    fun loadRegister(register: String): Long = registers.getOrElse(register) {
        throw IllegalStateException("Register $register doesn't exist")
    }

    fun execute(): ExecutionContext {
        val instruction = code[processLine]
        val result = if (instruction.isValid()) {
            optimizeMultiplication() ?: instruction.evaluate(this)
        }
        else
            ExecutionContext(processLine + 1, registers, code)
        return result
    }

    fun isEnd(): Boolean {
        return 0 > processLine || processLine >= code.size
    }

    fun optimizeMultiplication(): ExecutionContext? {
        if (! code.indices.contains(processLine + 5))
            return null
        val copy = code[processLine]
        val inc = code[processLine + 1]
        val decInner = code[processLine + 2]
        val jnzInner = code[processLine + 3]
        val decOuter = code[processLine + 4]
        val jnzOuter = code[processLine + 5]
        val result = if (copy is Copy && inc is Increment && decInner is Decrement && jnzInner is JumpNotZero &&
                decOuter is Decrement && jnzOuter is JumpNotZero) {
            if (copy.targetRegister == decInner.register && copy.targetRegister == jnzInner.registerOrValue &&
                    jnzInner.offset == "-2" && decOuter.register == jnzOuter.registerOrValue &&
                    jnzOuter.offset == "-5") {
                val left = valueOrRegisterContent(copy.sourceRegisterOrValue, this)
                val right = valueOrRegisterContent(decOuter.register, this)
                val target = inc.register
                val multiplication = modifyRegister(target, this, 6, { previous -> previous + left * right})
                val clear = modifyRegister(decInner.register, multiplication, 0, { previous -> 0 })
                modifyRegister(decOuter.register, clear, 0, { previous -> 0 })
            } else {
                null
            }
        } else {
            null
        }
        return result
    }
}

abstract class Instruction {
    abstract fun evaluate(context: ExecutionContext): ExecutionContext
    abstract fun toggle(): Instruction
    abstract fun isValid(): Boolean
}

fun modifyRegister(register: String, context: ExecutionContext, incrementLine: Int = 1, operation: (Long) -> Long): ExecutionContext {
    val oldValue = context.loadRegister(register)
    val newValue = operation(oldValue)
    val newRegisters = HashMap(context.registers)
    newRegisters[register] = newValue
    return ExecutionContext(context.processLine + incrementLine, newRegisters, context.code)
}

fun valueOrRegisterContent(valueOrRegister: String, context: ExecutionContext): Long {
    return if (isValue(valueOrRegister))
        valueOrRegister.toLong()
    else
        context.loadRegister(valueOrRegister)
}

fun isValue(valueOrRegister: String) = valueOrRegister.matches(Regex("\\d+|-\\d+"))
fun isRegister(valueOrRegister: String) = valueOrRegister.matches(Regex("[a-z]"))

class Copy(val sourceRegisterOrValue: String, val targetRegister: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        val value = valueOrRegisterContent(sourceRegisterOrValue, context)
        return modifyRegister(targetRegister, context) { oldValue ->  value}
    }

    override fun toggle(): Instruction {
        return JumpNotZero(sourceRegisterOrValue, targetRegister)
    }

    override fun isValid(): Boolean {
        return isRegister(targetRegister)
    }
}

class Increment(val register: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return modifyRegister(register, context) { oldValue -> oldValue + 1}
    }

    override fun toggle(): Instruction {
        return Decrement(register)
    }

    override fun isValid(): Boolean {
        return isRegister(register)
    }
}

class Decrement(val register: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return modifyRegister(register, context) { oldValue -> oldValue - 1}
    }

    override fun toggle(): Instruction {
        return Increment(register)
    }

    override fun isValid(): Boolean {
        return isRegister(register)
    }
}

class JumpNotZero(val registerOrValue: String, val offset: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        val condition = valueOrRegisterContent(registerOrValue, context)
        val offsetValue = valueOrRegisterContent(offset, context)
        return if (condition != 0L) {
            ExecutionContext(context.processLine + offsetValue.toInt(), context.registers, context.code)
        } else {
            ExecutionContext(context.processLine + 1, context.registers, context.code)
        }
    }

    override fun toggle(): Instruction {
        return Copy(registerOrValue, offset)
    }

    override fun isValid(): Boolean = true
}

class Toggle(val registerOrValue: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        val line = context.processLine + valueOrRegisterContent(registerOrValue, context)
        val newCode = if (context.code.indices.contains(line)) {
            val code = context.code.toMutableList()
            val instruction = code[line.toInt()]
            code[line.toInt()] = instruction.toggle()
            code
        } else {
            context.code
        }
        return ExecutionContext(context.processLine + 1, context.registers, newCode)
    }

    override fun toggle(): Instruction {
        return Increment(registerOrValue)
    }

    override fun isValid(): Boolean {
        return true
    }
}

class SecondComputer {

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
            Regex("tgl ([a-zA-Z]|\\d+|-\\d+)") to { result: MatchResult ->
                val (registerOrValue) = result.destructured
                Toggle(registerOrValue)
            }
    ))

    fun processInstructions(rawInstructions: List<String>, registers: Map<String, Long>): Map<String, Long> {
        val program: List<Instruction> = parser.parseAll(rawInstructions.map(String::trim))
        val context = ExecutionContext(0, registers, program)
        val result = generateSequence(context, ExecutionContext::execute).first { it.isEnd() }
        return result.registers
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2016/day23/instructions.txt")
    val finalRegisters = SecondComputer().processInstructions(instructions,
            mapOf("a" to 12L, "b" to 0L, "c" to 0L, "d" to 0L))
    println(finalRegisters["a"])
}
