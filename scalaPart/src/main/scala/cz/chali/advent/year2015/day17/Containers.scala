package cz.chali.advent.year2015.day17

import cz.chali.advent.input.InputReader

class Container(val capacity: Int)

object Containers {

    def findSolutionContainersCombinations(rawContainers: List[String], targetVolume: Int): IndexedSeq[List[Container]] = {
        val containers: List[Container] = rawContainers.map(c => new Container(c.toInt))
        val candidateCombinations = (1 to rawContainers.size).flatMap(containers.combinations)
        candidateCombinations.filter(candidate => candidate.map(_.capacity).sum == targetVolume)
    }

    def findSolutionWithMinimumContainersCombinations(rawContainers: List[String], targetVolume: Int): IndexedSeq[List[Container]] = {
        val solutions = findSolutionContainersCombinations(rawContainers, targetVolume)
        val minimumContainerCountInSolution = solutions.map(_.size).min
        solutions.filter(solution => solution.size == minimumContainerCountInSolution)
    }

    def main(args: Array[String]) {
        val containers: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day17/containers")
        val count: Int = findSolutionWithMinimumContainersCombinations(containers, 150).size
        println(s"Number of containers which fits exactly given volume: $count")
    }
}
