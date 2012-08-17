package org.wololo.wastelands.core
import akka.actor.ActorRef
import org.wololo.wastelands.core.unit.UnitTypes
import org.wololo.wastelands.core.unit.Direction

class CpuPlayer(game:ActorRef, gameState: GameCpuPlayerState) extends Player(gameState) {

  game ! event.Join()

  override def receive = akka.event.LoggingReceive {
    case e: event.Joined =>
      // TODO: create units from scenario definition
      game ! event.CreateUnit(UnitTypes.TestUnit1, (1, 2), Direction.random)
      game ! event.CreateUnit(UnitTypes.TestUnit1, (8, 8), Direction.random)
      game ! event.CreateUnit(UnitTypes.TestUnit1, (9, 11), Direction.random)
    case e: event.TileMapData =>
    //gameState.map = e.map
    case e: event.CreateUnit =>
      gameState.map.removeShadeAround(e.position)
    case e: event.Order =>
  }
}