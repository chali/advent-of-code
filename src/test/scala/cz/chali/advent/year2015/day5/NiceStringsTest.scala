package cz.chali.advent.year2015.day5

import org.scalatest.WordSpec

class NiceStringsTest extends WordSpec {

    "Santa" should {

        "find 2 nice strings with first rule set" in {
            val words = List("ugknbfddgicrmopn", "aaa",
                "jchzalrnumimnmhp", "haegwjzuvuyypxyu", "dvszwmarrgswjxmb")
            assert(NiceStrings.countNiceWords(words, NiceStrings.isNiceWordFirstVersion) == 2)
        }

        "find 2 nice strings with second rule set" in {
            val words = List("qjhvhtzxzqqjkmpb", "xxyxx",
                "uurcxstgmygtbstg", "ieodomkazucvgmuy")
            assert(NiceStrings.countNiceWords(words, NiceStrings.isNiceWordSecondVersion) == 2)
        }
    }
}
