package org.wololo.wastelands.core

import akka.actor._
import unit.UnitClientState
import org.wololo.wastelands.core.unit.UnitTypes
import org.wololo.wastelands.core.unit.Direction
import org.wololo.wastelands.core.event.Event

class Player(gameState: GamePlayerState) extends Actor {
  var game: ActorRef = null
  
  def receive = {
    case e: event.Event =>
      if (!e.isInstanceOf[event.Tick]) println("Player received " + e)
      e match {
      case e: event.GameCreated => join(e.game)
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
        if (self == e.player) gameState.map.removeShadeAround(e.position)
        var unitState = new UnitClientState(e.player, gameState, e.unitType, e.position, e.direction)
        gameState.units += (e.unit -> unitState)
      case e: event.Action =>
        gameState.units.get(sender).get.action(e)
      case e: event.Tick =>        
        gameState.ticks += 1
        gameState.units.values.foreach(_.tick())
        // TODO: remove elapsed projectiles
      case _ =>
    }
  }
  
  def join(game: ActorRef) {
    this.game = game 
    game ! event.Join()
  }
}