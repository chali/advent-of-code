package cz.chali.advent.year2015.day10

object LookAndSay {

    def lookAndSay(input: String): String = {
        if (input.isEmpty)
            throw new IllegalArgumentException("Input cannot be empty")
        val inputAsList = input.toList
        val firstChar = inputAsList.head
        val resultAsTuples = inputAsList.tail.foldLeft(List((firstChar, 1)))(
            (currentResult: List[(Char, Int)], currentChar: Char) => {
                val previous = currentResult.head
                if (previous._1 == currentChar) {
                    (currentChar, previous._2 + 1) :: currentResult.tail
                } else {
                    (currentChar, 1) :: currentResult
                }
            }
        )
        resultAsTuples.reverse.map(tuple => s"${tuple._2}${tuple._1}").mkString
    }

    def main(args: Array[String]) {
        val startingInput = "1113222113"
        val output = (1 to 50).foldLeft(startingInput)((input, round) => lookAndSay(input))
        println(s"Look and say output length is: ${output.length}")
    }
}
