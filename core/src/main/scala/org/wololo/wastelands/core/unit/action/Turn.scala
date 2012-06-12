package org.wololo.wastelands.core.unit.action

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Direction
import org.wololo.wastelands.core.unit.Order

class Turn(order: Order, unit: Unit, target: Direction) extends Action(order, unit) {

  override val CooldownTicks = 20
  
  val Type = 1
  
  def onTick() {
    turn()
  }

  private def turn() {
    unit.direction = unit.direction.turnTowards(target)
    complete()
  }

}