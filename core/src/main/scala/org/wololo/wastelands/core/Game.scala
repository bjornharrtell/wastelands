package org.wololo.wastelands.core
import scala.collection.mutable.ArrayBuffer

import org.wololo.wastelands.core.event.UnitCreated
import org.wololo.wastelands.core.unit.TestUnit1
import org.wololo.wastelands.core.unit.TestUnit2
import org.wololo.wastelands.core.unit.Harvester
import org.wololo.wastelands.core.unit.UnitTypes

import akka.actor._

class Game() extends Actor with GameState {
  val units = ArrayBuffer[ActorRef]()

  def receive = {
    case e: event.Event =>
      if (!e.isInstanceOf[event.Tick]) println("Game received " + e)
      e match {
        case e: event.Join =>
          events.foreach(sender ! _)
          events += e
          //sender ! event.TileMapData(map)
          players += sender
          players.foreach(_ ! event.Joined(sender))
        case e: event.CreateUnit =>
          events += e
          var unit = e.unitType match {
            case UnitTypes.TestUnit1 => context.actorOf(Props(new TestUnit1(sender, this, e.position, e.direction)))
            case UnitTypes.TestUnit2 => context.actorOf(Props(new TestUnit2(sender, this, e.position, e.direction)))
            case UnitTypes.Harvester => context.actorOf(Props(new Harvester(sender, this, e.position, e.direction)))
          }
          units += unit
          players.foreach(_ ! UnitCreated(unit, sender, e.unitType, e.position, e.direction))
        case e: event.Tick =>
          ticks += 1
          units.foreach(_.forward(e))
      }
  }
}
