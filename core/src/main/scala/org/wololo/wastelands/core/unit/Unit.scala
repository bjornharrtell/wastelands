package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.event.Event
import scala.collection.mutable.ArrayBuffer
import akka.actor._
import akka.pattern.{ ask, pipe }
import akka.util.duration._
import com.typesafe.config.ConfigFactory
import akka.util.Timeout

case object Alive
case object Dead
case object Locate
case class Position(position: (Int, Int))
case class Damage(hp: Int)

abstract class Unit(val player: ActorRef, val game: GameState, var position: Coordinate, var direction: Direction) extends Actor with UnitState {
  val Velocity = 0.04
  val Range = 2
  val AttackStrength = 2

  implicit val timeout = Timeout(1 second)

  /**
   * When events are received:
   * 1. mutate state
   * 2. trigger any events as a result of the state change
   * 3. forward event to other players
   */
  def receive = {
    case e: event.Event =>
      if (!e.isInstanceOf[event.Tick]) println("Unit received " + e)
      e match {
        case e: event.Tick => if (tick()) triggerOrder(order)
        case e: event.Order => order(e); triggerOrder(e.order)
        case e: event.Action => action(e); game.players.foreach(_.forward(e))
        case e: event.UnitDestroyed => game.players.foreach(_.forward(e))
      }
    case Locate => sender ! position
    case e: Damage => damage(e.hp)
    case Alive => sender ! (if (alive) Alive else Dead)
  }

  /**
   * Execute order
   *
   * Should be run when:
   * 1. order is given
   * 2. action is complete without cooldown
   * 3. when cooldown is complete
   */
  def triggerOrder(order: Order) {
    order match {
      case o: Move => executeMoveOrder(o)
      case o: Attack => executeAttackOrder(o)
      case o: Guard =>
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
      this.order = Guard()
    } else {
      if (action.isInstanceOf[Idle] &&
        !cooldowns.keys.exists(_ == Action.MoveTileStep) &&
        !cooldowns.keys.exists(_ == Action.Turn)) {

        generateMoveAction(order.destination)
      }
    }
  }

  /**
   * Generates actions to move this unit towards the destination
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
      case Dead =>
        this.order = Guard()
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

  def damage(damage: Int) {
    hp = hp - damage
    if (hp < 0) {
      alive = false
      self ! event.UnitDestroyed()
    }
  }

}