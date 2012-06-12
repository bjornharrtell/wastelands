package org.wololo.wastelands.core.unit.action
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Order

class MoveStep(order: Order, unit: Unit) extends Action(order, unit) {

  override val CooldownTicks = 30
  
  val Type = 0
  
  override def execute() {
	  super.execute();
	  unit.game.map.removeShadeAround(unit.position + unit.direction)
  }
  
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