package cz.chali.advent

import io.kotlintest.specs.WordSpec

class UtilsTest : WordSpec() {
    init {
        "Function" should {
            "scan trough list" {
                val list = listOf(1, 2, 3, 5)
                val cumulative = list.scan(0, { element, previous -> element + previous })
                cumulative shouldBe listOf(0, 1, 3, 6, 11)
            }
        }

        "Function" should {
            "find combinations" {
                val list = listOf(1, 2, 3, 5)
                list.combinations(1) shouldBe listOf(listOf(1), listOf(2), listOf(3), listOf(5))
                list.combinations(2) shouldBe listOf(listOf(2, 1), listOf(3, 1), listOf(5, 1),
                        listOf(3, 2), listOf(5, 2), listOf(5, 3))
            }
        }
    }
}