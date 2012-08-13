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
  var action: Action = Idle()
  var cooldowns = ArrayBuffer[Cooldown]()

  def order(e: event.Order) {
    order = e.order
    order match {
      case o: Move => move(o)
      case o: Attack => attack(o)
      case o: Guard => guard(o)
    }
  }

  def action(e: event.Action) {
    action = e.action
    action.start = gameState.ticks
    action match {
      case a: MoveTileStep => moveTileStep(a)
      case a: Turn => turn(a)
      case a: Fire => fire(a)
    }
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

  /**
   * Tick
   *
   * Check if duration has elapsed for actions and cooldowns. Create cooldown if
   * action has elapsed, remove cooldown if cooldown has elapsed.
   */
  def tick() = {
    // TODO: action length from unittype
    if (!action.isInstanceOf[Idle] && gameState.ticks - action.start >= 50) {
      cooldowns += new Cooldown(action, gameState.ticks)
      action = Idle()
    }

    // TODO: cooldown length from unittype
    val cooldownsToRemove = cooldowns.filter(gameState.ticks - _.start >= 30)
    cooldowns --= cooldownsToRemove
    cooldownsToRemove.length > 0
  }

}

class UnitClientState(val self: ActorRef, val player: ActorRef, val gameState: GameState, val unitType: Int, var position: Coordinate, var direction: Direction) extends UnitState with Selectable {
  // TODO: add stuff relevant for clientside, like screen bbox etc.
  val screenBounds = new Rect(10, 10, 10, 10)
  var isOnScreen = false
  var exploding = false
  var explode = false
}
