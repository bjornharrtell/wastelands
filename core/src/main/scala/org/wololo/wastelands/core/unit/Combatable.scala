package org.wololo.wastelands.core.unit
import org.wololo.wastelands.vmlayer.Sound

object CombatStatus {
  val Passive = 0
  val Reloading = 1
  val ReadyToFire = 2
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
 * 
 * @deprecated in favor of order/action stuff
 */
trait Combatable extends Tickable {
  self: Unit =>

  import CombatStatus._

  def fireSound: Sound
  def explodeSound: Sound



  var hasFired = false
  
  val FirePauseTicks = 120
  private var firePauseTicksCounter = FirePauseTicks
  var attackStatus = Passive
  var unitToAttack: Option[Unit] = None

  override def tick(): Unit = {
    attackStatus match {
      case ReadyToFire =>
        if (isWithinRange && unitToAttack.get.alive) {
          shoot(unitToAttack.get)
        } else {
          attackStatus = Passive
          if (!unitToAttack.get.alive) unitToAttack = None
        }
      case Reloading =>
        firePauseTicksCounter -= 1

        if (firePauseTicksCounter == 0) {
          attackStatus = ReadyToFire
          firePauseTicksCounter = FirePauseTicks
        }
      case Passive if unitToAttack.isDefined =>
        if (isWithinRange) {
          //moveTo(position)
          attackStatus = ReadyToFire
        } else {
          //moveTo(unitToAttack.get.position)
        }
      case Passive =>
    }

    super.tick
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
    attackStatus = Passive
    unitToAttack = None
  }

  def shoot(unit: Unit) {
    fireSound.play()

    game.projectiles += new Projectile(game, this, unit)

    //unit.takeDamage(2)

    if (unit.alive) {
      attackStatus = Reloading
    } else {
      abortAttack()
    }
  }

  def takeDamage(damage: Int) {
    hp -= damage

    if (hp <= 0) kill()
  }

  def kill() {
    //tile.unit = None
    if (!explode && !exploding) {
      explode = true
      explodeSound.play()
    }
  }
}