package cz.chali.advent.year2016.day7

import io.kotlintest.specs.WordSpec

class AddressVerificationTest : WordSpec() {
    init {
        "You" should {
            "find those rooms supporting TLS" {
                val addresses = listOf(
                        "abba[mnop]qrst",
                        "abcd[bddb]xyyx",
                        "aaaa[qwer]tyui",
                        "ioxxoj[asdfgh]zxcvbn"
                )
                val count = AddressVerification().howManySupportsTLS(addresses)
                count shouldBe 2
            }

            "find those rooms supporting SSL" {
                val addresses = listOf(
                        "aba[bab]xyz",
                        "xyx[xyx]xyx",
                        "aaa[kek]eke",
                        "zazbz[bzb]cdb"
                )
                val count = AddressVerification().howManySupportsSSL(addresses)
                count shouldBe 3
            }
        }
    }
}