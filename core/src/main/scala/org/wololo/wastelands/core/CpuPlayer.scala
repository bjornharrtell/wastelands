package org.wololo.wastelands.core
import akka.actor.ActorRef
import org.wololo.wastelands.core.unit.UnitTypes
import org.wololo.wastelands.core.unit.Direction

class CpuPlayer(server: ActorRef, game: ActorRef, gameState: GameClientState) extends Player(server, gameState) {

  game ! event.Join()
  
  override def receive = akka.event.LoggingReceive {
    case e: event.Joined =>
      game ! event.CreateUnit(UnitTypes.TestUnit2, (5,4), Direction.fromTileIndex((math.random * 7 + 1).toInt))
    case e: event.TileMapData =>
      //gameState.map = e.map
    case e: event.CreateUnit =>
      gameState.map.removeShadeAround(e.position)
    case e: event.Move => println(e)
  }
}