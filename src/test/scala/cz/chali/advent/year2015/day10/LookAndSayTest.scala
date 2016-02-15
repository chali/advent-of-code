package cz.chali.advent.year2015.day10

import org.scalatest.WordSpec

class LookAndSayTest extends WordSpec {

    "Elves" should {

        "look and say" in {
            assert(LookAndSay.lookAndSay("1") == "11")
            assert(LookAndSay.lookAndSay("11") == "21")
            assert(LookAndSay.lookAndSay("21") == "1211")
            assert(LookAndSay.lookAndSay("1211") == "111221")
            assert(LookAndSay.lookAndSay("111221") == "312211")
        }

    }
}
