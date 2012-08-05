package org.wololo.wastelands.core
import akka.actor.ActorRef
import org.wololo.wastelands.core.unit.UnitTypes
import org.wololo.wastelands.core.unit.Direction

class CpuPlayer(game:ActorRef) extends Player with GameState {

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
      map.removeShadeAround(e.position)
    case e: event.Move => println(e)
  }
}