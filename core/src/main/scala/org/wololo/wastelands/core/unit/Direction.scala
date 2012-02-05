package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

object Direction {
  implicit def direction2TileIndex(direction: Direction): Int = {
    direction match {
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

  implicit def tileIndex2direction(tileIndex: Int): Direction = {
    tileIndex match {
      case 0 => Direction(0, -1)
      case 1 => Direction(1, -1)
      case 2 => Direction(1, 0)
      case 3 => Direction(1, 1)
      case 4 => Direction(0, 1)
      case 5 => Direction(-1, 1)
      case 6 => Direction(-1, 0)
      case 7 => Direction(-1, -1)
    }
  }
}

case class Direction(dx: Int, dy: Int) extends Coordinate(dx, dy) {
  require(dx >= -1 && dx < 2)
  require(dy >= -1 && dy < 2)

  def leftOf(): Direction = {
    if (this.toInt == 0) 7 else this.toInt - 1
  }

  def rightOf(): Direction = {
    if (this.toInt == 7) 0 else this.toInt + 1
  }
}
