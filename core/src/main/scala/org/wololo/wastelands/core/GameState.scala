package org.wololo.wastelands.core
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.unit.Unit
import akka.actor.ActorRef

trait GameState {
  val map: TileMap = new TileMap()
  var ticks = 0
  var units = ArrayBuffer[ActorRef]()
}

class GameClientState extends GameState {
  var projectiles = ArrayBuffer[Projectile]()
}