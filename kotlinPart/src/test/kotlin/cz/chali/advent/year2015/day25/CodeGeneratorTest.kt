package cz.chali.advent.year2015.day25

import io.kotlintest.specs.WordSpec

class CodeGeneratorTest : WordSpec() {
    init {
        "Generator" should {
            "generate code for 6th row and 6th column" {
                val code = CodeGenerator().generate(column = 6, row = 6)
                code shouldBe 27995004L
            }
        }
    }
}