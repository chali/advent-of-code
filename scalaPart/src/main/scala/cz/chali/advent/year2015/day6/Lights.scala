package cz.chali.advent.year2015.day6

import cz.chali.advent.year2015.input.InputReader

case class Instruction(command: String, beginning: (Int, Int), end: (Int, Int))
case class Command(command: String, coordinates: (Int, Int))

object Lights {

    val horizontalSize = 1000
    val verticalSize = 1000

    type Grid = Vector[Int]

    def conductInstructions(rawInstructions: List[String], commandRules: (Int) => PartialFunction[String, Int]): Int = {
        val instructions: List[Instruction] = parseInstructions(rawInstructions)
        val lightChanges: Stream[Command] = instructions.toStream.flatMap((instruction: Instruction) => {
            for (
                x <- Stream.range[Int](instruction.beginning._1, instruction.end._1 + 1);
                y <- Stream.range[Int](instruction.beginning._2, instruction.end._2 + 1)
            ) yield Command(instruction.command, (x, y))
        })

        val finalGrid: Grid = lightChanges.foldLeft(createGrid())((currentGrid: Grid, command: Command) => {
            val index = indexInFlatStructure(command.coordinates)
            currentGrid.updated(index, commandRules(currentGrid(index)).apply(command.command))
        })
        finalGrid.sum
    }

    def parseInstructions(rawInstructions: List[String]): List[Instruction] = {
        rawInstructions.map(_.split(' ').toList).collect({
            case "turn" :: "on" :: beginning :: "through" :: end :: Nil =>
                Instruction("on", coordinatesToTuple(beginning), coordinatesToTuple(end))
            case "turn" :: "off" :: beginning :: "through" :: end :: Nil =>
                Instruction("off", coordinatesToTuple(beginning), coordinatesToTuple(end))
            case "toggle" :: beginning :: "through" :: end :: Nil =>
                Instruction("toggle", coordinatesToTuple(beginning), coordinatesToTuple(end))
        })
    }

    def coordinatesToTuple(point: String): (Int, Int) = {
        val coordinateComponents = point.split(',')
        (coordinateComponents(0).toInt, coordinateComponents(1).toInt)
    }

    def createGrid(): Grid = {
        Vector.fill(horizontalSize * verticalSize)(0)
    }

    def indexInFlatStructure(coordinates: (Int, Int)): Int = coordinates._1 * horizontalSize + coordinates._2

    def main(args: Array[String]) {
        val fileContent: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day6/instructions")
        val count = conductInstructions(fileContent, basicRules)
        println(s"Total count of lights on at the end: $count")

        val brightness = conductInstructions(fileContent, brightnessRules)
        println(s"Total brightness of lights on at the end: $brightness")
    }

    def basicRules(currentValue: Int): PartialFunction[String, Int] = {
        case "on" => 1
        case "off" => 0
        case "toggle" => if (currentValue == 1) 0 else 1
    }

    def brightnessRules(currentValue: Int): PartialFunction[String, Int] = {
        case "on" => currentValue + 1
        case "off" => Math.max(currentValue - 1, 0)
        case "toggle" => currentValue + 2
    }
}
