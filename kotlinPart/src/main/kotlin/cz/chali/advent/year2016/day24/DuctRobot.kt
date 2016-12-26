package cz.chali.advent.year2016.day24

import cz.chali.advent.combinations
import cz.chali.advent.input.Reader
import java.util.*

data class Location(val x: Int, val y: Int)
data class TargetPoint(val id: Char, val location: Location)
data class Connection(val a: TargetPoint, val b: TargetPoint, val length: Int)

class Grid(val nodes: List<List<Char>>) {
    companion object {
        fun create(rawGrid: List<String>) = Grid(rawGrid.map(String::toList))
    }

    fun shortestPathConnectingPoint(returnToStart: Boolean): Int {
        val targetPoints = findAllTargetPoints()
        val possibleConnections = targetPoints.combinations(2)
        val connections = possibleConnections.flatMap {
            val (a, b) = it
            val path = findShortestPathLength(a.location, b.location)
            listOf(Connection(a, b, path), Connection(b, a, path))
        }
        return shortestPathFromConnections(connections, targetPoints, returnToStart)
    }


    private fun findAllTargetPoints(): List<TargetPoint> {
        val allNodesAsPoint = nodes.mapIndexed { y, row ->
            row.mapIndexed { x, node ->
                TargetPoint(node, Location(x, y))
            }
        }.flatten()
        return allNodesAsPoint.filter { it.id.isDigit() }
    }

    private fun findShortestPathLength(from: Location, to: Location): Int {
        // The set of nodes already evaluated.
        val closedSet = hashSetOf<Location>()
        // The set of currently discovered nodes still to be evaluated.
        // Initially, only the start node is known.
        val openSet = hashSetOf(from)
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
            val location = openSet.minBy {
                fScore.getOrElse(it, { Double.POSITIVE_INFINITY })
            } ?: throw IllegalStateException("Could not find min")
            if (location == to)
                return countStepsDuringMove(cameFrom, location)
            openSet.remove(location)
            closedSet.add(location)

            neighbors@ for (neighbor in next(location)) {
                if (! closedSet.contains(neighbor)) {
                    // The distance from start to a neighbor
                    val locationGScore = gScore.getOrElse(location, { Double.POSITIVE_INFINITY })
                    val neighborGScore = gScore.getOrElse(neighbor, { Double.POSITIVE_INFINITY })
                    val tentative_gScore =  locationGScore + distance(location, neighbor)
                    if (! openSet.contains(neighbor))
                        openSet.add(neighbor)
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

    fun next(from: Location): List<Location> {
        val possibleLocation = listOf(
                Location(from.x - 1, from.y),
                Location(from.x + 1, from.y),
                Location(from.x, from.y - 1),
                Location(from.x, from.y + 1)
        ).filter { it.y >= 0 && it.y < nodes.size && it.x >= 0 && it.x < nodes[it.y].size }
        return possibleLocation.filter {
            nodes[it.y][it.x] != '#'
        }
    }

    private fun countStepsDuringMove(cameFrom: Map<Location, Location>, target: Location): Int {
        val path = generateSequence(target, { previous -> cameFrom[previous] }).toList()
        return path.size - 1
    }

    private fun distance(a: Location, b: Location): Double = (Math.abs(a.x - b.x) + Math.abs(a.y - b.y)).toDouble()

    private fun shortestPathFromConnections(connections: List<Connection>, points: List<TargetPoint>, returnToStart: Boolean): Int {
        val startingPoint = '0'
        val notCovered = points.map { it.id }.toSet() - startingPoint
        val pointsInProgress = PriorityQueue<SearchStatus>(Comparator { a, b -> a.length.compareTo(b.length) })
        var currentlyExplored = SearchStatus(startingPoint, notCovered, 0)
        while (currentlyExplored.notCovered.isNotEmpty()) {
            for (nextConnection in findConnectionsFrom(currentlyExplored.point, connections)) {
                val target = nextConnection.b.id
                val element = SearchStatus(target, currentlyExplored.notCovered - target,
                        currentlyExplored.length + nextConnection.length)
                val toAdd = if (returnToStart && element.notCovered.isEmpty()) {
                    addTripToStartingPoint(element, connections, startingPoint)
                } else {
                    element
                }
                pointsInProgress.add(toAdd)
            }
            currentlyExplored = pointsInProgress.poll()
        }
        return currentlyExplored.length
    }

    private fun findConnectionsFrom(from: Char, allConnections: List<Connection>): List<Connection> {
        return allConnections.filter { it.a.id == from }
    }

    data class SearchStatus(val point: Char, val notCovered: Set<Char>, val length: Int)

    private fun addTripToStartingPoint(element: SearchStatus, connections: List<Connection>, start: Char): SearchStatus {
        val connection = connections.find { it.a.id == element.point && it.b.id == start} ?: throw IllegalArgumentException()
        return SearchStatus(start, element.notCovered, element.length + connection.length)
    }
}

class DuctRobot {

    fun findShortestPathThatConnectAll(rawGrid: List<String>, returnToStart: Boolean = false): Int {
        val grid = Grid.create(rawGrid)
        return grid.shortestPathConnectingPoint(returnToStart)
    }
}

fun main(args: Array<String>) {
    val grid = Reader().readFile("/cz/chali/advent/year2016/day24/grid.txt")

    val steps = DuctRobot().findShortestPathThatConnectAll(grid)
    println(steps)

    val stepsWithReturn = DuctRobot().findShortestPathThatConnectAll(grid, true)
    println(stepsWithReturn)
}