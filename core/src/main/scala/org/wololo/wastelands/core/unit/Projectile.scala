package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Coordinate

object Projectile {
  val TileDuration = 5
}

case class Projectile(start: Int, from: Coordinate, to: Coordinate) {
  val duration = Projectile.TileDuration * from.distance(to)
}