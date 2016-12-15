package cz.chali.advent.year2016.day15

import io.kotlintest.specs.WordSpec

class DiscsTest : WordSpec() {
    init {
        "You" should {
            "press button at time to fall console through" {
                val discs = listOf(
                        "Disc #1 has 5 positions; at time=0, it is at position 4.",
                        "Disc #2 has 2 positions; at time=0, it is at position 1."
                )
                val time = Discs().timeWhenConsoleFallsThrough(discs)
                time shouldBe 5
            }
        }
    }
}