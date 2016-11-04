package cz.chali.advent.year2015.day24

import io.kotlintest.specs.WordSpec

class PackagesArrangementTest : WordSpec() {
    init {
        "Sleuth" should {
            "contain 3 groups with the first group with quantum entanglement" {
                val packages = listOf("1", "2", "3", "4", "5", "7", "8", "9", "10", "11")
                val quantumEntanglement = PackagesArrangement(3).computeSmallestQuantumEntanglement(packages)
                quantumEntanglement shouldBe 99L
            }

            "contain 4 groups with the first group with quantum entanglement" {
                val packages = listOf("1", "2", "3", "4", "5", "7", "8", "9", "10", "11")
                val quantumEntanglement = PackagesArrangement(4).computeSmallestQuantumEntanglement(packages)
                quantumEntanglement shouldBe 44L
            }
        }
    }
}
