package cz.chali.advent.year2015.day8

import org.scalatest.WordSpec

class StringsTest extends WordSpec {

    "Santa" should {

        "count this length difference between string and unescaped version" in {
            val input = List(
                "\"\"",
                "\"abc\"",
                "\"aaa\\\"aaa\"",
                "\"\\x27\""
            )
            assert(Strings.countLengthDifferences(input, Strings.removeEscaping) == 12)
        }

        "count this length difference between string and escaped version" in {
            val input = List(
                "\"\"",
                "\"abc\"",
                "\"aaa\\\"aaa\"",
                "\"\\x27\""
            )
            assert(Strings.countLengthDifferences(input, Strings.escape) == 19)
        }
    }
}
