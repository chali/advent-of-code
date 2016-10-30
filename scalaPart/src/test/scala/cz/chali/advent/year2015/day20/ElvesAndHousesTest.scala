package cz.chali.advent.year2015.day20

import org.scalatest.WordSpec

class ElvesAndHousesTest extends WordSpec {

    "Elves" should {

        "deliver at least x given presents to house with number" in {
            assert(ElvesAndHouses.findLowestHouseNumber(60) == 4)
            assert(ElvesAndHouses.findLowestHouseNumber(80) == 6)
        }

        "deliver at least x given presents but elves deliver only to one house" in {
            assert(ElvesAndHouses.findLowestHouseNumber(30, 1, 10) == 3)
        }
    }
}
