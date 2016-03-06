package cz.chali.advent.year2015.day15

import cz.chali.advent.year2015.input.InputReader

case class Ingredient(name: String, capacity: Int, durability: Int, flavor: Int, texture: Int, calories: Int)

object CookieRecipe {

    val lineParserRegex =
        """(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)""".r
    val maxSpoons = 100

    def bestRecipeScore(rawIngredients: List[String], caloriesFilter: Function[Int, Boolean] = (calories) => true): Int = {
        val ingredients = rawIngredients.map(parse)
        val possibleIngredientCombinations = ingredients
            .flatMap(ingredient => List.fill(maxSpoons)(ingredient))
            .combinations(maxSpoons)
        val sumsPerProperty: Iterator[(Int, Int, Int, Int, Int)] = possibleIngredientCombinations.map(ingredientsCandidate =>
            ingredientsCandidate.foldLeft((0, 0, 0, 0, 0))((accumulator, ingredient) =>
                (
                    accumulator._1 + ingredient.capacity,
                    accumulator._2 + ingredient.durability,
                    accumulator._3 + ingredient.flavor,
                    accumulator._4 + ingredient.texture,
                    accumulator._5 + ingredient.calories
                    )
            )
        )
        val filteredSumPerProperty = sumsPerProperty.filter(scores => caloriesFilter(scores._5))
        val scorePerCandidate: Iterator[Int] = filteredSumPerProperty.map(candidateSums =>

                Math.max(candidateSums._1, 0) *
                Math.max(candidateSums._2, 0) *
                Math.max(candidateSums._3, 0) *
                Math.max(candidateSums._4, 0)

        )
        scorePerCandidate.max
    }

    def parse(line: String): Ingredient = {
        line match {
            case lineParserRegex(name, capacity, durability, flavor, texture, calories) =>
                Ingredient(name, capacity.toInt, durability.toInt, flavor.toInt, texture.toInt, calories.toInt)
        }
    }

    def main(args: Array[String]) {
        val data: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day15/ingrediences")
        val score: Int = bestRecipeScore(data, calories => calories == 500)
        println(s"The best recipe has score: $score")
    }
}
