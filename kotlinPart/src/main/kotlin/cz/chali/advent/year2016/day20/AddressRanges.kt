package cz.chali.advent.year2016.day20

import cz.chali.advent.InputParser
import cz.chali.advent.input.Reader

data class MergedAndRest(val merged: LongRange, val rest: List<LongRange>)

class AddressRanges {

    val parser = InputParser(mapOf(
            Regex("(\\d+)-(\\d+)") to { result: MatchResult ->
                val (start, end) = result.destructured
                start.toLong() .. end.toLong()
            }))

    fun findFirstNonBlockedAddress(rawBlockedRanges: List<String>): Long {
        val result = parseAndMergeOverLappingRanges(rawBlockedRanges)
        return result.first().last + 1
    }

    fun countNonBlockedAddress(rawBlockedRanges: List<String>, allAddressCount: Long): Long {
        val result = parseAndMergeOverLappingRanges(rawBlockedRanges)
        val blocked = result.map { (it.last - it.first) + 1 }.sum()
        return allAddressCount - blocked
    }

    private fun parseAndMergeOverLappingRanges(rawBlockedRanges: List<String>): List<LongRange> {
        val ranges = parser.parseAll(rawBlockedRanges).sortedBy { it.start }
        return mergeOverLappingRanges(emptyList(), ranges)
    }

    tailrec private fun mergeOverLappingRanges(merged: List<LongRange>, forMerge: List<LongRange>): List<LongRange> {
        if (forMerge.isEmpty())
            return merged
        else {
            val (newMerged, rest) = mergeRanges(MergedAndRest(forMerge.first(), forMerge))
            return mergeOverLappingRanges(merged.plusElement(newMerged), rest)
        }
    }

    tailrec private fun mergeRanges(mergedAndRest: MergedAndRest): MergedAndRest {
        val (merged, rest) = mergedAndRest
        val overlappingWithMerged = rest.takeWhile { it.start <= merged.last + 1 }
        if (overlappingWithMerged.isEmpty())
            return mergedAndRest
        else {
            val highestBlockedFromOverlapping = overlappingWithMerged.maxBy { it.last } ?: throw IllegalStateException("Could not find max")
            val newMerged = if (merged.last < highestBlockedFromOverlapping.last)
                merged.first..highestBlockedFromOverlapping.last
            else
                merged
            return mergeRanges(MergedAndRest(newMerged, rest.drop(overlappingWithMerged.size)))
        }
    }
}

fun main(args: Array<String>) {
    val ranges = Reader().readFile("/cz/chali/advent/year2016/day20/ranges.txt")
    val address = AddressRanges().findFirstNonBlockedAddress(ranges)
    println(address)
    val count = AddressRanges().countNonBlockedAddress(ranges, 4294967296)
    println(count)
}