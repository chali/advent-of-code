package cz.chali.advent.year2015.day5

import cz.chali.advent.input.InputReader

object NiceStrings {

    val vowels: List[Char] = "aeiou".toList
    val forbiddenSequences: List[String] = List("ab", "cd", "pq", "xy")

    def countNiceWords(words: List[String], rule: (String => Boolean)): Int = {
        words.count(rule)
    }

    def isNiceWordFirstVersion(word: String): Boolean = {
        requiredAmountOfVowels(word, 3) &&
        atLeastOneLetterTwiceInRow(word) &&
        noForbiddenSequences(word)
    }

    def requiredAmountOfVowels(word: String, ammount: Int): Boolean = {
        word.toList.count((char: Char) => vowels.contains(char)) >= ammount
    }

    def atLeastOneLetterTwiceInRow(word: String): Boolean = {
        word.sliding(2).exists(charPair => charPair.charAt(0) == charPair.charAt(1) )
    }

    def noForbiddenSequences(word: String): Boolean = {
        ! forbiddenSequences.exists(forbiddenSequence => word.contains(forbiddenSequence))
    }

    def isNiceWordSecondVersion(word: String): Boolean = {
        containsAtLeastOneNonOverlappingLetterPairs(word) &&
        containsLetterPairWithSingleLetterBetween(word)
    }

    def containsAtLeastOneNonOverlappingLetterPairs(word: String): Boolean = {
        val allLetterPairs = word.sliding(2)
        //we try to find pair from the beginning and from the end, when pair is non overlapping
        //and at least twice in the word index difference will be more then 1
        allLetterPairs.exists(pair => word.lastIndexOf(pair) - word.indexOf(pair) > 1)
    }

    def containsLetterPairWithSingleLetterBetween(word: String): Boolean = {
        word.sliding(3).exists(charPair => charPair.charAt(0) == charPair.charAt(2) )
    }

    def main(args: Array[String]) {
        val fileContent: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day5/strings")
        val count = countNiceWords(fileContent, isNiceWordFirstVersion)
        println(s"Total count of nice words is: $count")

        val count2 = countNiceWords(fileContent, isNiceWordSecondVersion)
        println(s"Total count of nice words is: $count2")
    }
}
