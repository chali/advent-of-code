package cz.chali.advent.year2016.day21

import io.kotlintest.specs.WordSpec


class ScrambledLettersTest : WordSpec() {
    init {
        "You" should {
            "scramble word based on instruction into expected password" {
                val instructions = listOf(
                        "swap position 4 with position 0",
                        "swap letter d with letter b",
                        "reverse positions 0 through 4",
                        "rotate left 1 step",
                        "move position 1 to position 4",
                        "move position 3 to position 0",
                        "rotate based on position of letter b",
                        "rotate based on position of letter d"
                )
                val password = ScrambledLetters().scramble("abcde", instructions)
                password shouldBe "decab"
            }

            "unscramble password based on instruction into expected word" {
                val instructions = listOf(
                        "swap position 4 with position 0",
                        "swap letter d with letter b",
                        "reverse positions 0 through 4",
                        "rotate left 1 step",
                        "move position 1 to position 4",
                        "move position 3 to position 0",
                        "rotate based on position of letter b",
                        "rotate based on position of letter d"
                )
                val word = ScrambledLetters().unscramble("decab", instructions)
                word shouldBe "abcde"
            }
        }
    }
}