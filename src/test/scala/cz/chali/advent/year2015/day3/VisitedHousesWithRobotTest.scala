package cz.chali.advent.year2015.day3

import org.scalatest.WordSpec

class VisitedHousesWithRobotTest extends WordSpec {

    "Santa and Robot" should {

        "visit 3 houses" in {
            assert(VisitedHousesWithRobot.countVisitedHouses("^v") == 3)
            assert(VisitedHousesWithRobot.countVisitedHouses("^>v<") == 3)
        }

        "visit 11 houses" in {
            assert(VisitedHousesWithRobot.countVisitedHouses("^v^v^v^v^v") == 11)
        }

    }
}
