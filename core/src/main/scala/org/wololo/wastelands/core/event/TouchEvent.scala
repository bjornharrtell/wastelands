package org.wololo.wastelands.core.event

import org.wololo.wastelands.core.Coordinate

object TouchEvent {
  val DOWN = 0
  val UP = 1
  val MOVE = 2
}

case class TouchEvent(val coordinate: Coordinate, val action: Int) extends Event {
  
}