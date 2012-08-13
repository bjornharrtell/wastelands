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
    case e: event.Event =>
      if (!e.isInstanceOf[event.Tick]) println("Unit received " + e)
      e match {
        case e: event.Tick => if (tick()) triggerOrder(order)
        case e: event.Order => order(e); triggerOrder(e.order)
        case e: event.Action => action(e); gameState.players.foreach(_.forward(e))
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
    if (action.isInstanceOf[Idle] &&
      cooldowns.forall(!_.action.isInstanceOf[MoveTileStep]) &&
      cooldowns.forall(!_.action.isInstanceOf[Turn])) {

      val target = gameState.map.calcDirection(position, order.destination)

      if (target.isDefined) {
        if (direction != target.get) {

          self ! event.Action(Turn(target.get))
        } else if (!gameState.map.tiles(position+direction).isOccupied) {
          self ! event.Action(MoveTileStep())
        }
      }
    }
  }

}