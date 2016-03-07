package cz.chali.advent.year2015.day17

import org.scalatest.WordSpec

class ContainersTest extends WordSpec {

    "Elves" should {

        "be able to use 4 configuration to fit 25 liters" in {
            val containers = List("20", "15", "10", "5", "5")
            assert(Containers.findSolutionContainersCombinations(containers, 25).size == 4)
        }

        "be able to use 3 configuration when we skip configuration with minimum amount of containers" in {
            val containers = List("20", "15", "10", "5", "5")
            assert(Containers.findSolutionWithMinimumContainersCombinations(containers, 25).size == 3)
        }

    }
}
