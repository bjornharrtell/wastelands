package org.wololo.wastelands.core.unit.order

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Order
import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.unit.Direction
import org.wololo.wastelands.core.GameMap
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.action.Turn

class Move(unit: Unit, destination: Coordinate) extends Order(unit: Unit) {

  override def generateAction(): Option[Action] = {
    val direction = unit.game.map.calcDirection(unit.position, destination)

    if (direction.isDefined) {
      if (unit.direction != direction.get)
        return Option(new Turn(unit, direction.get))
      else
        return Option(new org.wololo.wastelands.core.unit.action.Move(unit))
    } else {
      return None
    }
  }
  
}