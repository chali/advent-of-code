package cz.chali.advent.year2016.day1

import io.kotlintest.specs.WordSpec

class GridTravelTest : WordSpec() {
    init {
        "You" should {
            "be in a distance 5 blocks after traveling with steps" {
                val steps = "R2, L3"
                val distance = GridTravel().shortestDistance(steps)
                distance shouldBe 5
            }

            "be in a distance 2 blocks after traveling with steps" {
                val steps = "R2, R2, R2"
                val distance = GridTravel().shortestDistance(steps)
                distance shouldBe 2
            }

            "be in a distance 12 blocks after traveling with steps" {
                val steps = "R5, L5, R5, R3"
                val distance = GridTravel().shortestDistance(steps)
                distance shouldBe 12
            }

            "be in a distance 12 blocks after traveling with steps to the first common point" {
                val steps = "R8, R4, R4, R8"
                val distance = GridTravel().shortestDistanceToFirstCommonPointOnPath(steps)
                distance shouldBe 4
            }
        }
    }
}