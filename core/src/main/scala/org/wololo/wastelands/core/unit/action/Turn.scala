package org.wololo.wastelands.core.unit.action

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Direction

class Turn(unit: Unit, target: Direction) extends Action(unit) {

  def onTick() {
    turn()
  }

  private def turn() {
    unit.direction = unit.direction.turnTowards(target)
    complete()
  }

}