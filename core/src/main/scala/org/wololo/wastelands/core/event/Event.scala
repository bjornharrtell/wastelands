package org.wololo.wastelands.core.event
import org.wololo.wastelands.core.TileMap
import akka.actor.ActorRef
import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.unit.Direction

sealed trait Event

// UI input events
object Touch {
  val DOWN = 0
  val UP = 1
  val MOVE = 2
}
@SerialVersionUID(1001L) case class Touch(val coordinate: Coordinate, val action: Int) extends Event

// Client/Server local events
@SerialVersionUID(1002L) case class Tick() extends Event
@SerialVersionUID(1003L) case class Render() extends Event

// Client initiated events
@SerialVersionUID(1004L) case class Connect() extends Event
@SerialVersionUID(1005L) case class Disconnect() extends Event
@SerialVersionUID(1006L) case class CreateGame(name: String) extends Event

// Server initiated events
@SerialVersionUID(1008L) case class Connected() extends Event
@SerialVersionUID(1009L) case class Info() extends Event
@SerialVersionUID(1010L) case class GameCreated(game: ActorRef) extends Event

// Player initiated events
@SerialVersionUID(1007L) case class Join() extends Event
@SerialVersionUID(1014L) case class CreateUnit(unitType: Int, position: Coordinate, direction: Direction) extends Event

// Game initiated events
@SerialVersionUID(1011L) case class Joined(player: ActorRef) extends Event
@SerialVersionUID(1015L) case class UnitCreated(unit: ActorRef, player: ActorRef, unitType: Int, position: Coordinate, direction: Direction) extends Event
@SerialVersionUID(1012L) case class TileMapData(map: TileMap) extends Event

// Unit events
@SerialVersionUID(1016L) case class Order(order: unit.Order) extends Event
@SerialVersionUID(1019L) case class Action(action: unit.Action) extends Event