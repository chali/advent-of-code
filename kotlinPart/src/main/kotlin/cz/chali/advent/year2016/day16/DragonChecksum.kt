package cz.chali.advent.year2016.day16


class DragonChecksum {

    fun fillAndComputeChecksum(initialState: String, diskSize: Int): String {
        val enoughData = generateSequence(initialState, { data ->
            "${data}0${reverse(data.reversed())}"
        }).first { it.length >= diskSize }
        val dataOnDisk = enoughData.take(diskSize)
        return checksum(dataOnDisk)
    }

    fun reverse(data: String): String = data.map { if (it == '0') '1' else '0' }.joinToString(separator = "")

    fun checksum(data: String): String {
        if (data.length % 2 == 1) {
            return data
        }
        val checksum = data.mapIndexed { index, char ->
            if (index % 2 == 0) {
                if (char == data[index+1])
                    "1"
                else
                    "0"
            } else {
                ""
            }
        }.joinToString(separator = "")
        return checksum(checksum)
    }
}

fun main(args: Array<String>) {
    val checksum = DragonChecksum().fillAndComputeChecksum("11011110011011101", 35651584)
    println(checksum)
}