package cz.chali.advent.year2016.day11

import cz.chali.advent.combinations
import cz.chali.advent.input.Reader
import java.util.*

enum class ComponentType {
    GENERATOR, MICROCHIP
}

data class Component(val name: String, val type: ComponentType) : Comparable<Component> {
    override fun compareTo(other: Component): Int {
        val nameComparison = name.compareTo(other.name)
        return if (nameComparison == 0)
            type.compareTo(other.type)
        else
            nameComparison
    }
}
data class Building(val elevator: Int, val floors: List<Set<Component>>) {

    val comparisonForm: String = comparisonForm()
    val hashcode = comparisonForm.hashCode()

    fun comparisonForm(): String {
        val floorsAsString = floors.map { floor ->
            floor.groupBy { it.name }.map {
                if (it.value.size > 1)
                    "P"
                else {
                    "${it.value[0].name}${it.value[0].type}"
                }
            }.joinToString(separator = ",", prefix = "[", postfix = "]")
        }.joinToString(separator = "")
        return "$elevator$floorsAsString"
    }

    val distance: Double =
        floors.mapIndexed { index, floor ->
            ((floors.size - 1) - index) * floor.size
        }.sum().toDouble()


    fun possibleNotDangerousStates(): List<Building> {
        val oneForMove = floors[elevator].combinations(1).toList()
        val twoForMove = floors[elevator].combinations(2).toList()

        val tryMoveTwoUp = moveUp(twoForMove)
        val tryMoveOneDown = moveDown(oneForMove)
        val moveUp = if (tryMoveTwoUp.isEmpty()) moveUp(oneForMove) else tryMoveTwoUp
        val moveDown = if (tryMoveOneDown.isEmpty()) moveDown(twoForMove) else tryMoveOneDown
        return moveUp + moveDown

    }

    private fun moveUp(groupsForMove: List<List<Component>>): List<Building> {
        if (elevator < floors.size - 1)
            return move(groupsForMove, elevator + 1)
        else
            return emptyList()
    }

    private fun moveDown(groupsForMove: List<List<Component>>): List<Building> {
        if (0 < elevator) {
            return move(groupsForMove, elevator - 1)
        } else {
            return emptyList()
        }
    }

    private fun move(groupsForMove: List<List<Component>>, newFloor: Int): List<Building> {
        val afterMove = groupsForMove.map { groupForMove ->
            val newFloors = floors.mapIndexed { elev, floor ->
                if (elev == newFloor)
                    TreeSet(floor + groupForMove)
                else if (elev == elevator)
                    TreeSet(floor - groupForMove)
                else
                    floor
            }
            Building(newFloor, newFloors)
        }
        return afterMove.filter({ it.notDangerous() })
    }

    private fun notDangerous(): Boolean {
        return floors.all { floor ->
            val chips = floor.filter { it.type == ComponentType.MICROCHIP }
            chips.size == floor.size || chips.all { chip -> floor.filter { it.name == chip.name }.size == 2 }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Building)
            return comparisonForm == other.comparisonForm
        else
            return false
    }

    override fun hashCode(): Int {
        return hashcode
    }
}

class GeneratorsAndChips {

    fun computeSteps(rawFloors: List<String>): Int {
        val initialBuilding = parse(rawFloors)
        val finalState = createFinalState(initialBuilding)
        return findShortestPathLength(initialBuilding, finalState)
    }

    private fun createFinalState(initialBuilding: Building): Building {
        val components = initialBuilding.floors.flatten().toSet()
        val newFloors = initialBuilding.floors.mapIndexed { index, floor ->
            if (index == initialBuilding.floors.size -1)
                components
            else
                emptySet()
        }
        return Building(initialBuilding.floors.size - 1, newFloors)
    }

    private fun findShortestPathLength(from: Building, to: Building): Int {
        // The set of nodes already evaluated.
        val closedSet = hashSetOf<Building>()
        // The set of currently discovered nodes still to be evaluated.
        // Initially, only the start node is known.
        val openSet = hashSetOf(from)
        // For each node, which node it can most efficiently be reached from.
        // If a node can be reached from many nodes, cameFrom will eventually contain the
        // most efficient previous step.
        val cameFrom = HashMap<Building, Building>()

        // For each node, the cost of getting from the start node to that node.
        val gScore = HashMap<Building, Double>()
        // The cost of going from start to start is zero.
        gScore[from] = 0.0
        // For each node, the total cost of getting from the start node to the goal
        // by passing by that node. That value is partly known, partly heuristic.
        val fScore = HashMap<Building, Double>()
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

            neighbors@ for (neighbor in location.possibleNotDangerousStates()) {
                if (! closedSet.contains(neighbor)) {
                    // The distance from start to a neighbor
                    val locationGScore = gScore.getOrElse(location, { Double.POSITIVE_INFINITY })
                    val neighborGScore = gScore.getOrElse(neighbor, { Double.POSITIVE_INFINITY })
                    val tentative_gScore =  locationGScore + distance(location, neighbor)
                    if (! (openSet.contains(neighbor)))
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

    private fun countStepsDuringMove(cameFrom: Map<Building, Building>, target: Building): Int {
        val path = generateSequence(target, { previous -> cameFrom[previous] }).toList()
        return path.size - 1
    }

    private fun distance(a: Building, b: Building): Double = Math.abs(a.distance - b.distance)

    private fun parse(rawFloors: List<String>): Building {
        return Building(elevator = 0, floors = rawFloors.map { parseFloor(it) })
    }

    private fun parseFloor(floor: String): Set<Component> {
        val componentsPart = floor.split("contains")[1]
        return when {
            hasComponents(componentsPart) -> emptySet<Component>()
            hasMoreThenTwoComponents(componentsPart) -> parseMultipleComponents(componentsPart)
            hasTwoComponents(componentsPart) -> parseTwoComponents(componentsPart)
            else -> parseOneComponent(componentsPart)

        }

    }

    private fun parseOneComponent(componentsPart: String): Set<Component> {
        val (name, type) = removeUnnecessaryChars(componentsPart).split(" ")
        return TreeSet(hashSetOf(Component(parseName(name), ComponentType.valueOf(type.toUpperCase()))))
    }

    private fun parseMultipleComponents(componentsPart: String): Set<Component> {
        val rawComponents = componentsPart.split(",")
        return TreeSet(rawComponents.map { rawComponent ->
            val (name, type) = removeUnnecessaryChars(rawComponent).split(" ")
            Component(parseName(name), ComponentType.valueOf(type.toUpperCase()))
        })
    }

    private fun parseTwoComponents(componentsPart: String): Set<Component> {
        val rawComponents = componentsPart.split(" and a ")
        return TreeSet(rawComponents.map { rawComponent ->
            val (name, type) = removeUnnecessaryChars(rawComponent).split(" ")
            Component(parseName(name), ComponentType.valueOf(type.toUpperCase()))
        })
    }

    private fun hasMoreThenTwoComponents(componentsPart: String): Boolean = componentsPart.contains(",")

    private fun hasComponents(componentsPart: String): Boolean = componentsPart.contains("nothing relevant")

    private fun hasTwoComponents(componentsPart: String): Boolean = componentsPart.contains(" and a ")

    private fun removeUnnecessaryChars(rawComponent: String): String =
            rawComponent.replace(" and a ", "").replace(" a ", "").replace(".", "")

    private fun parseName(componentName: String): String {
        if (componentName.contains("-")){
            return componentName.split("-").first()
        } else {
            return componentName
        }
    }
}

fun main(args: Array<String>) {
    val floors1 = Reader().readFile("/cz/chali/advent/year2016/day11/floors.txt")
    val steps1 = GeneratorsAndChips().computeSteps(floors1)
    println(steps1)

    val start = System.currentTimeMillis()
    val floors2 = Reader().readFile("/cz/chali/advent/year2016/day11/floors2.txt")
    val steps2 = GeneratorsAndChips().computeSteps(floors2)
    println(steps2)
    println(System.currentTimeMillis() - start)
}