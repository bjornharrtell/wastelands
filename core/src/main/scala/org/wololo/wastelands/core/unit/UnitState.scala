package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Coordinate
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.event
import akka.actor._
import org.wololo.wastelands.core.GameState
import org.wololo.wastelands.core.Rect
import akka.event.LoggingAdapter
import org.wololo.wastelands.core.GamePlayerState
import scala.collection.immutable.HashMap

trait UnitState {
  val game: GameState

  var unit: ActorRef = null
  val player: ActorRef
  val unitType: Int
  var position: Coordinate
  var direction: Direction
  var alive = true
  var hp = 10

  var order: Order = Guard()
  var action: Action = Idle()
  var cooldowns = HashMap[Int, Cooldown]()

  game.map.tiles(position).unit = Option(this)

  def onOrder(e: event.Order) {
    order = e.order
    /*order match {
      case o: Move => move(o)
      case o: Attack => attack(o)
      case o: Guard => guard(o)
    }*/
  }

  def onAction(e: event.Action) {
    action = e.action
    action.start = game.ticks
    action match {
      case action: MoveTileStep => moveTileStep(action)
      case action: Turn => turn(action)
      case action: Fire => fire(action)
    }
  }

  /*def move(order: Move) {
  }

  def attack(order: Attack) {
  }

  def guard(order: Guard) {
  }*/

  def moveTileStep(action: MoveTileStep) {
    game.map.tiles(position).unit = None
    position = position + direction
    // TODO: only remove shade if the unit belongs to the active player
    game.map.removeShadeAround(position)
    game.map.tiles(position).unit = Option(this)

  }

  def turn(action: Turn) {
    if (action.target != direction) direction = direction.turnTowards(action.target)
  }

  def fire(action: Fire) {
  }

  /**
   * Tick
   *
   * Check if duration has elapsed for actions and cooldowns. Create cooldown if
   * action has elapsed, remove cooldown if cooldown has elapsed.
   */
  def onTick() {
    if (!action.isInstanceOf[Idle] && game.ticks - action.start >= actionLength(action.actionType)) {
      createCooldown(action.actionType)
      action = Idle()
    }

    cooldowns.filter(elapsedCooldownFilter).keys.foreach(removeCooldown(_))
  }

  def elapsedCooldownFilter: PartialFunction[(Int, Cooldown), Boolean] = {
    case (actionType, cooldown) => game.ticks - cooldown.start >= cooldownLength(actionType)
  }

  def createCooldown(actionType: Int) {
    cooldowns = cooldowns + (action.actionType -> Cooldown(game.ticks))
  }

  def removeCooldown(actionType: Int) {
    cooldowns = cooldowns - actionType
  }

  def actionLength(actionType: Int) = actionType match {
    case Action.MoveTileStep => unitType match {
      case Unit.TestUnit1 => 30
      case Unit.TestUnit2 => 50
      case Unit.Harvester => 150
    }
    case Action.Turn => 0
    case Action.Fire => 0
    case Action.Idle => 0
  }

  def cooldownLength(actionType: Int) = actionType match {
    case Action.MoveTileStep => 15
    case Action.Turn => 15
    case Action.Fire => 50
    case Action.Idle => 0
  }
}

class UnitClientState(val player: ActorRef, val game: GamePlayerState, val unitType: Int, var position: Coordinate, var direction: Direction) extends UnitState with Selectable {
  val screenBounds = new Rect(10, 10, 10, 10)
  var isOnScreen = false
  var exploding = false
  var explode = false

  override def fire(action: Fire) {
    game.projectiles = game.projectiles :+ new Projectile(game.ticks, position, action.target)
  }
}
