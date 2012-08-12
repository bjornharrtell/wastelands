package org.wololo.wastelands.core

import org.wololo.wastelands.core.event._
import org.wololo.wastelands.vmlayer.VMContext
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx.Screen
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.wololo.wastelands.core.unit.UnitClientState

class Client(val vmContext: VMContext) extends ClientInputHandler with Player with GameClientState {
  val screen = new Screen(this)
  var selectedUnit: Option[UnitClientState] = None

  // NOTE: local or remote server...
  val server = context.actorOf(Props[Server])
  //val server = context.actorFor("akka://server@192.168.0.100:9000/user/Server")

  server ! Connect()

  override def receive = akka.event.LoggingReceive {
    case e: event.Connected =>
      // TODO: present user choices for creating/joining games
      server ! event.Create("NewGame")
    case e: event.Render =>
      screen.render()
      vmContext.render(screen.bitmap)
    case e: event.Touch => touch(e)
    case e: event.Tick =>
      // NOTE: need to forward ticks to server if it's a local actor (actorOf above)
      server.forward(e)
      handlePlayerEvent(e)
    case e: Event => handlePlayerEvent(e)
  }

  /**
   * Take action as a result of a chosen tile and current state
   */
  def mapTileAction(coordinate: Coordinate) {
    if (selectedUnit.isDefined) {
      selectedUnit.get.self ! event.Order(Move(coordinate))
    }
  }

  /**
   * Take action as a result of a chosen unit and current state
   */
  def unitAction(unit: UnitClientState) {
    if (selectedUnit.isDefined) {
      if (unit.player != self) {
        selectedUnit.get.self ! event.Order(Attack(unit.self))
      } else {
        selectedUnit.get.unselect()
        unit.select()
        selectedUnit = Option(unit)
      }
    } else if (unit.player == self) {
      unit.select()
      selectedUnit = Option(unit)
    }
  }

}