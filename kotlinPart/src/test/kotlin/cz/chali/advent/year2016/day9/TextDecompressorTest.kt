package cz.chali.advent.year2016.day9

import io.kotlintest.specs.WordSpec

class TextDecompressorTest : WordSpec() {
    init {
        "Decompressor" should {
            "extract original text and provide its length" {
                val text = "ADVENT A(1x5)BC(3x3)XYZA(2x2) BCD(2x2)EFG(6x1)(1x3)AX(8x2)(3x3)ABCY\n"
                val count = TextDecompressor().lengthOfDecompressedText(text, useNestedMarkers = false)
                count shouldBe 57L
            }

            "extract original text and provide its length" {
                val text = "X(8x2) (3x3)ABCY\n"
                val count = TextDecompressor().lengthOfDecompressedText(text, useNestedMarkers = true)
                count shouldBe 20L
            }
        }
    }
}