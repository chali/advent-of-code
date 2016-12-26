package cz.chali.advent.year2016.day24

import io.kotlintest.specs.WordSpec

class DuctRobotTest : WordSpec() {
    init {
        "Robot" should {
            "find path in expected number of steps" {
                val grid = listOf(
                        "###########",
                        "#0.1.....2#",
                        "#.#######.#",
                        "#4.......3#",
                        "###########"
                )
                val steps = DuctRobot().findShortestPathThatConnectAll(grid)
                steps shouldBe 14
            }

            "find path in expected number of steps with return to start" {
                val grid = listOf(
                        "###########",
                        "#0.1.....2#",
                        "#.#######.#",
                        "#4.......3#",
                        "###########"
                )
                val steps = DuctRobot().findShortestPathThatConnectAll(grid, returnToStart = true)
                steps shouldBe 20
            }
        }
    }
}