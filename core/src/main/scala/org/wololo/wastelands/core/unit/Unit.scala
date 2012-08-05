package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.event.Event
import scala.collection.mutable.ArrayBuffer
import akka.actor._
import com.typesafe.config.ConfigFactory

abstract class Unit(val player: ActorRef, val gameState:GameState, val position: Coordinate, var direction: Direction) extends Actor with UnitState {
  val Velocity = 0.04
  val Range = 2
  val AttackStrength = 2

  /**
   * When events are received, mutate state then trigger any events as a result of that state change 
   */
  def receive = akka.event.LoggingReceive {
    case e: Event =>
      mutate(e)
      
      e match {
        case e: event.Move => move(e.destination)
        case e: event.Tick => tick
        case _ =>
      }
      
      gameState.players.foreach(_ ! e)
  }

  /**
   * Action to take on given move order or action complete with active move over
   *
   * If there is no current active action and no cooldown for move or turn actions,
   * try to generate a new action which can no (target reached), turn or move action event.
   */
  def move(destination: Coordinate) = {
    if (action.isEmpty &&
      cooldowns.forall(cooldown =>
        cooldown.actionType == Action.Move ||
          cooldown.actionType == Action.Turn)) {

      val target = gameState.map.calcDirection(position, destination)

      if (target.isDefined) {
        if (direction != target.get) {
          self ! event.Turn(target.get)
        } else {
          self ! event.MoveTileStep
        }
      }
    }
  }

  def actionComplete = {
    order match {
      case Order.Move =>
    }
  }

  def tick() {
    if (gameState.ticks - actionStart >= actionLength) {
      self ! event.Cooldown(action.get)
      self ! event.ActionComplete
    }
  }

}