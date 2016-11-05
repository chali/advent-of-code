package cz.chali.advent.year2015.day24

import cz.chali.advent.year2015.input.Reader

/**
 * We have list of package weights which we try to split into n groups with the same weight.
 * The first group must be the smallest as possible and we want to get quantum entanglement (product of all package
 * weights within the group) of that group.
 */
class PackagesArrangement(val numberOfGroups: Int) {

    fun computeSmallestQuantumEntanglement(rawPackages: List<String>): Long {
        val packages: List<Int> = parsePackages(rawPackages)
        val targetGroupWeight = targetGroupWeight(packages)

        val firstGroups = findGroupsOfTargetWeight(packages, targetGroupWeight)
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


    fun findGroupsOfTargetWeight(packages: List<Int>, targetWeight: Int): List<Set<Int>> {
        return generateSequence(1, { it + 1}).map { findGroupsOfTargetWeightAndSize(it, packages, targetWeight) }
                .first { it.isNotEmpty() }
    }

    fun findGroupsOfTargetWeightAndSize(groupSize: Int, packages: List<Int>, possibleSpace: Int): List<Set<Int>> {
        if (groupSize == 1) {
            return packages.filter { it == possibleSpace }.map { setOf(it) }
        } else {
            return packages.filter { it <= possibleSpace }.zip(0 .. packages.size).flatMap { pair ->
                val (packageWeight, index) = pair
                val remaining = packages.subList(index + 1, packages.size)
                findGroupsOfTargetWeightAndSize(groupSize - 1, remaining, possibleSpace - packageWeight).map { restGroup ->
                    restGroup.plusElement(packageWeight)
                }
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


