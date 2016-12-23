package cz.chali.advent.year2016.day22

import cz.chali.advent.InputParser
import cz.chali.advent.permutations
import cz.chali.advent.input.Reader
import cz.chali.advent.transpose
import java.util.*

data class Location(val x: Int, val y: Int) {
    fun equal(x: Int, y: Int) = this.x == x && this.y == y
}
data class Node(val location: Location, val capacity: Int, val used: Int) {
    val available: Int
        get() = capacity - used
}

data class Grid(val height: Int, val width: Int, val nodes: List<List<Node>>) {

    companion object Factory {
        fun create(nodes: List<Node>): Grid {
            val sortedNodes = nodes.sortedWith(Comparator { a, b ->
                val comparisonY = a.location.y.compareTo(b.location.y)
                if (comparisonY == 0) a.location.x.compareTo(b.location.x) else comparisonY
            })
            val lastNode = sortedNodes.last()
            val height = lastNode.location.y + 1
            val width = lastNode.location.x + 1
            val grid = (0 until height).map { y ->
                (0 until width).map { x ->
                    sortedNodes[y * width + x]
                }
            }
            return Grid(height, width, grid)
        }
    }

    fun next(from: Location, ignore: Location): List<Location> {
        val fromNode = nodes[from.y][from.x]
        val possibleLocation = listOf(
                Location(from.x - 1, from.y),
                Location(from.x + 1, from.y),
                Location(from.x, from.y - 1),
                Location(from.x, from.y + 1)
        ).filter { it.x >= 0 && it.x < width && it.y >= 0 && it.y < height}
        return possibleLocation.filter {
            nodes[it.y][it.x].used <= fromNode.available
        }.filter { it != ignore }
    }

    fun swapData(current: Location, to: Location): Grid {
        val currentNode = nodes[current.y][current.x]
        val toNode = nodes[to.y][to.x]
        val newCurrent = currentNode.copy(used = toNode.used)
        val newTo = toNode.copy(used = currentNode.used)
        return this.copy(nodes = nodes.mapIndexed { y, row ->
            row.mapIndexed { x, node ->
                if (current.equal(x, y))
                    newCurrent
                else if (to.equal(x, y))
                    newTo
                else
                    node
            }
        })
    }

    fun print() {
        val capacityLengths = nodes.transpose().map { column ->
            column.map { it.capacity.toString().length }.max() ?: throw IllegalStateException("No max length for capacity")
        }
        val usedLengths = nodes.transpose().map { column ->
            column.map {it.used.toString().length }.max() ?: throw IllegalStateException("No max length for used space")
        }
        nodes.forEach { row ->
            row.forEachIndexed { index, cell ->
                print(cell.capacity.toString().padStart(capacityLengths[index]) + "T/"
                        + cell.used.toString().padStart(usedLengths[index]) + "T ")
            }
            println()
        }
        println()
    }
}

data class FreeSpaceMoveResult(val grid: Grid, val steps: Int)

class GridComputing {
    private val header = 2
    private val parser = InputParser(mapOf(
            Regex("/dev/grid/node-x(\\d+)-y(\\d+) +(\\d+)T +(\\d+)T +\\d+T +\\d+%") to { result: MatchResult ->
                val (x, y, capacity, used) = result.destructured
                Node(Location(x.toInt(), y.toInt()), capacity.toInt(), used.toInt())
            }
    ))

    fun countViablePairs(rawNodes: List<String>): Int {
        val nodes = parser.parseAll(rawNodes.drop(header))
        return nodes.permutations(2).filter { pair ->
            val (first, second) = pair
            first.used > 0 && second.available >= first.used
        }.count()
    }

    fun findMinimumSteps(rawNodes: List<String>): Int {
        val nodes = parser.parseAll(rawNodes.drop(header))
        val grid = Grid.create(nodes)
        grid.print()

        var steps = 0
        var freeSpaceLocation = findFreeSpace(grid)
        var target = initialTarget(grid)
        var tempGrid = grid
        while (target != Location(0, 0)) {
            val targetForFreeSpace = wherePlaceFreeSpace(target)
            val result = moveFreeSpaceTo(freeSpaceLocation, targetForFreeSpace, tempGrid, target)
            tempGrid = result.grid
            tempGrid = tempGrid.swapData(target, targetForFreeSpace)
            steps += result.steps + 1
            freeSpaceLocation = target
            target = targetForFreeSpace

        }
        return steps
    }

    private fun findFreeSpace(grid: Grid): Location {
        return grid.nodes.flatten().first { it.used == 0 }.location
    }

    private fun initialTarget(grid: Grid): Location {
        return Location(grid.width - 1, 0)
    }

    private fun wherePlaceFreeSpace(target: Location): Location {
        return Location(target.x - 1, target.y)
    }

    private fun moveFreeSpaceTo(from: Location, to: Location, grid: Grid, ignore: Location): FreeSpaceMoveResult {
        // The set of nodes already evaluated.
        val closedSet = HashSet<Location>()
        // The set of currently discovered nodes still to be evaluated.
        // Initially, only the start node is known.
        val openSet = hashMapOf(Pair(from, grid))
        // For each node, which node it can most efficiently be reached from.
        // If a node can be reached from many nodes, cameFrom will eventually contain the
        // most efficient previous step.
        val cameFrom = HashMap<Location, Location>()

        // For each node, the cost of getting from the start node to that node.
        val gScore = HashMap<Location, Double>()
        // The cost of going from start to start is zero.
        gScore[from] = 0.0
        // For each node, the total cost of getting from the start node to the goal
        // by passing by that node. That value is partly known, partly heuristic.
        val fScore = HashMap<Location, Double>()
        // For the first node, that value is completely heuristic.
        fScore[from] = distance(from, to)

        while (openSet.isNotEmpty()) {
            val (location, gridStatus) = openSet.minBy {
                fScore.getOrElse(it.key, { Double.POSITIVE_INFINITY })
            } ?: throw IllegalStateException("Could not find min")
            if (location == to)
                return FreeSpaceMoveResult(gridStatus, countStepsDuringMove(cameFrom, location))
            openSet.remove(location)
            closedSet.add(location)

            neighbors@ for (neighbor in gridStatus.next(location, ignore)) {
                if (! closedSet.contains(neighbor)) {
                    // The distance from start to a neighbor
                    val locationGScore = gScore.getOrElse(location, { Double.POSITIVE_INFINITY })
                    val neighborGScore = gScore.getOrElse(neighbor, { Double.POSITIVE_INFINITY })
                    val tentative_gScore =  locationGScore + distance(location, neighbor)
                    if (! openSet.containsKey(neighbor))
                        openSet.put(neighbor, gridStatus.swapData(location, neighbor))
                    else if (tentative_gScore >= neighborGScore)
                        continue@neighbors

                    cameFrom[neighbor] = location
                    gScore[neighbor] = tentative_gScore
                    fScore[neighbor] = gScore[neighbor]!! + distance(neighbor, to)
                }
            }

        }
        throw IllegalStateException("Path not found")
    }

    private fun countStepsDuringMove(cameFrom: Map<Location, Location>, target: Location): Int {
        val path = generateSequence(target, { previous -> cameFrom[previous] }).toList()
        return path.size - 1
    }

    private fun distance(a: Location, b: Location): Double = (Math.abs(a.x - b.x) + Math.abs(a.y - b.y)).toDouble()
}

fun main(args: Array<String>) {
    val nodes = Reader().readFile("/cz/chali/advent/year2016/day22/nodes.txt")
    val count = GridComputing().countViablePairs(nodes)
    println(count)

    val steps = GridComputing().findMinimumSteps(nodes)
    println(steps)
}