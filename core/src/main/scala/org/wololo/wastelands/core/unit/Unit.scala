package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit.order.Move
import org.wololo.wastelands.core.unit.order.Attack
import org.wololo.wastelands.core.unit.order.Guard
import org.wololo.wastelands.core.unit.action.MoveStep
import org.wololo.wastelands.core.unit.action.Turn
import org.wololo.wastelands.core.unit.action.Fire
import java.io.File
import org.wololo.wastelands.core.event.Event
import scala.collection.mutable.ArrayBuffer

import scala.actors._
import scala.actors.Actor._

case class MoveTile(coordinate: Coordinate) extends Event
case class ChangeOrder(order: Order) extends Event

/**
 * Base abstract implementation for units
 */
abstract class Unit(val player: Player, val position: Coordinate) extends Actor {
  val Velocity = 0.04
  var moveDistance = 0.0
  var direction: Direction = Direction.fromTileIndex((math.random * 7 + 1).toInt)

  val Range = 2
  val AttackStrength = 2
  var alive = true
  var hp = 10
  
  val id = 0
  
  var order: Order = new Guard(this)
  
  def changeOrder(e: ChangeOrder) {
    order = e.order
  }
  
  def handleEvent(e: Event) {
    println(e)
    e match {
      case e: MoveTile => exit
      case e: ChangeOrder => changeOrder(e)
    }
  }
 
  def act { loop { react {
	case e:Event => handleEvent(e)
  }}}
}