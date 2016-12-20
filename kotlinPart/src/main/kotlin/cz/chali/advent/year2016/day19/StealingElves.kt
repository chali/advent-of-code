package cz.chali.advent.year2016.day19


class StealingElves {

    fun whoWillGetEverything(elfCount: Int): Int {
        var elves = (1..elfCount).toList()
        while (elves.size > 1) {
            elves = elves.filterIndexed { index, elf ->
                if (index == 0 && elves.size % 2 == 1)
                    false
                else if (index % 2 == 0)
                    true
                else
                    false
            }
        }
        return elves.first()
    }

    /**
     * I reorder elves so first one is the first removed then pattern of removing depends on whether list has even
     * or odd elements. if even remove very third element if odd remove every index which have modulo == 2
     * Special case is last index if it is dividable by 3 then it is also removed.
     */
    fun whoWillGetEverythingStealingFromOpposite(elfCount: Int): Int {
        val elves = (1..elfCount).toList()
        var startingPoint = elves.size / 2
        var temp = elves.takeLast(elves.size - startingPoint) + elves.take(startingPoint)
        while (temp.size > 2) {
            temp = temp.filterIndexed { index, elf -> (index == temp.lastIndex && (index + 1) % 3 != 0) ||  (index + 1) % 3 == if (temp.size % 2 == 1) 2 else 0}
            temp = temp.sorted()
            startingPoint = temp.size / 2
            temp = temp.takeLast(temp.size - startingPoint) + temp.take(startingPoint)

        }
        return if (temp.size == 1) temp.first() else temp.component2()
    }
}

fun main(args: Array<String>) {
    val elf = StealingElves().whoWillGetEverythingStealingFromOpposite(3004953)
    println(elf)
}