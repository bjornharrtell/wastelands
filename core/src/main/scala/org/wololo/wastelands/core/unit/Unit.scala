package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.event.Event
import scala.collection.mutable.ArrayBuffer
import akka.actor._
import com.typesafe.config.ConfigFactory

abstract class Unit(val player: ActorRef, val gameState: GameState, var position: Coordinate, var direction: Direction) extends Actor with UnitState {
  val Velocity = 0.04
  val Range = 2
  val AttackStrength = 2

  /**
   * When events are received:
   * 1. mutate state
   * 2. trigger any events as a result of the state change
   * 3. forward event to other players
   */
  def receive = {
    case e: event.Tick => if (tick()) triggerOrder(order)
    case e: event.UnitEvent =>
      println("Unit received " + e)

      mutate(e)

      e match {
        case e: event.Action => gameState.players.foreach(_.forward(e))
        case e: event.Order => triggerOrder(e.order)
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
      case o: Attack =>
      case o: Guard =>
    }
  }

  /**
   * Action to take from move order
   *
   * If there is no current active action and no cooldown for move or turn actions,
   * try to generate a new action which can no (target reached), turn or move action event.
   */
  def move(order: Move) = {
    if (action.isEmpty &&
      cooldowns.forall(!_.action.isInstanceOf[MoveTileStep]) &&
      cooldowns.forall(!_.action.isInstanceOf[Turn])) {

      val target = gameState.map.calcDirection(position, order.destination)

      if (target.isDefined) {
        if (direction != target.get) {
          self ! event.Action(Turn(target.get))
        } else {
          // TODO: check that tile to be moved to is not occupied...
          self ! event.Action(MoveTileStep())
        }
      }
    }
  }

}