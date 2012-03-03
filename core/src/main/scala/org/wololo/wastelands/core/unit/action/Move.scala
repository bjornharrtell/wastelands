package org.wololo.wastelands.core.unit.action
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Unit

class Move(unit: Unit) extends Action(unit) {

  val map = unit.game.map

  var pausing = false
  val MovePauseTicks = 15
  private var movePauseTicksCounter = MovePauseTicks

  map.removeShadeAround(unit.position + unit.direction)

  def onTick() {
    if (pausing)
      pauseTick()
    else if (unit.moveDistance >= 1)
      finish()
    else
      unit.moveDistance += unit.Velocity
  }
  
  private def pauseTick() {
    movePauseTicksCounter -= 1
    if (movePauseTicksCounter == 0) complete()
  }

  private def finish() {
    unit.moveDistance = 0

    // deassociate with previous tile
    map.tiles(unit.position).unit = None

    unit.position += unit.direction

    // associate with new tile
    map.tiles(unit.position).unit = Option(unit)

    pausing = true
  }
}