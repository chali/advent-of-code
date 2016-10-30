package cz.chali.advent.year2015.day11

/**
 * Just brute force algorithm. I will generate word candidate by incrementation of one letter and then check whether
 * it fulfil password rules
 * */
object Passwords {

    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    val forbiddenChars = "iol"
    val straights = alphabet.sliding(3).toList
    val pairs = alphabet.toList.map(char => char.toString * 2)

    def generatePasswords(currentPassword: String): Stream[String] = {
        wordGenerator(currentPassword).filter(passwordCandidate => {
            ! containsForbiddenChars(passwordCandidate) &&
            containsPairs(passwordCandidate) &&
            containsStraight(passwordCandidate)
        })
    }

    def containsForbiddenChars(passwordCandidate: String): Boolean =
        forbiddenChars.toList.foldLeft(false)((result, char) =>
            result || passwordCandidate.contains(char.toString)
        )

    def containsPairs(passwordCandidate: String): Boolean =
        pairs.foldLeft(0)((result: Int, pair: String) =>
            result + (if (passwordCandidate.contains(pair)) 1 else 0)
        ) >= 2

    def containsStraight(passwordCandidate: String): Boolean =
        straights.foldLeft(false)((result, straight) =>
            result || passwordCandidate.contains(straight)
        )

    def wordGenerator(currentWord: String): Stream[String] = {
        val nextWord = incrementWord(currentWord)
        nextWord #:: wordGenerator(nextWord)
    }

    def incrementWord(currentWord: String): String = {
        currentWord.toList.reverse.foldLeft((true, ""))((overflowAccumulatorTuple: (Boolean, String), currentChar) => {
            val (overflow, accumulator) = overflowAccumulatorTuple
            if (overflow) {
                val next = nextChar(currentChar)
                (isOverFlow(currentChar, next), accumulator + next)
            } else {
                (false, accumulator + currentChar)
            }
        })._2.reverse
    }

    def nextChar(current: Char): Char = {
        val currentCharIndex = alphabet.indexOf(current)
        alphabet.charAt((currentCharIndex + 1) % 26)
    }

    def isOverFlow(current: Char, next: Char): Boolean = current.toInt > next.toInt

    def main(args: Array[String]) {
        val currentPassword = "cqjxjnds"
        val passwordStreams = generatePasswords(currentPassword)
        println(s"The next password is: ${passwordStreams.head}")
    }
}
