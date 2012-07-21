package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx._
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.event.TouchEvent

import scala.actors._
import scala.actors.Actor._

case object Create extends Event
case object End extends Event
case class Join() extends Event
case class Upload(map: TileMap) extends Event
case class Joined(player: Player) extends Event
case class UnitMove(unit: Unit) extends Event

class Game extends Actor with GameState {
  val map: TileMap = new TileMap()
  val players = ArrayBuffer[OutputChannel[Any]]()
  
  def handleEvent(e: Event) {
    println(e)
    e match {
      case Create => exit
      case End => exit
      case e: Join => 
        sender ! Upload(map)
        players += sender
        players.foreach(_ ! Joined(sender.asInstanceOf[Player]))
      case e: UnitMove => exit
    }
  }
 
  def act { loop { react {
	case e:Event => handleEvent(e)
  }}}
}
