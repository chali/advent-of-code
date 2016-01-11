package cz.chali.advent.year2015.day6

import org.scalatest.WordSpec

class LightsTest extends WordSpec {

    "You" should {

        "see 998 996 lights after those instructions with basic rules" in {
            val instructions: List[String] = List(
                "turn on 0,0 through 999,999",
                "toggle 0,0 through 999,0",
                "turn off 499,499 through 500,500"
            )
            assert(Lights.conductInstructions(instructions, Lights.basicRules) == 998996)
        }

        "see 2000000 lights brightness after those instructions with brightness rules" in {
            val instructions: List[String] = List(
                "turn on 0,0 through 0,0",
                "toggle 0,0 through 999,999",
                "turn off 1,1 through 1,1"
            )
            assert(Lights.conductInstructions(instructions, Lights.brightnessRules) == 2000000)
        }

    }
}
