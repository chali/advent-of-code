package cz.chali.advent.year2016.day8

import io.kotlintest.specs.WordSpec

class LittleScreenTest : WordSpec() {
    init {
        "Display" should {
            "show this much lights on" {
                val instructions = listOf(
                        "rect 3x2",
                        "rotate column x=1 by 1",
                        "rotate row y=0 by 4",
                        "rotate column x=1 by 1"
                )
                val count = LittleScreen(width = 7, height = 3).countEnabledLights(instructions)
                count shouldBe 6
            }
        }
    }
}