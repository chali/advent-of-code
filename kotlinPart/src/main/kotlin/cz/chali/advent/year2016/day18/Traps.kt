package cz.chali.advent.year2016.day18


class Traps {

    fun detectSafeSpotCount(initialLine: String, fieldLength: Int): Int {
        val trapField = generateSequence(initialLine, { previous -> computeNew(previous) }).take(fieldLength)
        return trapField.map { it.count { it == '.' } }.sum()
    }

    private fun computeNew(previous: String): String {
        return (0 until previous.length).map { index ->
            if (leftAndCenterMine(index, previous) ||
                    rightAndCenterMine(index, previous) ||
                    onlyLeftMine(index, previous) ||
                    onlyRightMine(index, previous)) {
                '^'
            } else {
                '.'
            }
        }.joinToString(separator = "")
    }

    private fun leftAndCenterMine(index: Int, line: String): Boolean {
        return isMine(index - 1, line) && isMine(index, line) && notMine(index + 1, line)
    }

    private fun rightAndCenterMine(index: Int, line: String): Boolean {
        return notMine(index - 1, line) && isMine(index, line) && isMine(index + 1, line)
    }

    private fun onlyLeftMine(index: Int, line: String): Boolean {
        return isMine(index - 1, line) && notMine(index, line) && notMine(index + 1, line)
    }

    private fun onlyRightMine(index: Int, line: String): Boolean {
        return notMine(index - 1, line) && notMine(index, line) && isMine(index + 1, line)
    }

    private fun isMine(index: Int, line: String): Boolean {
        if (index < 0 || index >= line.length)
            return false
        else
            return line[index] == '^'
    }

    private fun notMine(index: Int, line: String): Boolean = ! isMine(index, line)
}

fun main(args: Array<String>) {
    val initialLine = "......^.^^.....^^^^^^^^^...^.^..^^.^^^..^.^..^.^^^." +
            "^^^^..^^.^.^.....^^^^^..^..^^^..^^.^.^..^^..^^^.."
    val count = Traps().detectSafeSpotCount(initialLine, 400000)
    println(count)
}