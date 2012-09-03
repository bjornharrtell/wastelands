package org.wololo.wastelands.core.client

import org.wololo.wastelands.core.event
import org.wololo.wastelands.vmlayer.VMContext
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx.Screen
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.wololo.wastelands.core.Player
import org.wololo.wastelands.core.server.ServerActor
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.Coordinate

/**
 * PC controlled basic client
 * 
 * Platform agnostic via VMContext
 */
class Client(val vmContext: VMContext, val server: ActorRef) extends Player[ClientUnit] with ClientGame with ClientInputHandler {
  val screen = new Screen(this)
  var selectedUnit: Option[ActorRef] = None
  
  server ! event.Connect()

  override def receive = clientReceive orElse playerReceive
  
  def clientReceive: Receive = {
    case e: event.Connected =>
      // TODO: present user choices for creating/joining games
      server ! event.CreateGame("NewGame")
    case e: event.Render =>
      screen.render()
      vmContext.render(screen.bitmap)
    case e: event.Touch => touch(e)
  }
  
  override def onUnitCreated(e: event.UnitCreated) {
    if (self == e.player) map.removeShadeAround(e.position)
    var unitState = new ClientUnit(e.player, this, e.unitType, e.position, e.direction)
    units += (e.unit -> unitState)
  }
  
  override def onUnitDestroyed(e: event.UnitDestroyed) {
    super.onUnitDestroyed(e)
    units.get(sender).get.asInstanceOf[ClientUnit].explode = true
  }
  
  override def onTick() {
    super.onTick()
    projectiles = projectiles.filterNot(projectile => projectile.start+projectile.duration<ticks)
  }

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
      if (unitState.player != self) {
        // TODO: setting state should be more generic for orders
        selectedUnitState.order = Attack(unit)
        selectedUnit.get ! event.Order(selectedUnitState.order)
      } else {
        selectedUnitState.unselect()
        unitState.select()
        selectedUnit = Option(unit)
      }
    } else if (unitState.player == self) {
      unitState.select()
      selectedUnit = Option(unit)
    }
  }

}