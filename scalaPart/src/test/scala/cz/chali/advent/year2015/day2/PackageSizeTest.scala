package cz.chali.advent.year2015.day2

import org.scalatest.WordSpec

class PackageSizeTest extends WordSpec {

    "Elfs" should {

        "prepare 58sqf of paper" in {
            assert(PackageSize.computeSize(List("2x3x4")) == 58)
        }

        "prepare 43sqf of paper" in {
            assert(PackageSize.computeSize(List("1x1x10")) == 43)
        }

        "prepare 101sqf of paper" in {
            assert(PackageSize.computeSize(List("2x3x4", "1x1x10")) == 101)
        }
    }
}
