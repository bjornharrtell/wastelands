package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

object Direction {
  val Directions = Array(
    Direction(0, -1),
    Direction(1, -1),
    Direction(1, 0),
    Direction(1, 1),
    Direction(0, 1),
    Direction(-1, 1),
    Direction(-1, 0),
    Direction(-1, -1))
    
  def fromTileIndex(tileIndex: Int) = Directions(tileIndex).clone()
}

case class Direction(dx: Int, dy: Int) extends Coordinate(dx, dy) {
  import Direction._
  
  require(dx >= -1 && dx < 2)
  require(dy >= -1 && dy < 2)
  
  def leftOf: Direction = if (this.toTileIndex == 0) Directions(7) else Directions(this.toTileIndex - 1)
  def rightOf: Direction = if (this.toTileIndex == 7) Directions(0) else Directions(this.toTileIndex + 1)
  
  def toTileIndex: Int = Directions.indexOf(this)

  def turnTowards(target: Direction): Direction = {
    var delta = this.toTileIndex - target.toTileIndex

    if (delta > 0)
      if (math.abs(delta) >= 4) rightOf else leftOf
    else
      if (math.abs(delta) >= 4) leftOf else rightOf
  }

  override def clone: Direction = new Direction(dx, dy)
}
