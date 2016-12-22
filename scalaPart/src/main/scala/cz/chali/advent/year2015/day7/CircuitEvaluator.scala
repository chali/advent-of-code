package cz.chali.advent.year2015.day7


import cz.chali.advent.input.InputReader



abstract class Gate {

    type Board = Map[String, Gate]

    var value: Option[Int] = None

    def valueOrEvaluate(input: String, board: Board): Int = {
        if (input matches """\d+""") input.toInt else board(input).evaluateOrGet(board)
    }

    def evaluateOrGet(board: Board): Int = {
        //i'm not completely satisfied with this but so far I don't have any idea how to do it better.
        value.getOrElse({
            val newValue = evaluate(board)
            value = Some(newValue)
            newValue
        })
    }

    def evaluate(board: Board): Int
}

class And(leftInput: String, rightInput: String) extends Gate {
    override def evaluate(board: Board): Int = {
        (valueOrEvaluate(leftInput, board) & valueOrEvaluate(rightInput, board)) & 0xFFFF
    }
}

class Or(leftInput: String, rightInput: String) extends Gate {
    override def evaluate(board: Board): Int = {
        (board(leftInput).evaluateOrGet(board) | board(rightInput).evaluateOrGet(board)) & 0xFFFF
    }
}

class RightShift(input: String, shift: Int) extends Gate {
    override def evaluate(board: Board): Int = {
        (board(input).evaluateOrGet(board) >> shift) & 0xFFFF
    }
}

class LeftShift(input: String, shift: Int) extends Gate {
    override def evaluate(board: Board): Int = {
        (board(input).evaluateOrGet(board) << shift) & 0xFFFF
    }
}

class Not(input: String) extends Gate {
    override def evaluate(board: Board): Int = {
        (~ board(input).evaluateOrGet(board)) & 0xFFFF
    }
}

class Signal(value: Int) extends Gate {
    override def evaluate(board: Board): Int = {
        value & 0xFFFF
    }
}

class Wire(input: String) extends Gate {
    override def evaluate(board: Board): Int = {
        board(input).evaluateOrGet(board) & 0xFFFF
    }
}

object CircuitEvaluator {

    def evaluate(description: List[String]): Map[String, Int] = {
         val board: Map[String, Gate] = description.map(parse).toMap
         board.mapValues(_.evaluate(board))
    }

    def parse(gateDescription: String): (String, Gate) = {
        val components: Array[String] = gateDescription.split("->").map(_.trim)
        components(1) -> parseGate(components(0))
    }

    def parseGate(description: String): Gate = {
        val parsedGateDescription = description.split(" ").toList
        parsedGateDescription match {
            case left :: "OR" :: right :: Nil => new Or(left, right)
            case left :: "AND" :: right :: Nil => new And(left, right)
            case "NOT" :: input :: Nil => new Not(input)
            case input :: "RSHIFT" :: shift :: Nil => new RightShift(input, shift.toInt)
            case input :: "LSHIFT" :: shift :: Nil => new LeftShift(input, shift.toInt)
            case input :: Nil if input matches """\d+""" => new Signal(input.toInt)
            case input :: Nil if ! (input matches """\d+""") => new Wire(input)
        }
    }

    def main(args: Array[String]) {
        val fileContent: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day7/circuitDescription")
        val signals: Map[String, Int] = evaluate(fileContent)
        val signalForA = signals("a")
        println(s"Signal for wire a: $signalForA")
    }
}
