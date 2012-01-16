package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

abstract class Unit(val map: GameMap, val player: Int, val position: Coordinate) extends Movable with Selectable {
  var isOnScreen = false
  val ScreenBounds: Rect = (0,0,0,0)
  
  var direction = Direction(0, 0)
  
  var visible = true
  var hp = 20
  var alive = true
  
  var explode = false
  var exploding = false
  
  var readyToFire = true
  
  def attack(unit: Unit) {
    // TODO: if not in range, just initate a follow move
    
    // TODO: only shoot in range and if ready to fire
    shoot(unit)
  }
  
  def shoot(unit: Unit) {
    unit.takeDamage(10)
  }
  
  def takeDamage(damage: Int) {
    hp -= damage
    
    if (hp<=0) kill
  }
  
  def kill() {
    tile.get.unit = None
    tile = None
    alive = false
    explode = true
  }
}