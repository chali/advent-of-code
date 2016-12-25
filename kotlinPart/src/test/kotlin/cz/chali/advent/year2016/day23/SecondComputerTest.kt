package cz.chali.advent.year2016.day23

import io.kotlintest.specs.WordSpec

class SecondComputerTest : WordSpec() {
    init {
        "Computer" should {
            "process instructions and read correct value from register" {
                val instructions = listOf(
                        "cpy 2 a",
                        "tgl a",
                        "tgl a",
                        "tgl a",
                        "cpy 1 a",
                        "dec a",
                        "dec a"
                )
                val registers = SecondComputer().processInstructions(instructions, mapOf("a" to 0L))
                registers["a"] shouldBe 3L
            }
        }

        "Computer" should {
            "process instructions and read correct value from register using optimization" {
                val instructions = listOf(
                        "cpy b c",
                        "inc a",
                        "dec c",
                        "jnz c -2",
                        "dec d",
                        "jnz d -5"
                )
                val registers = SecondComputer().processInstructions(instructions,
                        mapOf("a" to 0L, "b" to 2000000L, "c" to 0L, "d" to 3000L))
                registers["a"] shouldBe 6000000000L
            }
        }
    }
}