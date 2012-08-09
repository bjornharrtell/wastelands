package org.wololo.wastelands.core.event
import org.wololo.wastelands.core.TileMap
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
@SerialVersionUID(1001L) case class Touch(val coordinate: Coordinate, val action: Int) extends Event

// Client/Server local events
@SerialVersionUID(1002L) case class Tick() extends Event
@SerialVersionUID(1003L) case class Render() extends Event

// Client/Player initiated events
@SerialVersionUID(1004L) case class Connect() extends Event
@SerialVersionUID(1005L) case class Disconnect() extends Event
@SerialVersionUID(1006L) case class Create(name: String) extends Event
@SerialVersionUID(1007L) case class Join() extends Event

// Server initiated events
@SerialVersionUID(1008L) case class Connected() extends Event
@SerialVersionUID(1009L) case class Info() extends Event

// Game initiated events
@SerialVersionUID(1010L) case class Created(game: ActorRef) extends Event
@SerialVersionUID(1011L) case class Joined(player: ActorRef) extends Event
@SerialVersionUID(1012L) case class TileMapData(map: TileMap) extends Event
@SerialVersionUID(1013L) case class End() extends Event

@SerialVersionUID(1014L) case class CreateUnit(unitType: Int, position: Coordinate, direction: Direction) extends Event
@SerialVersionUID(1015L) case class UnitCreated(unit: ActorRef, player: ActorRef, unitType: Int, position: Coordinate, direction: Direction) extends Event

// Unit order events
@SerialVersionUID(1016L) case class Move(destination: Coordinate) extends Event
@SerialVersionUID(1017L) case class Attack(target: ActorRef) extends Event
@SerialVersionUID(1018L) case class Guard() extends Event

// Unit action events
@SerialVersionUID(1019L) case class MoveTileStep() extends Event
@SerialVersionUID(1020L) case class Turn(target: Direction) extends Event
@SerialVersionUID(1021L) case class Fire() extends Event
@SerialVersionUID(1022L) case class ActionComplete(actionType: Int) extends Event
@SerialVersionUID(1023L) case class Cooldown(actionType: Int) extends Event
@SerialVersionUID(1024L) case class CooldownComplete(actionType: Int) extends Event