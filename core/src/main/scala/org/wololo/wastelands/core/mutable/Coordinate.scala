package org.wololo.wastelands.core.mutable
import org.wololo.wastelands.core.unit.Direction

/**
 * Mutable coordinate
 * 
 * Purpose is to lower amount of object allocations in rendering for Android performance
 */
class Coordinate(var x: Int = 0, var y: Int = 0) {
  def +=(coordinate: Coordinate) = { x += coordinate.x; y += coordinate.y; }
  
  def setTo(x:Int, y:Int) = { this.x = x; this.y = y; }
}