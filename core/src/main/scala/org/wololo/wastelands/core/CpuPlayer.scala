package org.wololo.wastelands.core
import akka.actor.ActorRef
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Direction

class CpuPlayer(game: ActorRef, gameState: GameCpuPlayerState) extends Player(gameState) {

  game ! event.Join()

  override def receive = {
    case e: event.Joined =>
      if (self == e.player) {
        // TODO: create units from scenario definition
        game ! event.CreateUnit(Unit.TestUnit1, (1, 2), Direction.random)
        game ! event.CreateUnit(Unit.TestUnit1, (9, 9), Direction.random)
        game ! event.CreateUnit(Unit.TestUnit1, (10, 11), Direction.random)
      }
    case e: event.TileMapData =>
    //gameState.map = e.map
    case e: event.UnitCreated =>
      gameState.map.removeShadeAround(e.position)
    case e: event.Order =>
  }
}