package cz.chali.advent.year2015.day9

import org.scalatest.WordSpec

class PathsTest extends WordSpec {

    "Santa" should {

        "use the shortest path with length 605" in {
            val connections = List(
                "London to Dublin = 464",
                "London to Belfast = 518",
                "Dublin to Belfast = 141"
            )
            assert(Paths.findShortestPath(connections) == 605)
        }

    }
}
