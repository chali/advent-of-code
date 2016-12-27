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

    /* method tries to find expected sequence of primitive instruction and replace them with simple computation.
       Computer doesn't have to evaluate them. */
    private fun optimizeMultiplication(): ExecutionContext? {
        val optimization = MultiplicationOptimization(processLine, code)
        if (optimization.canBeCodeOptimized())
            return optimization.optimize(this)
        else
            return null
    }
}

class MultiplicationOptimization(private val processLine: Int, private val code: List<Instruction>) {
    fun canBeCodeOptimized(): Boolean {
        return codeHasAnotherFiveInstructions() && instructionsHaveExpectedTypesAndParameters()
    }

    private fun codeHasAnotherFiveInstructions(): Boolean {
        return code.indices.contains(processLine + 5)
    }

    private fun instructionsHaveExpectedTypesAndParameters(): Boolean {
        val (copy, inc, decInner, jnzInner, decOuter, jnzOuter) = code.subList(processLine, processLine + 6)
        return copy is Copy && inc is Increment && decInner is Decrement && jnzInner is JumpNotZero &&
                decOuter is Decrement && jnzOuter is JumpNotZero &&
                expectedInstructionsInputs(copy, decInner, decOuter, jnzInner, jnzOuter)
    }

    private fun expectedInstructionsInputs(copy: Copy, decInner: Decrement, decOuter: Decrement, jnzInner: JumpNotZero, jnzOuter: JumpNotZero): Boolean {
        return copy.targetRegister == decInner.register && copy.targetRegister == jnzInner.registerOrValue &&
                jnzInner.offset == "-2" && decOuter.register == jnzOuter.registerOrValue &&
                jnzOuter.offset == "-5"
    }

    private operator fun List<Instruction>.component6() = this[5]

    fun optimize(context: ExecutionContext): ExecutionContext {
        val target = whereStoreResult()
        val multiplication = modifyRegister(target, context, incrementLine = 6,
                operation = { previous -> previous + left(context) * right(context)})
        return toClear().fold(multiplication, { previousContext, registerToClear ->
            modifyRegister(registerToClear, previousContext, 0, { previous -> 0 })
        })
    }

    private fun whereStoreResult() = (code[processLine + 1] as Increment).register
    private fun left(context: ExecutionContext) =
            valueOrRegisterContent((code[processLine] as Copy).sourceRegisterOrValue, context)
    private fun right(context: ExecutionContext) =
            valueOrRegisterContent((code[processLine + 4] as Decrement).register, context)
    private fun toClear(): List<String> {
        return listOf(
                (code[processLine + 2] as Decrement).register,
                (code[processLine + 4] as Decrement).register
        )
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
