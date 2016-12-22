package cz.chali.advent.year2016.day7

import cz.chali.advent.sliding
import cz.chali.advent.input.Reader


class AddressVerification {

    fun howManySupportsTLS(addresses: List<String>): Int {
        val addressGroups = createAddressGroups(addresses)
        return addressGroups.count { addressGroups ->
            addressGroups.filter { ! isHyperNet(it) }.any { findAtta(it) } &&
                    addressGroups.filter { isHyperNet(it) }.none { findAtta(it) }
        }
    }

    fun  howManySupportsSSL(addresses: List<String>): Int {
        val addressGroups = createAddressGroups(addresses)
        return addressGroups.count { addressGroups ->
            val atas = collectAtas(addressGroups.filter { ! isHyperNet(it) })
            containsReverseAta(addressGroups.filter { isHyperNet(it) }, atas)
        }
    }

    private fun containsReverseAta(addressGroups: List<String>, atas: List<String>): Boolean {
        return addressGroups.any { addressGroup ->
            atas.any { ata ->
                addressGroup.contains("${ata[1]}${ata[0]}${ata[1]}")
            }
        }
    }

    private fun collectAtas(addressGroups: List<String>): List<String> {
        return addressGroups.flatMap { addressGroup ->
            addressGroup.toList().sliding(3)
                    .filter {
                        it[0] != it[1] && it[0] == it[2]
                    }
                    .map { it.joinToString(separator = "") }
        }
    }

    private fun createAddressGroups(addresses: List<String>): List<List<String>> {
        return addresses.map { address ->
            val bracketIndex = address.mapIndexed { index, char ->
                if (char == '[')
                    index
                else if (char == ']')
                    index + 1
                else
                    -1
            }.filter { it >= 0 }
            val groupIndices = (listOf(0, address.length) + bracketIndex).sorted()
            groupIndices.sliding(2).map { indices ->
                val (beginning, end) = indices
                address.substring(beginning, end)
            }
        }
    }

    /* finds sequences like abba in string*/
    private fun findAtta(string: String): Boolean {
        return (0.. string.length - 4).any { index ->
            string[index] != string[index + 1] &&
                    string[index] == string[index + 3] &&
                    string[index + 1] == string[index+2]
        }
    }

    private fun isHyperNet(string: String): Boolean = string.startsWith('[')

}

fun main(args: Array<String>) {
    val addresses = Reader().readFile("/cz/chali/advent/year2016/day7/addresses.txt")
    val count = AddressVerification().howManySupportsSSL(addresses)
    println(count)
}