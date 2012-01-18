package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.vmlayer.Sound

abstract class Unit(val map: GameMap, val player: Int, val position: Coordinate) extends Movable with Selectable {
  var isOnScreen = false
  val ScreenBounds: Rect = (0, 0, 0, 0)

  var direction = Direction(0, 0)

  var visible = true
  
  var hp = 20
  var alive = true

  var explode = false
  var exploding = false

  val Range = 1
  val FirePauseTicks = 30
  var readyToFire = true
  
  var fireSound: Sound
  var explodeSound: Sound
  
  def attack(unit: Unit) {
    if (position.distance(unit.position) > Range) {
      // TODO: should track unit position
      moveTo(unit.position)
    } else {
      // TODO: only shoot if ready to fire
      shoot(unit)
    }
  }

  def shoot(unit: Unit) {
    fireSound.play
    unit.takeDamage(10)
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