package cz.chali.advent.year2015.day25

data class Cell(val column: Int, val row: Int, val value: Long)

class CodeGenerator(val initialNumber: Long = 20151125) {

    fun generate(column: Int, row: Int): Long {
        val tableSequence = generateSequence(Cell(1, 1, initialNumber), { previous: Cell ->
            val newColumn = if (previous.row == 1) 1 else previous.column + 1
            val newRow = if (previous.row == 1) previous.column + 1 else previous.row - 1
            val newValue = previous.value * 252533L % 33554393
            Cell(newColumn, newRow, newValue)
        })
        val target: Cell = tableSequence.first { cell: Cell -> cell.row == row && cell.column == column }
        return target.value
    }
}

fun main(args: Array<String>) {
    val code = CodeGenerator().generate(column = 3075, row = 2981)
    println(code)
}
