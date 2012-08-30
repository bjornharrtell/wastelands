package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Coordinate

object Projectile {
  // TODO: shouldn't be constant, depends on range...
  val Duration = 20
}

case class Projectile(start: Int, from: Coordinate, to: Coordinate) {
  
}