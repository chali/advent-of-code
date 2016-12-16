package cz.chali.advent.year2016.day16

import io.kotlintest.specs.WordSpec

class DragonChecksumTest : WordSpec() {
    init {
        "You" should {
            "fill dist and have expected checksum" {
                val checksum = DragonChecksum().fillAndComputeChecksum(initialState = "10000", diskSize = 20)
                checksum shouldBe "01100"
            }
        }
    }
}