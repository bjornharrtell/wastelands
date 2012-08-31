package org.wololo.wastelands.core
import org.wololo.wastelands.core.unit.Direction
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.Player

import akka.actor.ActorRef

/**
 * CPU controlled Player
 */
class CpuPlayer(game: ActorRef) extends Player {

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
      map.removeShadeAround(e.position)
    case e: event.Order =>
  }
}