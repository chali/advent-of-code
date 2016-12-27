package cz.chali.advent.year2016.day25

import io.kotlintest.specs.WordSpec

class ThirdComputerTest : WordSpec() {
    init {
        "Robot" should {
            "find path in expected number of steps" {
                val instructions = listOf(
                        "cpy 2 b",
                        "dec a",
                        "out c",
                        "out a",
                        "jnz b -2"
                )
                val steps = ThirdComputer().findGenerator(instructions)
                steps shouldBe 2L
            }
        }
    }
}