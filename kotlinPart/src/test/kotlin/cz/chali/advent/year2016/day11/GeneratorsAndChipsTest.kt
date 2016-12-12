package cz.chali.advent.year2016.day11

import io.kotlintest.specs.WordSpec

class GeneratorsAndChipsTest : WordSpec() {
    init {
        "You" should {
            "get all components in the last floor in steps" {
                val currentFloors = listOf(
                        "The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.",
                        "The second floor contains a hydrogen generator.",
                        "The third floor contains a lithium generator.",
                        "The fourth floor contains nothing relevant."
                )
                val botId = GeneratorsAndChips().computeSteps(currentFloors)
                botId shouldBe 11
            }
        }
    }
}