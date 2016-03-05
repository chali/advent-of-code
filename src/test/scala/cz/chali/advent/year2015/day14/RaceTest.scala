package cz.chali.advent.year2015.day14

import org.scalatest.WordSpec

class RaceTest extends WordSpec {

    val racers = List(
        "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
        "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."
    )

    "The winner" should {

        "end at the distance after 1000 seconds" in {
            assert(Race.distanceOfTheWinner(racers, 1000) == 1120)
        }

        "earn points after 1000 seconds" in {
            assert(Race.pointsOfTheWinner(racers, 1000) == 689)
        }

    }
}
