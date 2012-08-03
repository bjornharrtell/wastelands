package org.wololo.wastelands.core

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.wololo.wastelands.core.unit.Order
import org.wololo.wastelands.core.event.Event

class Player(client: Client, server: ActorRef) extends Actor with GameClientState {
  var game: ActorRef = null
  
  server ! event.Create("NewGame")
  
  def receive = {
    case e: event.Created => 
      game = e.game
    case e: event.TileMapData =>
      this.map = e.map
    case e: event.Move => println(e)
  }
}