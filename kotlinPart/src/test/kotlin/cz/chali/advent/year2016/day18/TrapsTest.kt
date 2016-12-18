package cz.chali.advent.year2016.day18

import io.kotlintest.specs.WordSpec

class TrapsTest : WordSpec() {
    init {
        "You" should {
            "find expected number of safe spots" {
                val count = Traps().detectSafeSpotCount(initialLine = ".^^.^.^^^^", fieldLength = 10)
                count shouldBe 38
            }
        }
    }
}