package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.event.Event

object Action {
  val MoveTileStep = 0
  val Turn = 1
  val Fire = 2
  val Idle = 3
}

sealed trait Action {
  val actionType: Int
  // NOTE: mutable start since it is reset on the clientside
  var start: Int = 0
  def length(unitType: Int) = actionType match {
    case Action.MoveTileStep => unitType match {
      case UnitTypes.TestUnit1 => 30
      case UnitTypes.TestUnit2 => 50
      case UnitTypes.Harvester => 150
    }
    case Action.Turn => 0
    case Action.Fire => 0
    case Action.Idle => 0
  }
}

@SerialVersionUID(4016L) case class MoveTileStep(actionType: Int = Action.MoveTileStep) extends Action
@SerialVersionUID(4017L) case class Turn(target: (Int, Int), actionType: Int = Action.Turn) extends Action
@SerialVersionUID(4018L) case class Fire(target: (Int, Int), actionType: Int = Action.Fire) extends Action
@SerialVersionUID(4019L) case class Idle(actionType: Int = Action.Idle) extends Action

