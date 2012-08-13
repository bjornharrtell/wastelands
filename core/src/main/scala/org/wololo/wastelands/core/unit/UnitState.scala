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
  
  def getActionLength(action: Action) : Int = action match {
    case a: MoveTileStep => unitType match {
      case UnitTypes.TestUnit1 => 30
      case UnitTypes.TestUnit2 => 50
      case UnitTypes.Harvester => 150
    }
    case a: Turn => 0
    case a: Fire => 0
  }
  
  def getCooldownLength(cooldown: Cooldown) : Int = cooldown.action match {
    case a: MoveTileStep => 15
    case a: Turn => 15
    case a: Fire => 50
  }

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
    if (!action.isInstanceOf[Idle] && gameState.ticks - action.start >= getActionLength(action)) {
      cooldowns += new Cooldown(action, gameState.ticks)
      action = Idle()
    }

    val cooldownsToRemove = cooldowns.filter(cooldown => gameState.ticks - cooldown.start >= getCooldownLength(cooldown))
    cooldowns --= cooldownsToRemove
    cooldownsToRemove.length > 0
  }

}

class UnitClientState(val self: ActorRef, val player: ActorRef, val gameState: GameState, val unitType: Int, var position: Coordinate, var direction: Direction) extends UnitState with Selectable {
  val screenBounds = new Rect(10, 10, 10, 10)
  var isOnScreen = false
  var exploding = false
  var explode = false
}
