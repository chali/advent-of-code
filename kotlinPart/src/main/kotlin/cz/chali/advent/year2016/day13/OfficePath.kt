package cz.chali.advent.year2016.day13

class OfficePath(val designerNumber: Int) {

    fun findPathLength(to: Pair<Int, Int>): Int {
        val map = buildAndComputeDistancesMap({ map ->
            targetNotFound(map, to)
        })
        return map[to.second][to.first]
    }

    fun allPointsWithingDistance(limit: Int): Int {
        val map = buildAndComputeDistancesMap({ map ->
            map.size <= limit + 1
        })
        return map.flatten().count { it >= 0 && it <= limit }
    }

     /* element in two dimensional array means -2 means unreachable, -1 is a wall >=0 distance from starting point */
    private fun buildAndComputeDistancesMap(endFunction: (List<List<Int>>) -> Boolean): List<List<Int>> {
         var map = startingMap()
         while (endFunction(map)) {
             map = fillAndExpand(map)
         }
         return map
    }

    private fun startingMap(): MutableList<MutableList<Int>> {
        return mutableListOf(mutableListOf(-2, -2), mutableListOf(-2, 0))
    }

    private fun targetNotFound(map: List<List<Int>>, to: Pair<Int, Int>): Boolean {
        return when {
            map.size <= to.second -> true
            map[to.second].size <= to.first -> true
            map[to.second][to.first] < 0 -> true
            else -> false
        }
    }

    private fun fillAndExpand(map: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        val filledMap = fill(map)
        return expand(filledMap)
    }

    private fun fill(map: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        var current = map
        var previous = mutableListOf<MutableList<Int>>()
        while (current != previous) {
            previous = current
            current = current.mapIndexed { y, row ->
                row.mapIndexed { x, element ->
                    fillElement(x, y, current)
                }.toMutableList()
            }.toMutableList()
        }
        return current
    }

    private fun fillElement(x: Int, y: Int, current: MutableList<MutableList<Int>>): Int {
        return if (current[y][x] != -1) {
            when {
                isWall(x, y) -> -1
                else -> computeDistance(x, y, current)
            }
        } else -1
    }

    private fun  computeDistance(x: Int, y: Int, current: MutableList<MutableList<Int>>): Int {
        val directions = listOf(
                Pair(x + 1, y),
                Pair(x - 1, y),
                Pair(x, y + 1),
                Pair(x, y - 1)
        )
        val minNeighbor: Int? = directions
                .filter { point -> current.indices.contains(point.second)
                        && current[point.second].indices.contains(point.first)
                }
                .map { current[it.second][it.first] }
                .filter { it >= 0 }
                .min()?.plus(1)

        val currentElement = current[y][x]
        return when {
            currentElement > 0 && minNeighbor != null -> Math.min(currentElement, minNeighbor)
            currentElement < -1 && minNeighbor != null -> minNeighbor
            else -> currentElement
        }
    }

    private fun expand(map: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        val expanded = map.map {
            it.add(-2)
            it
        }.toMutableList()
        expanded.add((0..expanded[0].size - 1).map { -2 }.toMutableList())
        return expanded
    }

    private fun isWall(x: Int, y: Int): Boolean {
        var number = (x * x + 3 * x + 2 * x * y + y + y * y) + designerNumber
        var ones = 0
        while (number > 0) {
            ones += number % 2
            number /= 2
        }
        return ones % 2 == 1
    }
}

fun main(args: Array<String>) {
    val count = OfficePath(1364).allPointsWithingDistance(50)
    println(count)
}