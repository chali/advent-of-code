package cz.chali.advent.year2015.day18

import org.scalatest.WordSpec

class LightsOfLifeTest extends WordSpec {

    "Santa" should {

        "see 17 lights in grid after 5 steps" in {
            val grid = List(
                ".#.#.#",
                "...##.",
                "#....#",
                "..#...",
                "#.#..#",
                "####.."
            )
            assert(LightsOfLife.countOnLights(grid, 5) == 17)
        }

    }
}
