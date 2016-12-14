package cz.chali.advent.year2016.day11

import cz.chali.advent.combinations
import cz.chali.advent.year2015.input.Reader
import java.util.*

/**
 * This code implement just plain brute force approach. It works with example data but puzzle input is taking
 * long time. I have to rethink implementation to be better and prune possible space of exploration.
 * */

enum class ComponentType {
    GENERATOR, MICROCHIP
}

data class Component(val name: String, val type: ComponentType)
data class Building(val elevator: Int, val floors: List<Set<Component>>, val steps: Int) {

    fun isDone(): Boolean {
        return (0..floors.size - 2).all { floors[it].isEmpty() }
    }

    fun possibleNotDangerousStates(): List<Building> {
        val theoreticalGroupsForMove = floors[elevator].combinations(1) + floors[elevator].combinations(2)
        val possibleStates = possibleNewFloors().flatMap { newFloor ->
           theoreticalGroupsForMove.map { groupForMove ->
               val newFloors = floors.mapIndexed { elev, floor ->
                   if (elev == newFloor)
                       floor + groupForMove
                   else if (elev == elevator)
                       floor - groupForMove
                   else
                       floor
               }
               Building(newFloor, newFloors, steps + 1)
           }
        }
        return possibleStates.filter({ it.notDangerous() })
    }

    private fun notDangerous(): Boolean {
        return floors.all { floor ->
            val chips = floor.filter { it.type == ComponentType.MICROCHIP }
            chips.size == floor.size || chips.all { chip -> floor.filter { it.name == chip.name }.size == 2 }
        }
    }

    private fun possibleNewFloors(): List<Int> {
        return when {
            0 < elevator && elevator < floors.size - 1 -> listOf(elevator - 1, elevator + 1)
            elevator == 0 -> listOf(elevator + 1)
            else -> listOf(elevator - 1)
        }
    }
}

class GeneratorsAndChips {

    fun computeSteps(rawFloors: List<String>): Int {
        val initialBuilding = parse(rawFloors)

        val queue: Queue<Building> = PriorityQueue({ a, b -> a.steps.compareTo(b.steps) })
        var currentState = initialBuilding
        while(! currentState.isDone()) {
            val nextPossible = currentState.possibleNotDangerousStates()
            queue.addAll(nextPossible)
            currentState = queue.poll()
        }
        return currentState.steps
    }

    private fun parse(rawFloors: List<String>): Building {
        return Building(elevator = 0, floors = rawFloors.map { parseFloor(it) }, steps = 0)
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
        return hashSetOf(Component(parseName(name), ComponentType.valueOf(type.toUpperCase())))
    }

    private fun parseMultipleComponents(componentsPart: String): Set<Component> {
        val rawComponents = componentsPart.split(",")
        return rawComponents.map { rawComponent ->
            val (name, type) = removeUnnecessaryChars(rawComponent).split(" ")
            Component(parseName(name), ComponentType.valueOf(type.toUpperCase()))
        }.toHashSet()
    }

    private fun parseTwoComponents(componentsPart: String): Set<Component> {
        val rawComponents = componentsPart.split(" and a ")
        return rawComponents.map { rawComponent ->
            val (name, type) = removeUnnecessaryChars(rawComponent).split(" ")
            Component(parseName(name), ComponentType.valueOf(type.toUpperCase()))
        }.toHashSet()
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
    val floors = Reader().readFile("/cz/chali/advent/year2016/day11/floors.txt")
    val steps = GeneratorsAndChips().computeSteps(floors)
    println(steps)
}