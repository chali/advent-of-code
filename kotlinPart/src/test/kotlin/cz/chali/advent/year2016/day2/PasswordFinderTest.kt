package cz.chali.advent.year2016.day2

import io.kotlintest.specs.WordSpec

class PasswordFinderTest : WordSpec() {
    init {
        "You" should {
            "find password for entrance keypad based on given instructions" {
                val instructions = listOf(
                        "ULL",
                        "RRDDD",
                        "LURDL",
                        "UUUUD"

                )
                val password = PasswordFinder().find(instructions, EntranceKeyPad())
                password shouldBe "1985"
            }

            "find password for bathroom keypad based on given instructions" {
                val instructions = listOf(
                        "ULL",
                        "RRDDD",
                        "LURDL",
                        "UUUUD"

                )
                val password = PasswordFinder().find(instructions, BathroomKeyPad())
                password shouldBe "5DB3"
            }
        }
    }
}