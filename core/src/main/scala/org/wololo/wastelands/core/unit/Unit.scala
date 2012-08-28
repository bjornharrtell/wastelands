package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.event.Event
import akka.actor._
import akka.pattern.{ ask, pipe }
import akka.util.duration._
import com.typesafe.config.ConfigFactory
import akka.util.Timeout
import akka.dispatch.Await

object Unit {
  val TestUnit1 = 0
  val TestUnit2 = 1
  val Harvester = 2
}

case object Alive
case object Dead
case object Locate
case class Damage(hp: Int)
case class UnitSpotted(unitState: UnitState)

abstract class Unit(val player: ActorRef, val game: GameState, var position: Coordinate, var direction: Direction) extends Actor with UnitState {
  val Velocity = 0.04
  val Range = 2
  val AttackStrength = 2

  implicit val timeout = Timeout(1 second)

  game.map.tiles(position).unit = Option(this);

  unit = self;
  self ! event.Order(Guard())

  def receive = {
    case e: event.Event =>
      if (!e.isInstanceOf[event.Tick]) println("Unit " + self + " received " + e)
      e match {
        case e: event.Tick => onTick()
        case e: event.Order => onOrder(e)
        case e: event.Action => onAction(e)
        case e: event.UnitDestroyed => onDestroyed(sender, e)
      }
    case Locate => sender ! position
    case e: Damage => onDamage(e.hp)
    case Alive => sender ! (if (alive) Alive else Dead)
    case e: UnitSpotted => onUnitSpotted(e)
    case _ =>
  }
  
  override def onTick() {
    val count = cooldowns.size
    super.onTick()
    if (count!=cooldowns.size) executeOrder(order)
  }

  override def createCooldown(actionType: Int) {
    super.createCooldown(actionType)
    if (actionType == Action.MoveTileStep) {
      game.map.surroundingTiles(position, 3).foreach(tile => {
        if (tile.isOccupied) tile.unit.get.unit ! UnitSpotted(this)
      })
    }
  }
  
  override def onAction(e: event.Action) {
    super.onAction(e)
    game.players.foreach(_.forward(e))
  }

  def onUnitSpotted(e: UnitSpotted) {
    if (order.isInstanceOf[Guard] && player != e.unitState.player && position.distance(e.unitState.position) <= Range) {
      self ! event.Order(Attack(e.unitState.unit))
    }
  }
  
  override def onOrder(e: event.Order) {
    super.onOrder(e)
    executeOrder(e.order)
  }
  
  /**
   * Execute order
   *
   * Should be run when:
   * 1. order is given
   * 2. action is complete without cooldown
   * 3. when cooldown is complete
   */
  def executeOrder(order: Order) {
    order match {
      case o: Move => executeMoveOrder(o)
      case o: Attack => executeAttackOrder(o)
      case o: Guard => executeGuardOrder(o)
    }
  }

  /**
   * Execute move order
   *
   * Check order goal conditions, set to default order if met else if there is no current active
   * action and no cooldown for move or turn actions, try to generate a new action which can be
   * a turn or move action event.
   */
  def executeMoveOrder(order: Move) = {
    if (order.destination == position || game.map.tiles(order.destination).isOccupied) {
      self ! event.Order(Guard())
    } else if (action.isInstanceOf[Idle] &&
      !cooldowns.keys.exists(_ == Action.MoveTileStep) &&
      !cooldowns.keys.exists(_ == Action.Turn)) {

      generateMoveAction(order.destination)
    }

  }

  /**
   * Generate action to move this unit towards the destination
   */
  def generateMoveAction(destination: Coordinate) {
    val target = game.map.calcDirection(position, destination)

    if (direction != target) {
      self ! event.Action(Turn(target))
    } else if (!game.map.tiles(position + direction).isOccupied) {
      self ! event.Action(MoveTileStep())
    }
  }

  /**
   * Executes attack order
   *
   * Ask target if alive if so check for active cooldowns if none generate appropriate action else do nothing.
   * If target is dead, give order to guard.
   */
  def executeAttackOrder(order: Attack) {
    order.target.ask(Alive).onSuccess({
      case Alive =>
        if (action.isInstanceOf[Idle] &&
          !cooldowns.keys.exists(_ == Action.Fire) &&
          !cooldowns.keys.exists(_ == Action.Turn)) {

          generateAttackAction(order.target)
        }
      case Dead => self ! event.Order(Guard())
    })
  }

  /**
   * Generates actions to fire at and damage target unit
   */
  def generateAttackAction(target: ActorRef) {
    target.ask(Locate).onSuccess({
      case targetPosition: Coordinate =>
        if (position.distance(targetPosition) <= Range) {
          self ! event.Action(Fire(targetPosition))
          target ! Damage(AttackStrength)
        } else {
          generateMoveAction(targetPosition)
        }
    })
  }

  def onDamage(damage: Int) {
    hp = hp - damage
    if (hp < 0) {
      alive = false
      self ! event.UnitDestroyed()
    }
  }

  def onDestroyed(sender: ActorRef, e: event.UnitDestroyed) {
    sender.ask(Locate).onSuccess({
      case senderPosition: Coordinate => game.map.tiles(senderPosition).unit = None
    })
    game.players.foreach(_.forward(e))
  }

  /**
   * Execute guard order
   */
  def executeGuardOrder(order: Guard) {
    for (tile <- game.map.surroundingTiles(position, Range)) {
      tile.unit match {
        case Some(unit) if (unit.player != player) => return self ! event.Order(Attack(unit.unit))
        case _ =>
      }
    }
  }

}