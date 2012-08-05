package org.wololo.wastelands.core

import akka.actor._
import unit.UnitClientState
import org.wololo.wastelands.core.unit.UnitTypes
import org.wololo.wastelands.core.unit.Direction

class Player(server: ActorRef, gameState: GameClientState) extends Actor {
  server ! new event.Connect()

  var game: ActorRef = null

  def receive = akka.event.LoggingReceive {
    case e: event.Connected =>
      server ! event.Create("NewGame")
    case e: event.Created =>
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
      if (self == e.player) gameState.map.removeShadeAround(e.position)
      var unitState = new UnitClientState(e.unit, e.player, gameState, e.unitType, e.position, e.direction)
      gameState.unitStates += (e.unit -> unitState)
    case e: event.Turn =>
      gameState.unitStates.get(sender).get.mutate(e)
    case e: event.Tick => println(e)
  }
}