package org.wololo.wastelands.core.server

import org.wololo.wastelands.core.unit._
import akka.actor._
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.Game

class GameActor extends Game with Actor {
  var units = Vector[ActorRef]()

  def receive = {
    case e: event.Join =>
      //events.foreach(sender ! _)
      //events += e
      //sender ! event.TileMapData(map)
      players = players :+ sender
      players.foreach(_ ! event.Joined(sender))
    case e: event.CreateUnit =>
      //events += e
      val player = sender
      var unit = e.unitType match {
        case Unit.TestUnit1 => context.actorOf(Props(new TestUnit1(player, this, e.position, e.direction)))
        case Unit.TestUnit2 => context.actorOf(Props(new TestUnit2(player, this, e.position, e.direction)))
        case Unit.Harvester => context.actorOf(Props(new Harvester(player, this, e.position, e.direction)))
      }
      units = units :+ unit
      // TODO: Send serializable unit state instance instead...
      players.foreach(_ ! event.UnitCreated(unit, player, e.unitType, e.position, e.direction))
    case e: event.Tick =>
      ticks += 1
      units.foreach(_.forward(e))
  }
}
