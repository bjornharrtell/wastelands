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
    case e: event.Tick => tick()
    case e: event.UnitEvent =>
      println("Unit received " + e)

      mutate(e)

      e match {
        case e: event.Action => gameState.players.foreach(_.forward(e))
        case e: event.ActionComplete => self ! event.Cooldown(e.action)
        case e: event.Cooldown =>
        case e: event.CooldownComplete => triggerOrder(order)
        case e: event.Order => triggerOrder(e.order)
      }

      // forward unit event to each player
      // TODO: should not forward to originating player?
      // TODO: have clientside take care of ActionComplete, Cooldown and CooldownComplete since they are deterministic.. ?
      // TODO: also, these events seem to all be timed stuff so perhaps the tick handler at respective side should just do the state change without events...?
      //gameState.players.foreach(_.forward(e))
  }

  /**
   * Trigger eventual events from order
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

  /**
   * Generated timed events (ActionComplete and CooldownComplete) if their duration has elapsed
   */
  def tick() = {
    // TODO: action length from unittype
    if (action.isDefined && gameState.ticks - action.get.start >= 50) {
      self ! event.ActionComplete(action.get)
      // HACK: make sure no additional ActionComplete is triggered since ticks might be queued...
      // suboptimal since it will happen later too
      cooldowns += new Cooldown(action.get, gameState.ticks)
      action = None
    }
    val cooldownsToRemove = ArrayBuffer[Cooldown]()
    cooldowns.foreach(cooldown => {
      // TODO: cooldown length from unittype/action
      if (gameState.ticks - cooldown.start >= 30) {
        self ! event.CooldownComplete(cooldown.action)
        cooldownsToRemove += cooldown
      }
    })
    // hack for todo above.. suboptimal since it will happen later too (but does not yet)
    cooldowns --= cooldownsToRemove
  }

}