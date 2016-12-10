package cz.chali.advent.year2016.day10

import io.kotlintest.specs.WordSpec

class BalanceBotsTest : WordSpec() {
    init {
        "You" should {
            "find bot with those two chips" {
                val instructions = listOf(
                        "value 5 goes to bot 2",
                        "bot 2 gives low to bot 1 and high to bot 0",
                        "value 3 goes to bot 1",
                        "bot 1 gives low to output 1 and high to bot 0",
                        "bot 0 gives low to output 2 and high to output 0",
                        "value 2 goes to bot 2"
                )
                val botId = BalanceBots().findBotWithGivenChips(setOf(3, 5), instructions)
                botId shouldBe 0
            }

            "find product of given outputs" {
                val instructions = listOf(
                        "value 5 goes to bot 2",
                        "bot 2 gives low to bot 1 and high to bot 0",
                        "value 3 goes to bot 1",
                        "bot 1 gives low to output 1 and high to bot 0",
                        "bot 0 gives low to output 2 and high to output 0",
                        "value 2 goes to bot 2"
                )
                val botId = BalanceBots().findProductOfGivenOutput(setOf(1, 2), instructions)
                botId shouldBe 6
            }
        }
    }
}