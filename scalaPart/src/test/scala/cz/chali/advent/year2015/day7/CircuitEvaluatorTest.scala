package cz.chali.advent.year2015.day7

import org.scalatest.WordSpec

class CircuitEvaluatorTest extends WordSpec {

    "Circuit" should {

        "have output given output" in {
            val description = List(
                "123 -> x",
                "456 -> y",
                "x AND y -> d",
                "x OR y -> e",
                "x LSHIFT 2 -> f",
                "y RSHIFT 2 -> g",
                "NOT x -> h",
                "NOT y -> i")

            val output = CircuitEvaluator.evaluate(description)

            assert(output("x") == 123)
            assert(output("y") == 456)
            assert(output("d") == 72)
            assert(output("f") == 492)
            assert(output("g") == 114)
            assert(output("h") == 65412)
            assert(output("i") == 65079)
        }

    }
}
