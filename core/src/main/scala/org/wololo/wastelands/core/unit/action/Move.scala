package org.wololo.wastelands.core.unit.action
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Unit

class Move(unit: Unit) extends Action(unit) {

  val map = unit.game.map

  var pausing = false
  val PauseTicks = 15
  private var pauseTicksCounter = PauseTicks

  map.removeShadeAround(unit.position + unit.direction)

  def onTick() {
    if (pausing)
      pauseTick()
    else if (unit.moveDistance >= 1)
      moveTileStep()
    else
      unit.moveDistance += unit.Velocity
  }
  
  private def pauseTick() {
    pauseTicksCounter -= 1
    if (pauseTicksCounter == 0) complete()
  }
  
  private def moveTileStep() {
    unit.moveTileStep()
    pausing = true
  }

  
}