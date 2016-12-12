package cz.chali.advent

import io.kotlintest.specs.WordSpec

class UtilsTest : WordSpec() {
    init {
        "Function" should {
            "scan trough list" {
                val list = listOf(1, 2, 3, 5)
                val cumulative = list.scan(0, { previous, element -> element + previous })
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

        "Function" should {
            "find combinations" {
                val list = listOf(1)
                list.combinations(2) shouldBe emptyList<Int>()
            }
        }

        "Function" should {
            "find transpose lists" {
                val list = listOf(listOf(1, 2, 3, 5), listOf(6, 7, 8, 9))
                val transposed = list.transpose()
                transposed shouldBe listOf(
                        listOf(1, 6),
                        listOf(2, 7),
                        listOf(3, 8),
                        listOf(5, 9)
                )
            }
        }

        "Function" should {
            "find create sliding window" {
                val list = listOf(1, 2, 3, 5)
                val windows = list.sliding(2)
                windows shouldBe listOf(
                        listOf(1, 2),
                        listOf(2, 3),
                        listOf(3, 5)
                )
            }
        }
    }
}