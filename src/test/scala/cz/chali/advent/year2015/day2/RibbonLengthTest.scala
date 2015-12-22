package cz.chali.advent.year2015.day2

import org.scalatest.WordSpec

class RibbonLengthTest extends WordSpec {

    "Elfs" should {

        "prepare 34ft of ribbon" in {
            assert(RibbonLength.computeLength(List("2x3x4")) == 34)
        }

        "prepare 14ft of ribbon" in {
            assert(RibbonLength.computeLength(List("1x1x10")) == 14)
        }

        "prepare 48ft of ribbon" in {
            assert(RibbonLength.computeLength(List("2x3x4", "1x1x10")) == 48)
        }

    }
}
