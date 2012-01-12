package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

abstract class Unit(val map: GameMap, val position: Coordinate) {
  var visible = false
  val ScreenBounds: Rect = (0,0,0,0)
  
  var direction = Direction(0, 0)
  
  map.tiles(position).unit = Option(this)
  map.removeShadeAround(position)
  
  def tick()
}