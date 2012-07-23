package org.wololo.wastelands.core

import scala.actors._
import scala.actors.Actor._
import org.wololo.wastelands.core.unit.Order
import org.wololo.wastelands.core.event._

class Player extends Actor {
	val order: Order
	
	def handleEvent(e: Event) {
    println(e)
    e match {
      case e: UnitMove => exit
    }
  }
 
  def act { loop { react {
	case e:Event => handleEvent(e)
  }}}
}