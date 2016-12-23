package cz.chali.advent.year2016.day22

import io.kotlintest.specs.WordSpec


class GridComputingTest : WordSpec() {
    init {
        "You" should {
            "count expected number of viable pairs" {
                val nodes = listOf(
                        "root@ebhq-gridcenter# df -h",
                        "Filesystem              Size  Used  Avail  Use%",
                        "/dev/grid/node-x0-y0     88T   66T    22T   75%",
                        "/dev/grid/node-x0-y1     85T   20T    65T   24%"
                )
                val count = GridComputing().countViablePairs(nodes)
                count shouldBe 1
            }

            "find minimum steps to move data from one corner to another" {
                val nodes = listOf(
                        "root@ebhq-gridcenter# df -h",
                        "Filesystem              Size  Used  Avail  Use%",
                        "/dev/grid/node-x0-y0   10T    8T     2T   80%",
                        "/dev/grid/node-x0-y1   11T    6T     5T   54%",
                        "/dev/grid/node-x0-y2   32T   28T     4T   87%",
                        "/dev/grid/node-x1-y0    9T    7T     2T   77%",
                        "/dev/grid/node-x1-y1    8T    0T     8T    0%",
                        "/dev/grid/node-x1-y2   11T    7T     4T   63%",
                        "/dev/grid/node-x2-y0   10T    6T     4T   60%",
                        "/dev/grid/node-x2-y1    9T    8T     1T   88%",
                        "/dev/grid/node-x2-y2    9T    6T     3T   66%"
                )
                val steps = GridComputing().findMinimumSteps(nodes)
                steps shouldBe 7
            }
        }
    }
}