package cz.chali.advent.year2015.day19

import cz.chali.advent.input.InputReader

import scala.util.Random

object Medicine {

    val formulaParserRegex = """(.+) => (.+)""".r

    def findMolecules(formulas: List[String], input: String): Set[String] = {
        val parsedFormulas = formulas.map(parse)
        parsedFormulas.flatMap(findAllAndTransform(input)).toSet
    }

    def howManyStepsIsNeededTo(formulas: List[String], start: String, target: String): Int = {
        val parsedFormulas = formulas.map(parse).map(_.swap)

        def computeSteps(molecule: String, level: Int): Option[Int] = {
            if (molecule == start) {
                Some(level)
            } else {
                val candidates: List[String] = Random.shuffle(parsedFormulas
                    .flatMap(findAllAndTransform(molecule)))
                candidates.headOption.flatMap(computeSteps(_, level + 1))
            }
        }

        //monte carlo method. I try to find number of steps in 1000 attempts and
        //used formulas are randomly picked.
        (1 to 1000).map( attempt =>
            computeSteps(target, 0)
        ).filter(_.isDefined).map(_.get).min
    }

    def findAllAndTransform(input: String)(pattern: (String, String)): List[String] = {
        val (searched, replacement) = pattern
        val occurrenceIndexes = (0 until input.length).filter( inputIndex =>
            input.startsWith(searched, inputIndex)
        )
        occurrenceIndexes.map(beginningIndex => {
            val endIndex = beginningIndex + searched.length
            input.substring(0, beginningIndex) + replacement + input.substring(endIndex)
        }).toList
    }

    def parse(line: String): (String, String) = {
        line match {
            case formulaParserRegex(searched, replacement) => (searched, replacement)
        }
    }

    def main(args: Array[String]) {
        val input = "CRnCaSiRnBSiRnFArTiBPTiTiBFArPBCaSiThSiRnTiBPBPMgArCaSiRnTiMgArCaSiThCaSiRnFArRnSiRnFArTiTiBF" +
            "ArCaCaSiRnSiThCaCaSiRnMgArFYSiRnFYCaFArSiThCaSiThPBPTiMgArCaPRnSiAlArPBCaCaSiRnFYSiThCaRnFArArCaCaSiR" +
            "nPBSiRnFArMgYCaCaCaCaSiThCaCaSiAlArCaCaSiRnPBSiAlArBCaCaCaCaSiThCaPBSiThPBPBCaSiRnFYFArSiThCaSiRnFArB" +
            "CaCaSiRnFYFArSiThCaPBSiThCaSiRnPMgArRnFArPTiBCaPRnFArCaCaCaCaSiRnCaCaSiRnFYFArFArBCaSiThFArThSiThSiRn" +
            "TiRnPMgArFArCaSiThCaPBCaSiRnBFArCaCaPRnCaCaPMgArSiRnFYFArCaSiThRnPBPMgAr"
        val formulas: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day19/formulas")
        val foundMolecules: Int = howManyStepsIsNeededTo(formulas, "e", input)
        println(s"Number of transformations: $foundMolecules")
    }
}
