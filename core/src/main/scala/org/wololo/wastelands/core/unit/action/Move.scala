package org.wololo.wastelands.core.unit.action
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Unit

class Move(unit: Unit) extends Action(unit) {

  val map = unit.game.map
  
  map.removeShadeAround(unit.position + unit.direction)

  def onTick() {
    if (unit.moveDistance >= 1)
      moveTileStep()
    else
      unit.moveDistance += unit.Velocity
  }
  
  private def moveTileStep() {
    unit.moveTileStep()
    complete()
  }

}