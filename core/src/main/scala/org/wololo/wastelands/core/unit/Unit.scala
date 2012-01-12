package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

/**
 * TODO: probably needs refactoring for more intelligent rendering with other parts
 */
abstract class Unit(val map: GameMap, val position: Coordinate) extends Movable with Selectable {
  var direction = Direction(0, 0)
  
  map.tiles(position).unit = Option(this)
  map.removeShadeAround(position)
}