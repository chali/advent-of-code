package cz.chali.advent.year2016.day14

import io.kotlintest.specs.WordSpec

class KeyGeneratorTest : WordSpec() {
    init {
        "You" should {
            "find index of 64th key" {
                val index = KeyGenerator().sourceForHash("abc", 64, 1)
                index shouldBe 22728
            }

            "find index of 64th key with more hashing" {
                val index = KeyGenerator().sourceForHash("abc", 64, 2017)
                index shouldBe 22551
            }
        }
    }
}