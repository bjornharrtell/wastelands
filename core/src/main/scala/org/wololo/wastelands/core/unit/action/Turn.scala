package org.wololo.wastelands.core.unit.action

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Direction

class Turn(unit: Unit, target: Direction) extends Action(unit: Unit) {

  var firstTick = true
  val TurnPauseTicks = 15
  private var turnPauseTicksCounter = TurnPauseTicks

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
    turnPauseTicksCounter -= 1
    if (turnPauseTicksCounter == 0) complete()
  }

}