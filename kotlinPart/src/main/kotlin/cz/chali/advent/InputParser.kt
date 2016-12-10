package cz.chali.advent

class InputParser<T>(val instructionRegexToParsingClosure: Map<Regex, (MatchResult) -> T>) {

    fun parseAll(rawInstructions: List<String>): List<T> {
        return rawInstructions.map { parse(it) }
    }

    private fun parse(rawInstruction: String): T {
        val matchedInstructionRegex: Regex = instructionRegexToParsingClosure.keys.first { it.matches(rawInstruction) }
        val instruction: T? = instructionRegexToParsingClosure[matchedInstructionRegex]?.let { parsingClosure: (MatchResult) -> T ->
            matchedInstructionRegex.matchEntire(rawInstruction)?.let(parsingClosure)
        }
        if (instruction != null)
            return instruction
        else
            throw IllegalStateException("Instruction '$rawInstruction' could not be parsed.")

    }
}


