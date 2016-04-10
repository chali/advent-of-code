package cz.chali.advent.year2015.day16

import cz.chali.advent.year2015.input.InputReader

case class Aunt(id: Int, information: Map[String, Int])

object AuntDetection {

    val lineParserRegex = """Sue (\d+): (.+)""".r

    def findAunt(rawAunts: List[String], clues: Map[String, (Int) => Boolean]): Int = {
        val aunts = rawAunts.map(parse)
        val filteredAunts = aunts.filter(checkInformation(clues))
        if (filteredAunts.size == 1)
            filteredAunts.head.id
        else
            throw new IllegalArgumentException("Clues are not specific enough")
    }

    def parse(line: String): Aunt = {
        line match {
            case lineParserRegex(id, rawInformation) => Aunt(id.toInt, parseInformation(rawInformation))
        }
    }

    def parseInformation(rawInformation: String): Map[String, Int] = {
        rawInformation
            .split(",")
            .map(_.trim)
            .map(rawPair => {
                val parsedPair = rawPair.split(":").map(_.trim)
                parsedPair(0) -> parsedPair(1).toInt
            }).toMap
    }

    def checkInformation(clues: Map[String, (Int) => Boolean])(aunt: Aunt): Boolean = {
        clues.foldLeft(true)((previousResult, cluePair) => {
            val potentialValue: Option[Int] = aunt.information.get(cluePair._1)
            if (potentialValue.isDefined)
                previousResult && cluePair._2(potentialValue.get)
            else
                previousResult
        })
    }

    def main(args: Array[String]) {
        val aunts: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day16/aunts")
        val clues: Map[String, (Int) => Boolean] = Map(
            "children" -> ((value) => value == 3),
            "cats" -> ((value) => value > 7),
            "samoyeds" -> ((value) => value == 2),
            "pomeranians" -> ((value) => value < 3),
            "akitas" -> ((value) => value == 0),
            "vizslas" -> ((value) => value == 0),
            "goldfish" -> ((value) => value < 5),
            "trees" -> ((value) => value > 3),
            "cars" -> ((value) => value == 2),
            "perfumes" -> ((value) => value == 1)
        )
        val id: Int = findAunt(aunts, clues)
        println(s"The aunt who sent gift is: $id")
    }
}
