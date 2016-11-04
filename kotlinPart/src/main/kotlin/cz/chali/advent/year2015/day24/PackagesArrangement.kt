package cz.chali.advent.year2015.day24

import cz.chali.advent.year2015.input.Reader
import java.util.*

/**
 * We have list of package weights which we try to split into n groups with the same weight.
 * The first group must be the smallest as possible and we want to get quantum entanglement (product of all package
 * weights within the group) of that group.
 *
 * General algorithm is rather naive and not performing super well. (for harder option it works around hour).
 * I find all groups of target weight. Then I take each group and I try to find other groups which doesn't share
 * elements with the first one one. (if you are trying to find last 2 groups you are find to find just one second
 * group and you know that third exists.)
 *
 * Other option could be consider {@link https://en.wikipedia.org/wiki/Composition_(combinatorics)}
 * Which I have found {@link http://mo.github.io/2016/01/09/advent-of-code.html}
 */
class PackagesArrangement(val numberOfGroups: Int) {

    fun computeSmallestQuantumEntanglement(rawPackages: List<String>): Long {
        val packages: List<Int> = parsePackages(rawPackages)
        val targetGroupWeight = targetGroupWeight(packages)

        val firstGroups = findFirstGroupsForEqualSplit(packages, targetGroupWeight)
        val smallestFirstGroups = allSmallestGroups(firstGroups)

        val smallestQuantumEntanglement = smallestQuantumEntanglement(smallestFirstGroups)
        return smallestQuantumEntanglement ?: throw IllegalStateException("Ideal configuration was not found")
    }

    fun parsePackages(rawPackages: List<String>): List<Int> {
        return rawPackages.map(String::toInt)
    }

    fun targetGroupWeight(packages: List<Int>): Int {
        val sumOfPackageWeights = packages.sum()
        if (sumOfPackageWeights % numberOfGroups != 0) {
            throw IllegalArgumentException("It is not possible to split package weights into $numberOfGroups equal packagesInGroups")
        }
        return sumOfPackageWeights / numberOfGroups
    }


    fun findFirstGroupsForEqualSplit(packages: List<Int>, targetWeight: Int): List<Set<Int>> {
        val exactGroups = findAllGroupsOfTargetWeight(setOf(), packages, targetWeight, ArrayList())
                .sortedBy { it.size }
        val firstGroupWithPossibleSplitOfAllPackages = exactGroups.filter { firstGroup ->
            existsNotOverlappingGroups(numberOfGroups - 1, firstGroup, exactGroups)
        }
        return firstGroupWithPossibleSplitOfAllPackages
    }

    fun findAllGroupsOfTargetWeight(group: Set<Int>, availablePackages: List<Int>, possibleSpace: Int,
                                    accumulator: MutableList<Set<Int>>): List<Set<Int>> {
        for (packageIndex in 0 .. availablePackages.size - 1) {
            val availablePackage = availablePackages[packageIndex]
            if (possibleSpace > availablePackage)
                findAllGroupsOfTargetWeight(group + availablePackage,
                        availablePackages.subList(packageIndex + 1, availablePackages.size),
                        possibleSpace - availablePackage, accumulator)
            else if (possibleSpace == availablePackage)
                accumulator.add(group + availablePackage)
        }
        return accumulator
    }

    fun existsNotOverlappingGroups(numberOfGroups: Int, selectedElements: Set<Int>, allGroups: List<Set<Int>>): Boolean {
        if (numberOfGroups == 2) {
            return allGroups.any { group -> group.none { element -> selectedElements.contains(element) } }
        } else {
            val nonOverLappingGroups: List<Set<Int>> = allGroups.filterNot { group ->
                group.any { element -> selectedElements.contains(element) }
            }
            return nonOverLappingGroups.any { nonOverLappingGroup: Set<Int> ->
                existsNotOverlappingGroups(numberOfGroups - 1, selectedElements + nonOverLappingGroup, nonOverLappingGroups)
            }
        }
    }

    fun allSmallestGroups(groups: List<Set<Int>>): List<Set<Int>> {
        val minimumFirstGroupSize = groups.map({ it.size }).min()
        return groups.filter {
            it.size ==  minimumFirstGroupSize
        }
    }

    fun smallestQuantumEntanglement(groups: List<Set<Int>>): Long? {
        return groups.map { it.map(Int::toLong) }
                .map { quantumEntanglement(it) }
                .min()
    }

    fun quantumEntanglement(packages: List<Long>): Long {
        return packages.reduce { a, b -> a * b }
    }
}

fun main(args: Array<String>) {
    val instructions = Reader().readFile("/cz/chali/advent/year2015/day24/packages")
    val smallestQuantumEntanglement = PackagesArrangement(3).computeSmallestQuantumEntanglement(instructions)
    println(smallestQuantumEntanglement)
}


