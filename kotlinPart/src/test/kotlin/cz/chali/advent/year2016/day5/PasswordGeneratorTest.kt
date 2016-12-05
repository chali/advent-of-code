package cz.chali.advent.year2016.day5

import io.kotlintest.specs.WordSpec

class PasswordGeneratorTest : WordSpec() {
    init {
        "Password generato" should {
            "generate password for this room id" {
                val roomId = "abc"
                val password = PasswordGenerator().generatePassword(roomId)
                password shouldBe "18f47a30"
            }

            "generate password for this room id" {
                val roomId = "abc"
                val password = PasswordGenerator().generatePasswordWithOrderSpecification(roomId)
                password shouldBe "05ace8e3"
            }
        }
    }
}