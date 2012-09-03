package org.wololo.wastelands.core
import org.wololo.wastelands.core.unit.Direction
import org.wololo.wastelands.core.unit.Unit

import akka.actor.ActorRef

/**
 * CPU controlled Player
 */
class CpuPlayer(gametojoin: ActorRef) extends Player[PlayerUnit] {

  game = gametojoin
  
  game ! event.Join()

  override def receive: Receive = cpuPlayerReceive orElse playerReceive
  
  def cpuPlayerReceive: Receive = {
    case e: event.Joined =>
      if (self == e.player) {
        // TODO: create units from scenario definition
        game ! event.CreateUnit(Unit.TestUnit1, (1, 2), Direction.random)
        game ! event.CreateUnit(Unit.TestUnit1, (9, 9), Direction.random)
        game ! event.CreateUnit(Unit.TestUnit1, (10, 11), Direction.random)
      }
  }
}