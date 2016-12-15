package cz.chali.advent.year2016.day14

import cz.chali.advent.sliding
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter


class KeyGenerator {

    fun sourceForHash(salt: String, orderOfSearchedHash: Int, times: Int): Int {
        val targetHash = generateSequence(0, {i -> i + 1})
                .map { it to hash(salt, it, times) }
                .sliding(1001)
                .filter { window ->
                    val firstHash = window.first().second
                    val tripleChar = findTripleChar(firstHash)
                    if (tripleChar != null) {
                        val fiveChars = (1..5).map { tripleChar }.joinToString(separator = "")
                        window.drop(1).any { it.second.contains(fiveChars) }
                    } else {
                        false
                    }
                }
                .drop(orderOfSearchedHash - 1)
                .first().first()

        return targetHash.first
    }

    private fun hash(salt: String, it: Int, times: Int): String {
        return (1..times).fold((salt + it), { previous, foo ->
            DatatypeConverter.printHexBinary(MessageDigest.getInstance("MD5").digest(previous.toByteArray())).toLowerCase()
        })
    }

    private fun findTripleChar(hash: String): Char? {
        return hash.toList().sliding(3).firstOrNull({ it[0] == it[1] && it[1] == it[2] })?.first()
    }
}

fun main(args: Array<String>) {
    val index = KeyGenerator().sourceForHash("qzyelonm", 64, 2017)
    println(index)
}