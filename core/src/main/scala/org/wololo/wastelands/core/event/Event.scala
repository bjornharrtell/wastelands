package org.wololo.wastelands.core.event
import org.wololo.wastelands.core.TileMap
import org.wololo.wastelands.core.Player
import akka.actor.ActorRef
import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.unit.Direction

sealed trait Event

// UI input events
object Touch {
  val DOWN = 0
  val UP = 1
  val MOVE = 2
}
case class Touch(val coordinate: Coordinate, val action: Int) extends Event

// Client/Server remote events
case class Connect() extends Event
case class Connected() extends Event
case class Info() extends Event

// Client/Server local events
case class Tick(ticks: Int) extends Event

// Game/Player remote events
case class Create() extends Event
case class End() extends Event
case class Join(id: Int) extends Event
case class TileMapData(map: TileMap) extends Event
case class Joined() extends Event
case class CreateUnit() extends Event
case class UnitCreated(id: Int, unitType: Int, player: Int, coordinate: Coordinate, direction: Direction) extends Event
object Action {
  val MOVE = 0
  val TURN = 1
  val FIRE = 2
}
case class Action(actionType: Int) extends Event
case class ActionComplete(actionType: Int) extends Event

// Unit client local events
case class Cooldown(actionType: Int) extends Event
case class CooldownComplete(actionType: Int) extends Event