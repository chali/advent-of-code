package cz.chali.advent.year2015.day23

import cz.chali.advent.year2015.input.Reader
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


class Half(val register: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return modifyRegister(register, context) { oldValue -> oldValue / 2 }
    }
}

class Triple(val register: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return modifyRegister(register, context) { oldValue -> oldValue * 3 }
    }
}

class Increment(val register: String) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return modifyRegister(register, context) { oldValue -> oldValue + 1}
    }
}

class Jump(val offset: Int) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return ExecutionContext(context.processLine + offset, context.registers)
    }
}

class JumpIfEven(val register: String, val offset: Int) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
       return if (context.loadRegister(register) % 2 == 0L) {
           ExecutionContext(context.processLine + offset, context.registers)
       } else {
           ExecutionContext(context.processLine + 1, context.registers)
       }
    }
}

class JumpIfOne(val register: String, val offset: Int) : Instruction() {
    override fun evaluate(context: ExecutionContext): ExecutionContext {
        return if (context.loadRegister(register) == 1L) {
            ExecutionContext(context.processLine + offset, context.registers)
        } else {
            ExecutionContext(context.processLine + 1, context.registers)
        }
    }
}

class InstructionParser {
    val instructionRegexToParsingClosure: Map<Regex, (MatchResult) -> Instruction> = mapOf(
            Regex("inc ([a-zA-Z])") to { result: MatchResult ->
                val (register) = result.destructured
                Increment(register)
            },
            Regex("tpl ([a-zA-Z])") to { result: MatchResult ->
                val (register) = result.destructured
                Triple(register)
            },
            Regex("hlf ([a-zA-Z])") to { result: MatchResult ->
                val (register) = result.destructured
                Half(register)
            },
            Regex("jmp ([+,-])(\\d+)") to { result: MatchResult ->
                val (sign, offset) = result.destructured
                Jump((if (sign == "-") -1 else 1) * Integer.parseInt(offset))
            },
            Regex("jie ([a-zA-Z]), ([+,-])(\\d+)") to { result: MatchResult ->
                val (register, sign, offset) = result.destructured
                JumpIfEven(register, (if (sign == "-") -1 else 1) * Integer.parseInt(offset))
            },
            Regex("jio ([a-zA-Z]), ([+,-])(\\d+)") to { result: MatchResult ->
                val (register, sign, offset) = result.destructured
                JumpIfOne(register, (if (sign == "-") -1 else 1) * Integer.parseInt(offset))
            }
    )

    fun parse(rawInstruction: String): Instruction {
        val matchedInstructionRegex: Regex = instructionRegexToParsingClosure.keys.first { it.matches(rawInstruction) }
        val instruction: Instruction? = instructionRegexToParsingClosure[matchedInstructionRegex]?.let { parsingClosure: (MatchResult) -> Instruction ->
            matchedInstructionRegex.matchEntire(rawInstruction)?.let(parsingClosure)
        }
        if (instruction != null)
            return instruction
        else
            throw IllegalStateException("Instruction '$rawInstruction' could not be parsed.")

    }
}


class FirstComputer {
    fun processInstructions(rawInstructions: List<String>, registers: Map<String, Long>): Map<String, Long> {
        val program: List<Instruction> = parseProgram(rawInstructions)
        var context = ExecutionContext(0, registers)
        while (0 <= context.processLine && context.processLine < program.size) {
            val instruction = program[context.processLine]
            context = instruction.evaluate(context)
        }
        return context.registers
    }

    private fun parseProgram(rawInstructions: List<String>): List<Instruction> {
        val parser = InstructionParser()
        return rawInstructions.map(String::trim).map { parser.parse(it) }
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2015/day23/instructions")
    val finalRegisters = FirstComputer().processInstructions(instructions, mapOf("a" to 0L, "b" to 0L))
    println(finalRegisters["b"])
}