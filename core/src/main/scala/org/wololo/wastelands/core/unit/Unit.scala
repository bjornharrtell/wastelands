package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.event.Event
import scala.collection.mutable.ArrayBuffer
import akka.actor._
import com.typesafe.config.ConfigFactory

abstract class Unit(val state: ServerUnitState) extends Actor {
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
    case e: event.Event =>
      if (!e.isInstanceOf[event.Tick]) println("Unit received " + e)
      e match {
        case e: event.Tick => if (state.tick()) triggerOrder(state.order)
        case e: event.Order => state.order(e); triggerOrder(e.order)
        case e: event.Action => state.action(e); state.game.players.foreach(_.forward(e))
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
   * Check order goal conditions, set to default order if met else if there is no current active
   * action and no cooldown for move or turn actions, try to generate a new action which can be
   * a turn or move action event.
   */
  def move(order: Move) = {
    if (order.destination == state.position || state.game.map.tiles(order.destination).isOccupied) {
      state.order = Guard()
    } else {
      if (state.action.isInstanceOf[Idle] &&
        state.cooldowns.forall(!_.action.isInstanceOf[MoveTileStep]) &&
        state.cooldowns.forall(!_.action.isInstanceOf[Turn])) {

        val target = state.game.map.calcDirection(state.position, order.destination)

        if (state.direction != target) {
          self ! event.Action(Turn(target))
        } else if (!state.game.map.tiles(state.position + state.direction).isOccupied) {
          self ! event.Action(MoveTileStep())
        }
      }
    }
  }

}