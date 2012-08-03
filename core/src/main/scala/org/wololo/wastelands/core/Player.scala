package org.wololo.wastelands.core

import akka.actor._

class Player(server: ActorRef, gameState: GameClientState) extends Actor {
  server ! new event.Connect()

  // TODO: consider listening to events on a dedicated system to change state instead of expecting events to be relayed here.
  
  def receive = akka.event.LoggingReceive {
    case e: event.Connected =>
      server ! event.Create("NewGame")
    case e: event.Created => 
      e.game ! event.Join()
    case e: event.Joined =>
    
    case e: event.TileMapData =>
      //gameState.map = e.map
    case e: event.UnitCreated =>
      gameState.map.removeShadeAround(e.position)
    case e: event.Move => println(e)
    case e: event.Tick => println(e)
  }
}