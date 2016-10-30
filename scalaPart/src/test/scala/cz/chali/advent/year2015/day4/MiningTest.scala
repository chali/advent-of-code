package cz.chali.advent.year2015.day4

import org.scalatest.WordSpec

class MiningTest extends WordSpec {

    "Santa" should {

        "find integer 609043" in {
            assert(Mining.findCode("abcdef", "00000") == 609043)
        }

        "find integer 1048970" in {
            assert(Mining.findCode("pqrstuv", "00000") == 1048970)
        }

    }
}
