package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.event.Event

sealed trait Action {
  var start:Int = 0
}

@SerialVersionUID(4016L) case class MoveTileStep() extends Action
@SerialVersionUID(4017L) case class Turn(target: Direction) extends Action
@SerialVersionUID(4018L) case class Fire() extends Action