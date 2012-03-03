package org.wololo.wastelands.core.unit.action

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Direction

class Turn(unit: Unit, target: Direction) extends Action(unit) {

  var firstTick = true
  val PauseTicks = 15
  private var pauseTicksCounter = PauseTicks

  def onTick() {
    if (firstTick)
      turn()
    else
      pauseTick()
  }

  private def turn() {
    unit.direction = unit.direction.turnTowards(target)
    firstTick = false
  }

  private def pauseTick() {
    pauseTicksCounter -= 1
    if (pauseTicksCounter == 0) complete()
  }

}