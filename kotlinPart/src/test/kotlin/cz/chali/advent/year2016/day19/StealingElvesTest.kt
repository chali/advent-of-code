package cz.chali.advent.year2016.day19

import io.kotlintest.specs.WordSpec

class StealingElvesTest : WordSpec() {
    init {
        "You" should {
            "find the latest elf who will get all presents" {
                val elf = StealingElves().whoWillGetEverything(5)
                elf shouldBe 3
            }

            "find the latest elf who will get all presents" {
                StealingElves().whoWillGetEverythingStealingFromOpposite(5) shouldBe 2
                StealingElves().whoWillGetEverythingStealingFromOpposite(6) shouldBe 3
                StealingElves().whoWillGetEverythingStealingFromOpposite(7) shouldBe 5
                StealingElves().whoWillGetEverythingStealingFromOpposite(8) shouldBe 7
                StealingElves().whoWillGetEverythingStealingFromOpposite(9) shouldBe 9
                StealingElves().whoWillGetEverythingStealingFromOpposite(10) shouldBe 1
            }
        }
    }
}