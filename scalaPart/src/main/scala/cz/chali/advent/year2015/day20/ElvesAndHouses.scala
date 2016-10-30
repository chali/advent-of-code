package cz.chali.advent.year2015.day20

object ElvesAndHouses {

    def findLowestHouseNumber(numberOfPresents: Int, houseLimit: Double = Double.PositiveInfinity,
                              presentsPerElf: Int = 10): Int = {
        val houses: Stream[Int] = Stream.from(1)
        val presents = houses.map(houseNumber => {
            (1 to Math.sqrt(houseNumber).toInt)
                .filter(houseNumber % _ == 0)
                .flatMap(elfNumber => List(elfNumber, houseNumber / elfNumber))
                .toSet[Int]
                .filter(houseNumber / _ <= houseLimit)
                .sum * presentsPerElf
        })
        val sumOfElves = numberOfPresents
        presents.indexWhere(_ >= sumOfElves) + 1
    }

    def main(args: Array[String]) {
        val numberOfPresents: Int = 36000000
        val houseNumber: Int = findLowestHouseNumber(numberOfPresents)
        println(s"Number of house: $houseNumber")
        val houseNumberWithLimitedElves: Int = findLowestHouseNumber(numberOfPresents, 50, 11)
        println(s"Number of house: $houseNumberWithLimitedElves")
    }
}
