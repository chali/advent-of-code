package cz.chali.advent.year2016.day12

import io.kotlintest.specs.WordSpec

class ComputerTest : WordSpec() {
    init {
        "Computer" should {
            "process instructions and read correct value from register" {
                val instructions = listOf(
                        "cpy 41 a",
                        "inc a",
                        "inc a",
                        "dec a",
                        "jnz a 2",
                        "dec a"
                )
                val registers = Computer().processInstructions(instructions, mapOf("a" to 0L))
                registers["a"] shouldBe 42L
            }
        }
    }
}