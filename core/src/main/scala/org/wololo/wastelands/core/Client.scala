package org.wololo.wastelands.core

import org.wololo.wastelands.core.event._
import org.wololo.wastelands.vmlayer.VMContext
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx.Screen
import akka.actor._
import com.typesafe.config.ConfigFactory

class Client(val vmContext: VMContext) extends Actor with ClientInputHandler with GamePlayerState {
  val screen = new Screen(this)
  var selectedUnit: Option[ActorRef] = None

  val player = context.actorOf(Props(new Player(this)))
  
  // NOTE: local or remote server...
  val server = context.actorOf(Props[Server])
  //val server = context.actorFor("akka://server@192.168.0.100:9000/user/Server")

  server ! Connect()

  def receive = {
    case e: event.Connected =>
      // TODO: present user choices for creating/joining games
      server ! event.CreateGame("NewGame")
    case e: event.GameCreated => 
      player.forward(e)
    case e: event.Render =>
      screen.render()
      vmContext.render(screen.bitmap)
    case e: event.Touch => touch(e)
    case e: event.Tick =>
      // NOTE: need to forward ticks to server if it's a local actor (actorOf above)
      server.forward(e)
      player.forward(e)
  }

  // TODO: The below methods are ugly because actor is separated from state
  // The only way to avoid this is to make every interaction event driven?
  // And this means we should have a "client unit actor" aswell as the current serverside actor...?
  
  // TODO: think about what the client needs to do with units...!
  
  /**
   * Take action as a result of a chosen tile and current state
   */
  def mapTileAction(coordinate: Coordinate) {
    if (selectedUnit.isDefined) {
      // TODO: syntax to get state sux      
      val selectedUnitState = units.get(selectedUnit.get).get
      // TODO: setting state should be more generic for orders
      selectedUnitState.order = Move(coordinate)
      selectedUnit.get ! event.Order(selectedUnitState.order)
    }
  }

  /**
   * Take action as a result of a chosen unit and current state
   */
  def unitAction(unit: ActorRef) {
    val unitState = units.get(unit).get
    if (selectedUnit.isDefined) {
      val selectedUnitState = units.get(selectedUnit.get).get
      if (unitState.player != player) {
        // TODO: setting state should be more generic for orders
        selectedUnitState.order = Attack(unit)
        selectedUnit.get ! event.Order(selectedUnitState.order)
      } else {
        selectedUnitState.unselect()
        unitState.select()
        selectedUnit = Option(unit)
      }
    } else if (unitState.player == player) {
      unitState.select()
      selectedUnit = Option(unit)
    }
  }

}