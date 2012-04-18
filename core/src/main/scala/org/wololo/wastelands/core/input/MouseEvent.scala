package org.wololo.wastelands.core.input

import org.wololo.wastelands.core.Coordinate

sealed abstract class MouseEvent extends InputEvent {
  def coordinate: Coordinate
}

sealed abstract class MouseButtonEvent extends MouseEvent {
  def clicks: Int
}

case class MouseClicked(coordinate: Coordinate, clicks: Int) extends MouseButtonEvent {

}