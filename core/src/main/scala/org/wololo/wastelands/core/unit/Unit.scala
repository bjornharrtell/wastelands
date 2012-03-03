package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit.order.Move
import org.wololo.wastelands.core.unit.order.Attack
import org.wololo.wastelands.core.unit.order.Guard

/**
 * Base abstract implementation for units
 */
abstract class Unit(val game: Game, val player: Int, val position: Coordinate) extends Selectable with Subscriber {
  var isOnScreen = false
  val ScreenBounds: Rect = (0, 0, 0, 0)
  
  game.map.tiles(position).unit = Option(this)

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

  def notify(pub: Publisher, event: Event) {
    event match {
      case x: ActionCompleteEvent => onActionComplete()
    }
  }

  def onActionComplete() {
    action = order.generateAction()
    
    if (action.isEmpty) guard()
  }
  
  def guard() {
    order = new Guard(this)
  }
  
  def moveTo(position: Coordinate) {
    order = new Move(this, position)
    if (action.isEmpty)
      action = order.generateAction()
  }

  def attack(target: Unit) {
    order = new Attack(this, target)
  }
}