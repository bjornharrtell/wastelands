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
        case e: event.Locate => sender ! event.Position(position)
        case e: event.Damage =>
          hp = hp - e.hp
          if (hp < 0) {
            alive = false
            self ! event.UnitDestroyed()
          }
        case e: event.UnitDestroyed => game.players.foreach(_.forward(e))
      }
  }

  /**
   * Trigger eventual action from order
   *
   * Should be run when:
   * 1. order is given
   * 2. action is complete without cooldown
   * 3. when cooldown is complete
   */
  def triggerOrder(order: Order) {
    order match {
      case o: Move => move(o)
      case o: Attack => attack(o)
      case o: Guard =>
    }
  }

  /**
   * Action to take from move order
   *
   * Check order goal conditions, set to default order if met else if there is no current active
   * action and no cooldown for move or turn actions, try to generate a new action which can be
   * a turn or move action event.
   */
  def move(order: Move) = {
    if (order.destination == position || game.map.tiles(order.destination).isOccupied) {
      this.order = Guard()
    } else {
      if (action.isInstanceOf[Idle] &&
        cooldowns.forall(!_.action.isInstanceOf[MoveTileStep]) &&
        cooldowns.forall(!_.action.isInstanceOf[Turn])) {

        generateMoveAction(order.destination)
      }
    }
  }

  def generateMoveAction(destination: Coordinate) {
    val target = game.map.calcDirection(position, destination)

    if (direction != target) {
      self ! event.Action(Turn(target))
    } else if (!game.map.tiles(position + direction).isOccupied) {
      self ! event.Action(MoveTileStep())
    }
  }

  def attack(order: Attack) {
    // TODO: ask target if it's alive, if not set order to Guard

    if (action.isInstanceOf[Idle] &&
      cooldowns.forall(!_.action.isInstanceOf[Fire]) &&
      cooldowns.forall(!_.action.isInstanceOf[Turn])) {

      generateAttackAction(order.target)
    }
  }

  def generateAttackAction(target: ActorRef) {
    target.ask(event.Locate()).onSuccess({
      case e: event.Position =>
        if (position.distance(e.position) <= Range) {
          self ! event.Action(Fire(e.position))
          target ! event.Damage(AttackStrength)
        } else {
          generateMoveAction(e.position)
        }
    })
  }

}