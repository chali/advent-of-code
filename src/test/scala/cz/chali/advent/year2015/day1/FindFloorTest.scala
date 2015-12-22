package cz.chali.advent.year2015.day1

import org.scalatest.WordSpecLike

class FindFloorTest extends WordSpecLike {

    "Santa" should {
        "end up in the first floor" in {
            assert(FindFloor.findFloor("(") == 1)
            assert(FindFloor.findFloor("(()()") == 1)
        }

        "end up in the third floor" in {
            assert(FindFloor.findFloor("(((") == 3)
        }

        "end up in the basement" in {
            assert(FindFloor.findFloor(")") == -1)
            assert(FindFloor.findFloor("())()") == -1)
        }
    }
}
