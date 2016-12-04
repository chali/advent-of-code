package cz.chali.advent.year2016.day4

import io.kotlintest.specs.WordSpec

class RoomNamesTest : WordSpec() {
    init {
        "You" should {
            "sum valid room names ids" {
                val roomNames = listOf(
                        "aaaaa-bbb-z-y-x-123[abxyz]",
                        "a-b-c-d-e-f-g-h-987[abcde]",
                        "not-a-real-room-404[oarel]",
                        "totally-real-room-200[decoy]"
                )
                val sum = RoomNames().sumValidRoomIds(roomNames)
                sum shouldBe 1514
            }

            "find code of desired room" {
                val roomNames = listOf(
                        "aaaaa-bbb-z-y-x-123[abxyz]",
                        "a-b-c-d-e-f-g-h-987[abcde]",
                        "not-a-real-room-404[oarel]",
                        "totally-real-room-200[decoy]"
                )
                val code = RoomNames().findCodeOfRoom(roomNames, "ttttt uuu s r q")
                code shouldBe 123
            }
        }
    }
}