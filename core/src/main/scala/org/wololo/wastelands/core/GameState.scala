package org.wololo.wastelands.core
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.unit.Unit

trait GameState {
  val map: TileMap
  
  var ticks = 0
  
  var units = ArrayBuffer[Unit]()
  var projectiles = ArrayBuffer[Projectile]()
}