package cz.chali.advent.year2015.day22

import org.scalatest.WordSpec

class WizardSimulatorTest extends WordSpec {

    "WizardSimulator" should {

        "find least amount of mana to win" in {
            val boss = List(
                "Hit Points: 13",
                "Damage: 8"
            )
            assert(WizardSimulator.leastAmountOfManaToWin(boss, 10, 250) == 226)
        }

    }
}
