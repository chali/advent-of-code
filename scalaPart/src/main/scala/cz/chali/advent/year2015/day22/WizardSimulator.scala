package cz.chali.advent.year2015.day22

import cz.chali.advent.input.InputReader

import scala.collection.mutable
import scala.util.matching.Regex

object FighterType extends Enumeration {
    val Player, Boss = Value
}

//represents state of fighters in the game
case class Fighter(fighterType: FighterType.Value,
                   health: Int,
                   armor: Int,
                   mana: Int,
                   usedMana: Int,
                   availableActions: List[Action],
                   activeSpells: Map[TimedSpell, Int] = Map()) {

    val endingSpellPredicate: PartialFunction[(Spell, Int), Boolean] = { case (_, turns: Int) => turns == 0 }

    def usableActions: List[Action] = availableActions.filter(_.usable(this))

    def applyEffects(initialOpponent: Fighter): (Fighter, Fighter) = {
        activeSpells.keys.foldLeft((this, initialOpponent))((players, activeSpell) =>
            players match {
                case (caster, opponent) => activeSpell.everyRound(caster, opponent)
            }
        )
    }

    def endSpells(): Fighter = {
        val ending = activeSpells.filter(endingSpellPredicate)
        ending.keys.foldLeft(this)((fighter, endingSpell) => endingSpell.timerExhausted(fighter))
    }
}

//represents state of the whole game
case class Game(attacker: Fighter, defender: Fighter) {

    //players switch role in the next turn
    def nextTurn: Game = this.copy(attacker = defender, defender = attacker)

    def winner: Option[Fighter] = {
        if (attacker.health <= 0)
            Some(defender)
        else if (defender.health <= 0)
            Some(attacker)
        else
            None
    }

    def hasWinner = winner.isDefined

    def whichFighterWon(fighterType: FighterType.Value): Boolean = {
        winner.exists(_.fighterType == fighterType)
    }

    def applyEffects(): Game = {
        if (hasWinner)
            this
        else {
            val (intermediateAttacker, intermediateDefender) = attacker.applyEffects(defender)
            val (newDefender, newAttacker) = intermediateDefender.applyEffects(intermediateAttacker)
            this.copy(attacker = newAttacker, defender = newDefender)
        }
    }

    def doAction(action: Action): Game = {
        if (hasWinner)
            this
        else
            action.doAction(this)
    }

    def endSpells(): Game = {
        this.copy(attacker = attacker.endSpells(), defender = defender.endSpells())
    }

}

abstract class Action {
    def usable(user: Fighter): Boolean
    def doAction(game: Game): Game
}

case class Attack(damage: Int) extends Action {
    override def usable(user: Fighter) = true
    override def doAction(game: Game): Game = {
        val defender = game.defender
        val currentDamage = Math.max(damage - defender.armor, 1)
        game.copy(
            defender = defender.copy(health = defender.health - currentDamage)
        )
    }
}

abstract class Spell(mana: Int) extends Action {
    override def usable(user: Fighter): Boolean = user.mana >= mana

    final override def doAction(game: Game): Game = {
        cast(payMana(game))
    }

    private def payMana(game: Game): Game = {
        val attacker = game.attacker
        game.copy(
            attacker = attacker.copy(
                mana = attacker.mana - mana,
                usedMana = attacker.usedMana + mana
            )
        )
    }

    def cast(game: Game): Game
}

case class MagicMissile(damage: Int = 4, mana: Int = 53) extends Spell(mana) {
    def cast(game: Game): Game = {
        val defender = game.defender
        game.copy(
            defender = defender.copy(health = defender.health - damage)
        )
    }
}
case class Drain(damage: Int = 2, mana: Int = 73) extends Spell(mana) {
    override def cast(game: Game): Game = {
        val attacker = game.attacker
        val defender = game.defender
        game.copy(
            attacker = attacker.copy(health = attacker.health + damage),
            defender = defender.copy(health = defender.health - damage)
        )
    }
}

abstract class TimedSpell(turns: Int, mana: Int) extends Spell(mana) {

    override def cast(game: Game): Game = {
        beginningEffect(activate(game))
    }

    def activate(game: Game): Game = {
        val attacker = game.attacker
        game.copy(
            attacker = attacker.copy(
                availableActions = attacker.availableActions.filterNot(_ == this),
                activeSpells = attacker.activeSpells + (this -> turns)
            )
        )
    }

    def beginningEffect(game: Game): Game = game

    def everyRound(attacker: Fighter, defender: Fighter): (Fighter, Fighter) = {
        val updatedActiveSpells = attacker.activeSpells.updated(this, attacker.activeSpells(this) - 1)
        everyRoundEffect(attacker.copy(activeSpells = updatedActiveSpells), defender)
    }

    def everyRoundEffect(attacker: Fighter, defender: Fighter): (Fighter, Fighter) = (attacker, defender)

    def timerExhausted(caster: Fighter): Fighter = {
        deactivate(endEffect(caster))
    }

    def deactivate(caster: Fighter): Fighter = {
        caster.copy(
            activeSpells = caster.activeSpells.filterKeys(_ != this),
            availableActions = this :: caster.availableActions
        )
    }

    def endEffect(caster: Fighter): Fighter = caster
}

case class Shield(armor: Int = 7, turns: Int = 6, mana: Int = 113) extends TimedSpell(turns, mana) {
    override def beginningEffect(game: Game): Game = {
        val attacker = game.attacker
        game.copy(attacker = attacker.copy(armor = attacker.armor + armor))
    }

    override def endEffect(caster: Fighter): Fighter = {
        caster.copy(armor = caster.armor - armor)
    }
}

case class Poison(damage: Int = 3, turns: Int = 6, mana: Int = 173) extends TimedSpell(turns, mana) {
    override def everyRoundEffect(attacker: Fighter, defender: Fighter): (Fighter, Fighter) = {
        (attacker, defender.copy(health = defender.health - damage))
    }
}

case class Recharge(manaRecharge: Int = 101, turns: Int = 5, mana: Int = 229) extends TimedSpell(turns, mana) {
    override def everyRoundEffect(attacker: Fighter, defender: Fighter): (Fighter, Fighter) = {
        (attacker.copy(mana = attacker.mana + manaRecharge), defender)
    }
}

object WizardSimulator {

    val healthRegex = """Hit Points: (\d+)""".r
    val damageRegex = """Damage: (\d+)""".r

    val game = mutable.PriorityQueue[Game]()(Ordering.by[Game, Int](_.attacker.usedMana).reverse)

    def leastAmountOfManaToWin(rawBoss: List[String], initialHealth: Int, initialMana: Int): Int = {
        val player: Fighter = preparePlayer(initialHealth, initialMana)
        val boss: Fighter = parseBoss(rawBoss)
        var currentState = Game(player, boss)
        while (! currentState.whichFighterWon(FighterType.Player)) {
            //version for hard mode
            /*
            val currentStateAfterEffects = currentState.copy(
                attacker = currentState.attacker.copy(health = currentState.attacker.health - 1)
            ).applyEffects().endSpells()
            */
            val currentStateAfterEffects = currentState.applyEffects().endSpells()
            val possibleNotDangerousStates: List[Game] = currentStateAfterEffects.attacker.usableActions
                .flatMap(attackerAction => {
                    val firstAttackerActionFinished = currentStateAfterEffects.doAction(attackerAction).nextTurn
                    val secondAttackerAfterEffects = firstAttackerActionFinished.applyEffects().endSpells()
                    secondAttackerAfterEffects.attacker.usableActions
                        .map(secondAttackerAction => secondAttackerAfterEffects.doAction(secondAttackerAction).nextTurn)
                })
            game.enqueue(possibleNotDangerousStates.filterNot(_.whichFighterWon(FighterType.Boss)):_*)
            currentState = game.dequeue()
        }
        currentState.winner.map(_.usedMana).get
    }

    def preparePlayer(initialHealth: Int, initialMana: Int): Fighter = {
        Fighter(FighterType.Player, initialHealth, 0, initialMana, 0, List(MagicMissile(),
            Drain(), Shield(), Poison(), Recharge()), Map())
    }

    def parseBoss(rawBoss: List[String]): Fighter = {
        val health = parse(healthRegex, rawBoss.head)
        val damage = parse(damageRegex, rawBoss(1))
        Fighter(FighterType.Boss, health, 0, 0, 0,List(Attack(damage)))
    }

    private def parse(regex: Regex, text: String): Int = text match {
        case regex(number) => number.toInt
    }

    def main(args: Array[String]) {
        val boss: List[String] = InputReader.readLines("/cz/chali/advent/year2015/day22/boss")
        val mana: Int = leastAmountOfManaToWin(boss, 50, 500)
        println(s"You need at least x mana to win this fight: $mana")
    }
}
