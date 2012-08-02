package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Coordinate

class UnitState(val player: Int, val position: Coordinate) {
  val Velocity = 0.04
  var moveDistance = 0.0
  var direction: Direction = Direction.fromTileIndex((math.random * 7 + 1).toInt)

  val Range = 2
  val AttackStrength = 2
  var alive = true
  var hp = 10
  
  val id = 0
  
  //var order: Order = new Guard(this)
}