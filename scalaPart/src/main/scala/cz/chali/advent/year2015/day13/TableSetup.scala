package cz.chali.advent.year2015.day13

import cz.chali.advent.input.InputReader

object TableSetup {

    type HappinessMapping = Map[String, Map[String, Int]]

    val lineParserRegex = """(\w+) would (\w+) (\d+) happiness units by sitting next to (\w+).""".r

    def computeBestHappiness(happinessChanges: List[String]): Int = {
        val parsedRows = happinessChanges.map(parse)
        val happinessMapping: HappinessMapping = parsedRows.groupBy(_._1).mapValues(
            (value: List[(String, String, Int)]) => value.map(v => v._2 -> v._3).toMap
        )
        val possiblePersons = happinessMapping.keys.toList
        possiblePersons.permutations.map(computeScore(happinessMapping)).max
    }

    def parse(line: String): (String, String, Int) = {
        line match {
            case lineParserRegex(person1, sign, happiness, person2) => (person1, person2, determineSign(sign) * happiness.toInt)
        }
    }

    def determineSign(sign: String): Int = if (sign == "gain") 1 else -1

    def computeScore(happinessMapping: HappinessMapping)(tableSetup: List[String]): Int = {
        Stream.continually(tableSetup).flatten.sliding(3).map(window => {
            val leftNeighbor = window(0)
            val person = window(1)
            val rightNeighbor = window(2)

            happinessMapping(person)(leftNeighbor) + happinessMapping(person)(rightNeighbor)
        }).take(tableSetup.size).sum
    }

    def main(args: Array[String]) {
        val data: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day13/tableSetup")
        val bestHappiness: Int = computeBestHappiness(data)
        println(s"Best happiness for table setup is: $bestHappiness")
    }
}
