package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Coordinate
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.event
import akka.actor._
import org.wololo.wastelands.core.GameState
import org.wololo.wastelands.core.Rect
import akka.event.LoggingAdapter

trait UnitState {
  val gameState: GameState
  
  val self: ActorRef
  val player: ActorRef
  val unitType: Int
  var position: Coordinate
  var direction: Direction
  var alive = true
  var hp = 10

  var order = Order.Guard
  var target: Option[ActorRef] = None
  var destination: Option[Coordinate] = None
  var action: Option[Int] = None
  var actionStart: Int = 0
  var actionLength: Int = 0
  var cooldowns = ArrayBuffer[Cooldown]()

  def mutate: PartialFunction[Event, scala.Unit] = {
      case e: event.Move =>
        move(e.destination)
      case e: event.Attack =>
        attack(e.target)
      case e: event.Guard =>
        guard()
      case e: event.MoveTileStep =>
        position = position + direction
        // TODO: only remove shade if the unit belongs to the active player
        gameState.map.removeShadeAround(position)
        action = Option(Action.Move)
        actionStart = gameState.ticks
      	actionLength = 50
      case e: event.Turn =>
      	direction = direction.turnTowards(e.target)
      	action = Option(Action.Turn)
      	actionStart = gameState.ticks
      	actionLength = 0
      case e: event.Cooldown =>
        // TODO: get duration from action type
        cooldowns += new Cooldown(e.actionType, gameState.ticks, 30)
      case e: event.CooldownComplete =>
        // TODO: cleanup cooldowns...
      case e: event.ActionComplete =>
        // TODO: set order to none (guard?) if its goal is complete
        action = None
      case e: event.Tick =>
  }

  private def move(destination: Coordinate) {
    order = Order.Move
    this.destination = Option(destination)
  }

  private def attack(target: ActorRef) {
    order = Order.Attack
    this.target = Option(target)
  }

  private def guard() {
    order = Order.Guard
  }

}

class UnitClientState(val self: ActorRef, val player: ActorRef, val gameState: GameState, val unitType: Int, var position: Coordinate, var direction: Direction) extends UnitState with Selectable {
  // TODO: add stuff relevant for clientside, like screen bbox etc.
  val screenBounds = new Rect(10, 10, 10, 10)
  var isOnScreen = false
  var exploding = false
  var explode = false
}
