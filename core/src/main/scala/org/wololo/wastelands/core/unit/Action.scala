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
  // NOTE: mutable start to be able to reset after deserialized at clientside
  var start: Int = 0
}

@SerialVersionUID(4016L) case class MoveTileStep(actionType: Int = Action.MoveTileStep) extends Action
@SerialVersionUID(4017L) case class Turn(target: (Int, Int), actionType: Int = Action.Turn) extends Action
@SerialVersionUID(4018L) case class Fire(target: (Int, Int), actionType: Int = Action.Fire) extends Action
@SerialVersionUID(4019L) case class Idle(actionType: Int = Action.Idle) extends Action

