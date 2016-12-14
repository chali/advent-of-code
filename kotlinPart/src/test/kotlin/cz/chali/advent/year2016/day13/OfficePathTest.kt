package cz.chali.advent.year2016.day13

import io.kotlintest.specs.WordSpec


class OfficePathTest : WordSpec() {
    init {
        "You" should {
            "find path of expected length" {
                val length = OfficePath(10).findPathLength(to = Pair(7, 4))
                length shouldBe 11
            }

            "find path of expected length" {
                val length = OfficePath(1364).findPathLength(to = Pair(31,39))
                length shouldBe 86
            }

            "find number of points within reach" {
                val count = OfficePath(10).allPointsWithingDistance(3)
                count shouldBe 6
            }
        }
    }
}