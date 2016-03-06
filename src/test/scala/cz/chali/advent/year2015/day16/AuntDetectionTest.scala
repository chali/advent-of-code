package cz.chali.advent.year2015.day16

import org.scalatest.WordSpec

class AuntDetectionTest extends WordSpec {

    "You" should {

        "find aunt based on given clues" in {
            val clues: Map[String, (Int) => Boolean] = Map(
                "children" -> ((value) => value == 3),
                "cats" -> ((value) => value == 7),
                "samoyeds" -> ((value) => value == 2)
            )
            val aunts = List(
                "Sue 1: children: 3, cats: 7, akitas: 0",
                "Sue 2: children: 2, trees: 1, akitas: 0",
                "Sue 3: cars: 10, cats: 6, perfumes: 7",
                "Sue 4: samoyeds: 1, vizslas: 0, cars: 6",
                "Sue 5: cats: 1, trees: 3, perfumes: 10"
            )

            assert(AuntDetection.findAunt(aunts, clues) == 1)
        }

    }
}
