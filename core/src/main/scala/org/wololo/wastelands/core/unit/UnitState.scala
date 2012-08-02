package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Coordinate

trait UnitState {
  val player: Int
  val position: Coordinate
  var moveDistance = 0.0
  var direction: Direction
  var alive = true
  var hp = 10
}

class UnitClientState extends UnitState {
  // TODO: add stuff relevant for clientside, like screen bbox etc.
}
