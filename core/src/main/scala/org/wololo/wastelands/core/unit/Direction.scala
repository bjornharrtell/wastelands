package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

object Direction {
  implicit def direction2TileIndex(direction: Direction): Int = Directions.indexOf(direction)
  implicit def tileIndex2direction(tileIndex: Int): Direction = Directions(tileIndex)

  val Directions = Array(
    Direction(0, -1),
    Direction(1, -1),
    Direction(1, 0),
    Direction(1, 1),
    Direction(0, 1),
    Direction(-1, 1),
    Direction(-1, 0),
    Direction(-1, -1))
}

case class Direction(dx: Int, dy: Int) extends Coordinate(dx, dy) {
  require(dx >= -1 && dx < 2)
  require(dy >= -1 && dy < 2)

  import Direction._

  def ==(direction: Direction) = this.toInt == direction.toInt
  def !=(direction: Direction) = !(this == direction)

  def leftOf: Direction = if (this.toInt == 0) 7 else this.toInt - 1
  def rightOf: Direction = if (this.toInt == 7) 0 else this.toInt + 1

  def turnTowards(target: Direction): Direction = {
    var delta = this.toInt - target.toInt

    if (delta > 0) {
      if (math.abs(delta) >= 4) {
        rightOf
      } else {
        leftOf
      }
    } else {
      if (math.abs(delta) >= 4) {
        leftOf
      } else {
        rightOf
      }
    }
  }

  override def clone: Direction = new Direction(dx, dy)
}
