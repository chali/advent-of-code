package cz.chali.advent.year2015.day21

import org.scalatest.WordSpec

class BossFightTest extends WordSpec {

    "Player" should {

        "defeat boss with gear for this price" in {
            val boss = List(
                "Hit Points: 99",
                "Damage: 5",
                "Armor: 2"
            )
            assert(BossFight.findLowestGearPriceToWin(boss) == 38)
        }

        "lost with boss with gear for this price" in {
            val boss = List(
                "Hit Points: 99",
                "Damage: 5",
                "Armor: 2"
            )
            assert(BossFight.findHighestGearPriceToLose(boss) == 58)
        }
    }
}