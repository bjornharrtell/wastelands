package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.vmlayer.Sound
import org.wololo.wastelands.core.unit.order.Move
import org.wololo.wastelands.core.unit.order.Attack
import scala.collection.mutable.Subscriber
import scala.collection.mutable.Publisher
import org.wololo.wastelands.core.unit.order.Guard


/**
 * Base combined implementation for units
 *
 * This class only contains helper information for renderer and collission detection.
 * Mixins provide base implementations for combined unit logic.
 */
abstract class Unit(val game: Game, val player: Int, val position: Coordinate) extends Selectable with Subscriber[Event, Publisher[Event]] {
  var isOnScreen = false
  val ScreenBounds: Rect = (0, 0, 0, 0)

  var order: Order = new Guard(this)
  var action: Option[Action] = None 

  val Velocity = 0.04
  
  // TODO: this is move action state, should it live here?
  var moveDistance = 0.0

  var direction: Direction = (math.random * 7 + 1).toInt

  var alive = true
  var hp = 10
  var explode = false
  var exploding = false

  val Range = 2

  def notify(pub: Publisher[Event], event: Event) {
    event match {
      case e: ActionCompleteEvent => onActionComplete()
    }
  }

  def onActionComplete() {
    action = order.generateAction
  }
  
  def guard() {
    
  }
  
  def moveTo(position: Coordinate) {
    order = new Move(this, position)
    if (action.isEmpty)
      action = order.generateAction
  }

  def attack(target: Unit) {
    order = new Attack(this, target)
  }
}