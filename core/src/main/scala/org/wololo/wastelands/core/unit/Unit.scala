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
import akka.dispatch.Await

case object Alive
case object Dead
case object Locate
case object Who
case class Damage(hp: Int)

abstract class Unit(val player: ActorRef, val game: GameState, var position: Coordinate, var direction: Direction) extends Actor with UnitState {
  val Velocity = 0.04
  val Range = 2
  val AttackStrength = 2

  implicit val timeout = Timeout(1 second)

  
  println("Unit actor created, player: " + player)
  
  unit = self;
  //self ! event.Order(Guard())

  override def postStop() {
    println("Unit actor stopped: " + self)
  }
  
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
        case e: event.UnitDestroyed => destroyed(sender, e)
      }
    case Locate => sender ! position
    case e: Damage => damage(e.hp)
    case Who => sender ! player;
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
        self ! event.Order(Guard())
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

  def destroyed(sender: ActorRef, e: event.UnitDestroyed) {
    sender.ask(Locate).onSuccess({
      case senderPosition: Coordinate =>
        game.map.tiles(senderPosition).unit = None
    })
    game.players.foreach(_.forward(e))
  }

  /**
   * Execute guard order
   */
  def executeGuardOrder(order: Guard) = {
    for (tile <- game.map.surroundingTiles(position, Range)) {
      tile.unit match {
        case Some(unit) =>
          println("Guard from " + player + " on unit with player " + unit.player)
          if (unit.player != player) self ! event.Order(Attack(unit.unit))
        case _ =>
      }
    }
  }

}