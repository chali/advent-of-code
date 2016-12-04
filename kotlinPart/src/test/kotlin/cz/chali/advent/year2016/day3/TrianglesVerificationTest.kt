package cz.chali.advent.year2016.day3

import io.kotlintest.specs.WordSpec

class TrianglesVerificationTest : WordSpec() {
    init {
        "You" should {
            "see those triangles" {
                val possibleTriangles = listOf("  5  10  25", "  10  13  5")
                val validTriangles = TrianglesVerification().numberOfCorrectTriangles(possibleTriangles)
                validTriangles shouldBe 1
            }

            "see those triangles" {
                val possibleTriangles = listOf(
                        " 101 301 501",
                        " 102 302 502",
                        " 103 303 503",
                        " 201 401 601",
                        " 202 402 602",
                        " 203 403 603"
                )
                val validTriangles = TrianglesVerification().numberOfCorrectTrianglesByColumn(possibleTriangles)
                validTriangles shouldBe 6
            }
        }
    }
}