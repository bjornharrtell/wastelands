package org.wololo.wastelands.core

case class Direction(dx: Int, dy: Int) extends Coordinate(dx,dy) {
  require(dx >= -1 && dx < 2)
  require(dy >= -1 && dy < 2)
}
