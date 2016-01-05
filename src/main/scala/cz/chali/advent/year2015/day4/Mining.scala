package cz.chali.advent.year2015.day4

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

object Mining {

    def findCode(key: String, searchedPrefix: String): Int = {
        def computeAndTest(code: Int): Int = {
            val hash: Array[Byte] = md5(s"$key$code")
            val hashInHex: String = DatatypeConverter.printHexBinary(hash)

            if (hashInHex.startsWith(searchedPrefix)) {
                code
            } else {
                computeAndTest(code + 1)
            }
        }
        computeAndTest(1)
    }

    def md5(s: String): Array[Byte] = {
        MessageDigest.getInstance("MD5").digest(s.getBytes)
    }

    def main(args: Array[String]) {
        val key: String = "yzbqklnj"
        val code: Int = findCode(key, "00000")
        println(s"Code for key: $key is: $code")
        val code2: Int = findCode(key, "000000")
        println(s"Code for key: $key is: $code2")
    }
}
