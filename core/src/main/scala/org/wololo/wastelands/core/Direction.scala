package org.wololo.wastelands.core


case class Direction(x: Int,  y: Int) {
  require(x >= -1 && x < 2)
  require(y >= -1 && y < 2)
}
