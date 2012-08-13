package org.wololo.wastelands.core

import akka.actor._
import unit.UnitClientState
import org.wololo.wastelands.core.unit.UnitTypes
import org.wololo.wastelands.core.unit.Direction
import org.wololo.wastelands.core.event.Event

trait Player extends Actor with ActorLogging {
  this: GameState =>

  var game: ActorRef = null

  def handlePlayerEvent(e: Event) = {
    if (!e.isInstanceOf[event.Tick]) println("Player received " + e)
    
    e match {
      case e: event.GameCreated =>
        game = e.game
        game ! event.Join()
      case e: event.Joined =>
        if (self == e.player) {
          // TODO: create units from scenario definition
          game ! event.CreateUnit(UnitTypes.TestUnit2, (5, 4), Direction.random)
          game ! event.CreateUnit(UnitTypes.TestUnit2, (6, 6), Direction.random)
          game ! event.CreateUnit(UnitTypes.Harvester, (5, 6), Direction.random)
        }
      case e: event.TileMapData =>
        // TODO: use map data...
        //gameState.map = e.map
      case e: event.UnitCreated =>
        if (self == e.player) map.removeShadeAround(e.position)
        var unitState = new UnitClientState(e.unit, e.player, this, e.unitType, e.position, e.direction)
        units += (e.unit -> unitState)
      case e: event.Action =>
        units.get(sender).get.action(e)
      case e: event.Tick =>        
        ticks += 1
        units.values.foreach(_.tick())
      case _ =>
    }
  }
}