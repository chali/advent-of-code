package cz.chali.advent.year2016.day6

import io.kotlintest.specs.WordSpec

class MessageCorrectorTest : WordSpec() {
    val messages = listOf(
            "eedadn",
            "drvtee",
            "eandsr",
            "raavrd",
            "atevrs",
            "tsrnev",
            "sdttsa",
            "rasrtv",
            "nssdts",
            "ntnada",
            "svetve",
            "tesnvt",
            "vntsnd",
            "vrdear",
            "dvrsen",
            "enarar"

    )

    init {
        "You" should {
            "correct messages" {
                val correctMessages = MessageCorrector().correct(messages)
                correctMessages shouldBe "easter"
            }

            "correct messages with least char occurrences" {
                val correctMessages = MessageCorrector().correctLeastCommon(messages)
                correctMessages shouldBe "advent"
            }
        }
    }
}