package cz.chali.advent.year2015.day23

import io.kotlintest.specs.WordSpec

class FirstComputerTest : WordSpec() {
    init {
        "First computer" should {
            "process instructions and fill registers" {
                val instructions = listOf(
                        "inc a",
                        "jio a, +2",
                        "tpl a",
                        "inc a"
                )
                val registers = FirstComputer().processInstructions(instructions, registers = mapOf("a" to 0L))
                registers["a"] shouldBe 2L
            }
        }
    }
}