package cz.chali.advent.year2015.day1

import org.scalatest.WordSpecLike

class FindBasementTest extends WordSpecLike {

    "Santa" should {
        "hit basement in 1 steps" in {
            assert(FindBasement.findFirstBasementHit(")") == 1)
        }

        "hit basement in 3 steps" in {
            assert(FindBasement.findFirstBasementHit("())") == 3)
        }

        "never hit basement" in {
            assert(FindBasement.findFirstBasementHit("((") == -1)
        }
    }
}
