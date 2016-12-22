package cz.chali.advent.year2015.day14

import cz.chali.advent.input.InputReader

case class Racer(name: String, speed: Int, energy: Int, rest: Int)
case class RaceProgress(distance: Int, points: Int)

object Race {

    val lineParserRegex = """(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".r

    def distanceOfTheWinner(rawRacers: List[String], raceTime: Int): Int = {
        race(rawRacers, raceTime).map(_.distance).max
    }

    def pointsOfTheWinner(rawRacers: List[String], raceTime: Int): Int = {
        race(rawRacers, raceTime).map(_.points).max
    }

    def race(rawRacers: List[String], raceTime: Int): List[RaceProgress] = {
        val racers = rawRacers.map(parse)
        (0 until raceTime).foldLeft(startLine(racers.size))(raceSecond(racers))
    }

    def parse(line: String): Racer = {
        line match {
            case lineParserRegex(racer, speed, energy, rest) => Racer(racer, speed.toInt, energy.toInt, rest.toInt)
        }
    }

    def computeDistance(raceTime: Int)(racer: Racer): Int = {
        val completeRunRestCycles = raceTime / (racer.energy + racer.rest)
        val restOfTheTime = raceTime % (racer.energy + racer.rest)
        (racer.speed * racer.energy * completeRunRestCycles) + (racer.speed * Math.min(restOfTheTime, racer.energy))
    }

    def startLine(size: Int): List[RaceProgress] = List.fill(size)(RaceProgress(0,0))

    def raceSecond(racers: List[Racer])(progresses: List[RaceProgress], currentTime: Int): List[RaceProgress] = {
        val updatedDistances = racers.zip(progresses).collect({
            case (racer, progress) =>
                val positionInRacerCycle = currentTime % (racer.energy + racer.rest)
                val distanceUpdate: Int = if (positionInRacerCycle < racer.energy) racer.speed else 0
                RaceProgress(progress.distance + distanceUpdate, progress.points)
        })
        val maxDistance = updatedDistances.map(_.distance).max
        val updatedPoints = updatedDistances.map(progress =>
            if (progress.distance == maxDistance)
                RaceProgress(progress.distance, progress.points + 1)
            else
                progress
        )
        updatedPoints
    }

    def main(args: Array[String]) {
        val data: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day14/racers")
        val winningDistance: Int = distanceOfTheWinner(data, 2503)
        println(s"Distance traveled by the winner is: $winningDistance")
    }
}
