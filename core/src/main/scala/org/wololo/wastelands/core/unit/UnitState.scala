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

  var order: Order = Guard()
  // TODO: consider idle action to get rid of optional stuff...
  var action: Option[Action] = None
  var cooldowns = ArrayBuffer[Cooldown]()

  def mutate: PartialFunction[Event, scala.Unit] = {
    case e: event.Order =>
      this.order = e.order
      e.order match {
        case o: Move => move(o)
        case o: Attack => attack(o)
        case o: Guard => guard(o)
      }
    case e: event.Action =>
      e.action.start = gameState.ticks
      this.action = Option(e.action)
      e.action match {
        case a: MoveTileStep => moveTileStep(a)
        case a: Turn => turn(a)
        case a: Fire => fire(a)
      }
    case e: event.Cooldown => //cooldowns += new Cooldown(e.action, gameState.ticks)
    // TODO: check if case class comparison is ok here...
    case e: event.CooldownComplete => //cooldowns -- cooldowns.filter(_.action == e.action)
    case e: event.ActionComplete =>
      // TODO: set order to none (guard?) if its goal is complete
      //action = None
    case e: event.Tick =>
  }

  private def move(order: Move) {
  }

  private def attack(order: Attack) {
  }

  private def guard(order: Guard) {
  }

  private def moveTileStep(action: MoveTileStep) {
    position = position + direction
    // TODO: only remove shade if the unit belongs to the active player
    gameState.map.removeShadeAround(position)
  }

  private def turn(action: Turn) {
    direction = direction.turnTowards(action.target)
  }

  private def fire(action: Fire) {
  }

}

class UnitClientState(val self: ActorRef, val player: ActorRef, val gameState: GameState, val unitType: Int, var position: Coordinate, var direction: Direction) extends UnitState with Selectable {
  // TODO: add stuff relevant for clientside, like screen bbox etc.
  val screenBounds = new Rect(10, 10, 10, 10)
  var isOnScreen = false
  var exploding = false
  var explode = false
}
