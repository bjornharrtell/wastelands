package org.wololo.wastelands.core

trait GameState {
  val map: TileMap
  
  var ticks = 0

  var selectedUnit: Option[Unit] = None

  var player = 0
  
  var units = ArrayBuffer[Unit]()
  var projectiles = ArrayBuffer[Projectile]()
}