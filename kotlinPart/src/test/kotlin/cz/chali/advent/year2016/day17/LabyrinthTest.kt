package cz.chali.advent.year2016.day17

import io.kotlintest.specs.WordSpec

class LabyrinthTest : WordSpec() {
    init {
        "You" should {
            "find expected shortest path in labyrinth" {
                val path = Labyrinth().findPath(initialPassword = "ihgpwlah")
                path shouldBe "DDRRRD"
            }

            "find expected shortest path in labyrinth" {
                val path = Labyrinth().findPath(initialPassword = "kglvqrro")
                path shouldBe "DDUDRLRRUDRD"
            }

            "find expected shortest path in labyrinth" {
                val path = Labyrinth().findPath(initialPassword = "ulqzkmiv")
                path shouldBe "DRURDRUDDLLDLUURRDULRLDUUDDDRR"
            }

            "find expected longest path length in labyrinth" {
                val path = Labyrinth().findLongestPathLength(initialPassword = "ihgpwlah")
                path shouldBe 370
            }

            "find expected longest path length in labyrinth" {
                val path = Labyrinth().findLongestPathLength(initialPassword = "kglvqrro")
                path shouldBe 492
            }

            "find expected longest path length in labyrinth" {
                val path = Labyrinth().findLongestPathLength(initialPassword = "ulqzkmiv")
                path shouldBe 830
            }
        }
    }
}