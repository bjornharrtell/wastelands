package org.wololo.wastelands.core

import org.wololo.wastelands.core.event._
import org.wololo.wastelands.vmlayer.VMContext
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx.Screen
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.wololo.wastelands.core.unit.UnitClientState

class Client(val vmContext: VMContext) extends Actor with ClientInputHandler with GamePlayerState {
  val screen = new Screen(this)
  var selectedUnit: Option[UnitClientState] = None

  val player = context.actorOf(Props(new Player(this)))
  
  // NOTE: local or remote server...
  val server = context.actorOf(Props[Server])
  //val server = context.actorFor("akka://server@192.168.0.100:9000/user/Server")

  server ! Connect()

  def receive = {
    case e: event.Connected =>
      // TODO: present user choices for creating/joining games
      server ! event.CreateGame("NewGame")
    case e: event.GameCreated => player.forward(e)
    case e: event.Render =>
      screen.render()
      vmContext.render(screen.bitmap)
    case e: event.Touch => touch(e)
    case e: event.Tick =>
      // NOTE: need to forward ticks to server if it's a local actor (actorOf above)
      server.forward(e)
      player.forward(e)
  }

  /**
   * Take action as a result of a chosen tile and current state
   */
  def mapTileAction(coordinate: Coordinate) {
    if (selectedUnit.isDefined) {
      selectedUnit.get.order = Move(coordinate)
      selectedUnit.get.self ! event.Order(selectedUnit.get.order)
    }
  }

  /**
   * Take action as a result of a chosen unit and current state
   */
  def unitAction(unit: UnitClientState) {
    if (selectedUnit.isDefined) {
      if (unit.player != player) {
        selectedUnit.get.order = Attack(unit.self)
        selectedUnit.get.self ! event.Order(selectedUnit.get.order)
      } else {
        selectedUnit.get.unselect()
        unit.select()
        selectedUnit = Option(unit)
      }
    } else if (unit.player == player) {
      unit.select()
      selectedUnit = Option(unit)
    }
  }

}