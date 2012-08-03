package org.wololo.wastelands.core
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.unit.Unit
import akka.actor.ActorRef
import scala.collection.mutable.HashMap
import org.wololo.wastelands.core.unit.UnitClientState

trait GameState {
  var map: TileMap = new TileMap()
  var ticks = 0
  var units = ArrayBuffer[ActorRef]()
}

trait GameClientState extends GameState {
  var projectiles = ArrayBuffer[Projectile]()
  
  var unitStates = HashMap[ActorRef, UnitClientState]()
  
  // TODO: need to keep UnitState instances togheter with the unit actorrefs
}