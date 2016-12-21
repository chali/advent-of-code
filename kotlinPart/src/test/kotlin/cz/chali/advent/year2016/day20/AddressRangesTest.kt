package cz.chali.advent.year2016.day20

import io.kotlintest.specs.WordSpec

class AddressRangesTest : WordSpec() {
    init {
        "You" should {
            "find the first open address" {
                val ranges = listOf(
                        "5-8",
                        "0-2",
                        "1-3",
                        "4-7"
                )
                val address = AddressRanges().findFirstNonBlockedAddress(ranges)
                address shouldBe 9L
            }

            "find a count of open addresses" {
                val ranges = listOf(
                        "8-9",
                        "0-5",
                        "2-4",
                        "2-3"
                )
                val count = AddressRanges().countNonBlockedAddress(ranges, 10)
                count shouldBe 2L
            }
        }
    }
}