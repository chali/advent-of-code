package cz.chali.advent.year2016.day5

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter


class PasswordGenerator {

    val passwordLength = 8
    val passwordPosition = 0..(passwordLength - 1)

    fun generatePassword(roomId: String): String {
        val passwordChar = { hash: String -> hash[5]}
        return hashSequence(roomId)
                .map(passwordChar)
                .take(passwordLength)
                .joinToString(separator = "")
                .toLowerCase()
    }

    fun generatePasswordWithOrderSpecification(roomId: String): String {
        val isHashValid = { hash: String -> hash[5].isDigit() && passwordPosition.contains(hash[5].toString().toInt()) }
        val possibleHashes = hashSequence(roomId).filter(isHashValid)
        val orderToCharPair = { hash: String -> hash[5].toString().toInt() to hash[6] }
        val possiblePositionsAndChars = possibleHashes.map(orderToCharPair)
        val onlyFirstOccurrencesOnGivenPosition = possiblePositionsAndChars.distinctBy { it.first }.take(passwordLength)
        return onlyFirstOccurrencesOnGivenPosition.sortedBy { it.first }
                .map { it.second }
                .joinToString(separator = "")
                .toLowerCase()
    }

    fun hashSequence(roomId: String): Sequence<String> {
        return generateSequence(0, { it + 1})
                .map { roomId + it }
                .map { MessageDigest.getInstance("MD5").digest(it.toByteArray()) }
                .map { DatatypeConverter.printHexBinary(it) }
                .filter { it.startsWith("00000") }
    }
}

fun main(args: Array<String>) {
    val password = PasswordGenerator().generatePasswordWithOrderSpecification("ugkcyxxp")
    println(password)
}