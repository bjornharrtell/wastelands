package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

object Direction {
  implicit def direction2TileIndex(direction: Direction): Int = {
    direction match {
      case Direction(0, 0) => 0
      case Direction(0, -1) => 0
      case Direction(1, -1) => 1
      case Direction(1, 0) => 2
      case Direction(1, 1) => 3
      case Direction(0, 1) => 4
      case Direction(-1, 1) => 5
      case Direction(-1, 0) => 6
      case Direction(-1, -1) => 7
    }
  }
}

case class Direction(dx: Int, dy: Int) extends Coordinate(dx,dy) {
  require(dx >= -1 && dx < 2)
  require(dy >= -1 && dy < 2)
}
