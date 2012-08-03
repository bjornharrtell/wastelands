package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx._
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.event.Event
import akka.actor._
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.HashMap
import org.wololo.wastelands.core.event.UnitCreated

// TODO: figure out if it makes sense to treat game as an actor both server and clientside... probably does not? at least not the same kind of actor?
// TODO: an idea is that we have Game and Player actor which share event types which modify respective owned game state. this might even be applied for Unit actor at serverside and UnitState at clientside
// TODO: or both Server and Client have a Game actor but it behaves differently depending on the owner?

class Game() extends Actor with GameState {
  val players: ArrayBuffer[ActorRef] = ArrayBuffer[ActorRef]()

  def initScenario = {
    //Direction.fromTileIndex((math.random * 7 + 1).toInt)
  }
  
  def receive = {
    case e: event.Join =>
      sender ! event.TileMapData(map)
      players += sender
      players.foreach(_ ! event.Joined(sender))
    case e: event.CreateUnit =>
      var unit: ActorRef = null
      e.unitType match {
        case UnitTypes.TestUnit1 => unit = context.actorOf(Props(new TestUnit1(sender, this, e.position, e.direction)))
      }
      units += unit
      players.filterNot(_ == sender).foreach(_ ! UnitCreated(unit, e.unitType, e.position, e.direction))
  }
}
