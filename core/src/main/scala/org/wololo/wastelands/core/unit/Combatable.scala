package org.wololo.wastelands.core.unit
import org.wololo.wastelands.vmlayer.Sound

trait Combatable extends Tickable {
  self: Unit =>

  var hp = 10

  var explode = false
  var exploding = false

  val Range = 2
  val FirePauseTicks = 120
  private var firePauseTicksCounter = FirePauseTicks
  var attackStatus = AttackStatusPassive
  var unitToAttack: Option[Unit with Combatable] = None

  var fireSound: Sound
  var explodeSound: Sound

  val AttackStatusPassive = 0
  val AttackStatusReloading = 1
  val AttackStatusReadyToFire = 2

  override def tick() {
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
          moveTo(position)
          attackStatus = AttackStatusReadyToFire
        } else {
          moveTo(unitToAttack.get.position)
        }
      case AttackStatusPassive =>
    }

    super.tick()
  }

  def isWithinRange(): Boolean = {
    position.distance(unitToAttack.get.position) <= Range
  }

  def attack(unit: Unit with Combatable) {
    unitToAttack = Option(unit)
  }

  def shoot(unit: Unit with Combatable) {
    fireSound.play
    unit.takeDamage(2)

    if (unit.alive) {
      attackStatus = AttackStatusReloading
    } else {
      attackStatus = AttackStatusPassive
      unitToAttack = None
    }
  }

  def takeDamage(damage: Int) {
    hp -= damage

    if (hp <= 0) kill
  }

  def kill() {
    tile.get.unit = None
    tile = None
    alive = false
    explode = true
    explodeSound.play
  }
}