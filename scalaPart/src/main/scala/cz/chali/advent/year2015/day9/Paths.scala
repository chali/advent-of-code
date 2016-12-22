package cz.chali.advent.year2015.day9

import cz.chali.advent.input.InputReader

case class Connection(point1: String, point2: String, distance: Int)

object Paths {

    def findShortestPath(rawConnections: List[String]): Int = {
        val connections: List[Connection] = rawConnections.map(parseConnection)
        val cities: List[String] = extractCities(connections)
        val distanceMap: Map[Set[String], Int] = connections.map(connection =>
            Set(connection.point1, connection.point2) -> connection.distance).toMap

        //Algorithm currently works only with complete graphs. It would require to put infinity
        //when there is no connection or completely change the way how possible path are created (not use permutation)
        val possiblePaths: Map[List[String], Int] = cities.permutations.map(possiblePath => {
            val pathDistance = possiblePath.sliding(2).foldLeft(0)((distance, points) => distance + distanceMap(points.toSet))
            possiblePath -> pathDistance
        }).toMap
        possiblePaths.values.min
    }

    def parseConnection(connection: String): Connection = {
        val parsedConnections = connection.split(" ").map(_.trim).toList
        parsedConnections match {
            case point1 :: "to" :: point2 :: "=" :: distance :: Nil => Connection(point1, point2, distance.toInt)
        }
    }

    def extractCities(connections: List[Connection]): List[String] = {
        connections.flatMap( connection => List(connection.point1, connection.point2)).distinct
    }

    def main(args: Array[String]) {
        val connections: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day9/paths")
        val pathLength: Int = findShortestPath(connections)
        println(s"Length of the shortest path is: $pathLength")
    }
}
