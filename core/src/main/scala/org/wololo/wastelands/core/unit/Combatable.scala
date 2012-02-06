package org.wololo.wastelands.core.unit
import org.wololo.wastelands.vmlayer.Sound

object Combatable {
  val AttackStatusPassive = 0
  val AttackStatusReloading = 1
  val AttackStatusReadyToFire = 2
}

/**
 * Logic to make a Unit able to attack and get killed
 * 
 * Default state is Passive.
 * 
 * When Passive state unit will auto move toward target unit until in range then change into ReadyToFire state.
 * When ReadyToFire the unit will proceed to either:
 *   Passive if target unit is dead or out of range.
 *   Reloading after shooting target.
 * When Reloading the unit will proceed to ReadyToFire after tick countdown.
 */
trait Combatable extends Tickable {
  self: Unit =>

  import Combatable._

  def fireSound: Sound
  def explodeSound: Sound

  var alive = true
  var hp = 10

  var explode = false
  var exploding = false

  var hasFired = false

  val Range = 2
  val FirePauseTicks = 120
  private var firePauseTicksCounter = FirePauseTicks
  var attackStatus = AttackStatusPassive
  var unitToAttack: Option[Unit] = None

  override def tick(): Unit = {
    attackStatus match {
      case AttackStatusReadyToFire =>
        if (isWithinRange && unitToAttack.get.alive) {
          shoot(unitToAttack.get)
        } else {
          attackStatus = AttackStatusPassive
          if (!unitToAttack.get.alive) unitToAttack = None
        }
      case AttackStatusReloading =>
        firePauseTicksCounter -= 1

        if (firePauseTicksCounter == 0) {
          attackStatus = AttackStatusReadyToFire
          firePauseTicksCounter = FirePauseTicks
        }
      case AttackStatusPassive if unitToAttack.isDefined =>
        if (isWithinRange) {
          //moveTo(position)
          attackStatus = AttackStatusReadyToFire
        } else {
          moveTo(unitToAttack.get.position)
        }
      case AttackStatusPassive =>
    }

    super.tick()
  }

  def isWithinRange: Boolean = {
    position.distance(unitToAttack.get.position) <= Range
  }

  def attack(unit: Unit with Combatable) {
    unitToAttack = Option(unit)
  }

  def isAttacking: Boolean = {
    unitToAttack != None
  }

  def abortAttack() {
    attackStatus = AttackStatusPassive
    unitToAttack = None
  }

  def shoot(unit: Unit with Combatable) {
    fireSound.play()

    game.projectiles += new Projectile(game, this, unit)

    unit.takeDamage(2)

    if (unit.alive) {
      attackStatus = AttackStatusReloading
    } else {
      abortAttack()
    }
  }

  def takeDamage(damage: Int) {
    hp -= damage

    if (hp <= 0) kill()
  }

  def kill() {
    tile.unit = None
    if (!explode && !exploding) {
      explode = true
      explodeSound.play()
    }
  }
}