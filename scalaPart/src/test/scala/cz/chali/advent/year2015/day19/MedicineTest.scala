package cz.chali.advent.year2015.day19

import org.scalatest.WordSpec

class MedicineTest extends WordSpec {

    "Santa" should {

        "find those molecules" in {
            val input = "HOH"
            val formulas = List(
                "H => HO",
                "H => OH",
                "O => HH"
            )
            assert(Medicine.findMolecules(formulas, input) == Set("HOOH", "HOHO", "OHOH", "HHHH"))
        }

        "find number of transformation" in {
            val formulas = List(
                "e => H",
                "e => O",
                "H => HO",
                "H => OH",
                "O => HH"
            )
            assert(Medicine.howManyStepsIsNeededTo(formulas, "e", "HOH") == 3)
            assert(Medicine.howManyStepsIsNeededTo(formulas, "e", "HOHOHO") == 6)
        }

    }
}
