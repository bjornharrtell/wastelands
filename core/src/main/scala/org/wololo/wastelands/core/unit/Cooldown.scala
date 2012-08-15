package org.wololo.wastelands.core.unit

class Cooldown(val action: Action, val start: Int) {
  def length = action.actionType match {
    case Action.MoveTileStep => 15
    case Action.Turn => 15
    case Action.Fire => 50
    case Action.Idle => 0
  }
}