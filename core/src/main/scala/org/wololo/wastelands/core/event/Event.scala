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
case class Touch(val coordinate: Coordinate, val action: Int) extends Event

// Client/Server local events
case class Tick() extends Event

// Client/Player initiated events
case class Connect() extends Event
case class Disconnect() extends Event
case class Create(name: String) extends Event
case class Join() extends Event

// Server initiated events
case class Connected() extends Event
case class Info() extends Event

// Game initiated events
case class Created(game: ActorRef) extends Event
case class Joined(player: ActorRef) extends Event
case class TileMapData(map: TileMap) extends Event
case class End() extends Event

case class CreateUnit(unitType: Int, position: Coordinate, direction: Direction) extends Event
case class UnitCreated(unit: ActorRef, player: ActorRef, unitType: Int, position: Coordinate, direction: Direction) extends Event

// Unit order events
case class Move(destination: Coordinate) extends Event
case class Attack(target: ActorRef) extends Event
case class Guard() extends Event

// Unit action events
case class MoveTileStep() extends Event
case class Turn(target: Direction) extends Event
case class Fire() extends Event
case class ActionComplete(actionType: Int) extends Event
case class Cooldown(actionType: Int) extends Event
case class CooldownComplete(actionType: Int) extends Event