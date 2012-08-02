package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx._
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.event.Event
import akka.actor._
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.HashMap

// TODO: figure out if it makes sense to treat game as an actor both server and clientside... probably does not? at least not the same kind of actor?
// TODO: an idea is that we have Game and Player actor which share event types which modify respective owned game state. this might even be applied for Unit actor at serverside and UnitState at clientside
// TODO: or both Server and Client have a Game actor but it behaves differently depending on the owner?

class Game extends Actor with GameState {
  val map: TileMap = new TileMap()
  var players: HashMap[Int, ActorRef] = HashMap.empty[Int, ActorRef]

  def handleAction(sender: ActorRef, e: event.Action) {
    players.values.filterNot(_ == sender).foreach(_ ! e)

    e match {
      case e: event.Action if e.actionType == event.Action.MOVE => move(e)
    }
  }

  def handleEvent(e: Event) {
    println(e)
    e match {
      case e: event.Create => exit
      case e: event.End => exit
      case e: event.Join =>
        sender ! event.TileMapData(map)
        players += (e.id -> sender)
        players.values.foreach(_ ! event.Joined)
      case e: event.Action => handleAction(sender, e)
    }
  }

  def receive = {
    case e: Event => handleEvent(e)
  }

  def move(e: event.Action) {
    // TODO: change state of unit

    // TODO: figure out how to best handle what comes after: ticks -> move complete -> cooldown
  }
}
