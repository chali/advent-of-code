package cz.chali.advent.year2015.day15

import org.scalatest.WordSpec

class CookieRecipeTest extends WordSpec {

    "Cookie" should {

        "from the best recipe should have score" in {
            val ingredients = List(
                "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
                "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"
            )
            assert(CookieRecipe.bestRecipeScore(ingredients) == 62842880)
        }

        "from the best recipe with 500 calories should have score" in {
            val ingredients = List(
                "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
                "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"
            )
            assert(CookieRecipe.bestRecipeScore(ingredients, calories => calories == 500) == 57600000)
        }

    }
}
