package org.wololo.wastelands.core.unit.action

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Direction

class Turn(unit: Unit, target: Direction) extends Action(unit) {

  val PauseTicks = 10
  private var pauseTicksCounter = PauseTicks

  def onTick() {
    if (pauseTicksCounter == 0) turn()
    pauseTicksCounter -= 1
  }

  private def turn() {
    unit.direction = unit.direction.turnTowards(target)
    complete()
  }

}