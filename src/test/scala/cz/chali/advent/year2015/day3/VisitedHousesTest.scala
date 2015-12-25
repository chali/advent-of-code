package cz.chali.advent.year2015.day3

import org.scalatest.WordSpec

class VisitedHousesTest extends WordSpec {

    "Santa" should {

        "visit 2 houses" in {
            assert(VisitedHouses.countVisitedHouses(">") == 2)
            assert(VisitedHouses.countVisitedHouses("^v^v^v^v^v") == 2)
        }

        "visit 4 houses" in {
            assert(VisitedHouses.countVisitedHouses("^>v<") == 4)
        }

    }
}
