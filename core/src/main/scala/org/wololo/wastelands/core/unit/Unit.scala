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
   * When events are received, mutate state then trigger any events as a result of that state change
   */
  def receive = {
    case e: Event =>
      if (!e.isInstanceOf[event.Tick]) println("Unit received " + e)
      
      mutate(e)

      e match {
        case e: event.ActionComplete =>
          self ! event.Cooldown(e.actionType)
          gameState.players.foreach(_.forward(e))
        case e: event.Cooldown =>
          gameState.players.foreach(_.forward(e))
        case e: event.CooldownComplete =>
          // TODO: handle other orders than move
          move()
          gameState.players.foreach(_.forward(e))
        case e: event.Turn =>
          gameState.players.foreach(_.forward(e))
        case e: event.MoveTileStep =>
          gameState.players.foreach(_.forward(e))
        case e: event.Move =>
          move()
          gameState.players.foreach(_.forward(e))
        case e: event.Tick => tick()
        case _ =>
      }
  }

  /**
   * Action to take on given move order or cooldown complete with active move over
   *
   * If there is no current active action and no cooldown for move or turn actions,
   * try to generate a new action which can no (target reached), turn or move action event.
   */
  def move() = {
    if (action.isEmpty &&
      cooldowns.forall(cooldown =>
        cooldown.actionType == Action.Move ||
          cooldown.actionType == Action.Turn)) {

      val target = gameState.map.calcDirection(position, destination.get)

      if (target.isDefined) {
        if (direction != target.get) {
          self ! event.Turn(target.get)
        } else {
          self ! event.MoveTileStep()
        }
      }
    }
  }

  /**
   * Generated timed events (ActionComplete and CooldownComplete) if their duration has elapsed
   */
  def tick() = {
    // TODO: additional ticks are triggered before ActionComplete sets action to None... must mutate state here instead of ActionComplete (but the exact same thing must happen at clientside..)
    if (action.isDefined && gameState.ticks - actionStart >= actionLength) {
      self ! event.ActionComplete(action.get)
      // hack for todo above.. suboptimal since it will happen later too
      action = None
    }
    val cooldownsToRemove = ArrayBuffer[Cooldown]()
    cooldowns.foreach(cooldown => {
      if (gameState.ticks - cooldown.start >= cooldown.length) {
        self ! event.CooldownComplete(cooldown.actionType)
        cooldownsToRemove += cooldown
      }
    })
    // hack for todo above.. suboptimal since it will happen later too (but does not yet)
    cooldowns --= cooldownsToRemove
  }

}