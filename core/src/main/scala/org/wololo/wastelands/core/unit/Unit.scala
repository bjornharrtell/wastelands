package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

abstract class Unit(val map: GameMap, val player: Int, val position: Coordinate) extends Movable with Selectable {
  var visible = false
  val ScreenBounds: Rect = (0,0,0,0)
  
  var direction = Direction(0, 0)
  
  map.tiles(position).unit = Option(this)
}