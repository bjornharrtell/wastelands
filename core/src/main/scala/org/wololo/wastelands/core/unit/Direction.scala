package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

object Direction {
  val Directions = Array(
    new Direction(0, -1),
    new Direction(1, -1),
    new Direction(1, 0),
    new Direction(1, 1),
    new Direction(0, 1),
    new Direction(-1, 1),
    new Direction(-1, 0),
    new Direction(-1, -1))

  def fromTileIndex(tileIndex: Int) = Directions(tileIndex).copy()
  def random = Direction.fromTileIndex((math.random * 7 + 1).toInt)
  implicit def tuple2Direction(tuple: (Int, Int)): Direction = Direction(tuple._1, tuple._2)
  implicit def direction2Tuple(direction: Direction): (Int, Int) = (direction.x, direction.y)
}

case class Direction(x: Int, y: Int) {
  import Direction._

  require(x >= -1 && x < 2)
  require(y >= -1 && y < 2)

  def leftOf: Direction = if (this.toTileIndex == 0) fromTileIndex(7) else fromTileIndex(this.toTileIndex - 1)
  def rightOf: Direction = if (this.toTileIndex == 7) fromTileIndex(0) else fromTileIndex(this.toTileIndex + 1)

  def toTileIndex: Int = Directions.indexOf(this)

  def turnTowards(target: Direction): Direction = {
    var delta = this.toTileIndex - target.toTileIndex

    if ((delta & 7) > 4) rightOf else leftOf
  }
}
