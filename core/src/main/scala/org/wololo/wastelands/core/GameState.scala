package org.wololo.wastelands.core
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.unit.Unit
import akka.actor.ActorRef
import scala.collection.mutable.HashMap
import org.wololo.wastelands.core.unit.UnitClientState
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.unit.UnitState

trait GameState {
  val events = ArrayBuffer[Event]()
  
  val players: ArrayBuffer[ActorRef] = ArrayBuffer[ActorRef]()
  val map: TileMap = new TileMap()
  var ticks = 0
  
  val projectiles = ArrayBuffer[Projectile]()
  
  val units = HashMap[ActorRef, UnitState]()
}


trait GameClientState extends GameState {
  //override val units = HashMap[ActorRef, UnitClientState]()
}