package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.event.Event

object Action {
  val Move = 0
  val Turn = 1
  val Fire = 2
}

class Action(val actionType: Int, val length: Int)