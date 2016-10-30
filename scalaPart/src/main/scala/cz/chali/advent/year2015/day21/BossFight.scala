package cz.chali.advent.year2015.day21

import cz.chali.advent.year2015.input.InputReader

import scala.util.matching.Regex

case class Item(name: String, price: Int, damage: Int, armor: Int)

case class Player(health: Int, damage: Int, armor: Int)

object BossFight {

    type Price = Int

    val healthRegex = """Hit Points: (\d+)""".r
    val damageRegex = """Damage: (\d+)""".r
    val armorRegex = """Armor: (\d+)""".r

    val weapons = List(
        Item("Dagger", 8, 4, 0),
        Item("Shortsword", 10, 5, 0),
        Item("Warhammer", 25, 6, 0),
        Item("Longsword", 40, 7, 0),
        Item("Greataxe", 74, 8, 0)
    )

    val armors = List(
        Item("No armor", 0, 0, 0),
        Item("Leather", 13, 0, 1),
        Item("Chainmail", 31, 0, 2),
        Item("Splintmail", 53, 0, 3),
        Item("Bandedmail", 75, 0, 4),
        Item("Platemail", 102, 0, 5)
    )

    val rings = List(
        Item("Nothing", 0, 0, 0),
        Item("Nothing 2", 0, 0, 0),
        Item("Damage +1", 25, 1, 0),
        Item("Damage +2", 50, 2, 0),
        Item("Damage +3", 100, 3, 0),
        Item("Defense +1", 20, 0, 1),
        Item("Defense +2", 40, 0, 2),
        Item("Defense +3", 80, 0, 3)
    )

    val playersHealth = 100


    def findLowestGearPriceToWin(rawBoss: List[String]): Price = {
        val boss = parseBoss(rawBoss)
        val itemsFromCheapest = items()
        itemsFromCheapest.map(gearUp).find(fight(boss)).map(_._2).get
    }

    def findHighestGearPriceToLose(rawBoss: List[String]): Price = {
        val boss = parseBoss(rawBoss)
        val itemsFromTheMostExpensive = items().reverse
        itemsFromTheMostExpensive.map(gearUp).find(player => ! fight(boss)(player)).map(_._2).get
    }

    def parseBoss(rawBoss: List[String]): Player = {
        val health = parse(healthRegex, rawBoss(0))
        val damage = parse(damageRegex, rawBoss(1))
        val armor = parse(armorRegex, rawBoss(2))
        Player(health, damage, armor)
    }

    def items(): List[List[Item]] = {
        val itemCombinations: List[List[Item]] = for (
            weapon <- weapons;
            armor <- armors;
            rings <- rings.combinations(2)
        ) yield List(weapon, armor, rings(0), rings(1))
        itemCombinations.sortBy(_.map(_.price).sum)
    }

    def gearUp(items: List[Item]): (Player, Price) = {
        val player = Player(
            playersHealth,
            items.map(_.damage).sum,
            items.map(_.armor).sum
        )
        (player, items.map(_.price).sum)
    }

    def fight(boss: Player)(playerWithGear: (Player, Price)): Boolean = {
        val (player, _) = playerWithGear
        val playerDamage: Int = Math.max(player.damage - boss.armor, 1)
        val bossDamage: Int = Math.max(boss.damage - player.armor, 1)
        val playerRounds = player.health / bossDamage
        val bossRounds = boss.health / playerDamage
        ! (playerRounds < bossRounds)
    }

    private def parse(regex: Regex, text: String): Int = text match {
        case regex(number) => number.toInt
    }

    def main(args: Array[String]) {
        val boss: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day21/boss")
        val lowestPrice: Price = findLowestGearPriceToWin(boss)
        println(s"You need at least x gold to win this fight: $lowestPrice")
        val highestPrice: Price = findHighestGearPriceToLose(boss)
        println(s"You spend at least x gold and still lose this fight: $highestPrice")
    }
}
