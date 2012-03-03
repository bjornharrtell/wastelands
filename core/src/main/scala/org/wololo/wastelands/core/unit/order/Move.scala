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
    val directionOption = calcDirection(unit.game.map, unit.position)

    directionOption match {
      case Some(direction) if (direction != unit.direction) =>
        Option(new Turn(unit, direction))
      case Some(direction) =>
        Option(new org.wololo.wastelands.core.unit.action.Move(unit))
      case None =>
        None
    }
  }

  /**
   * TODO: replace with pathfinding
   */
  private def calcDirection(map: GameMap, position: Coordinate): Option[Direction] = {
    val delta = destination - position

    val isDestinationReached = delta == (0, 0)

    if (!isDestinationReached) {
      // calculate tile directions per axis
      val dx = math.signum(delta.x)
      val dy = math.signum(delta.y)

      var direction = Direction(dx, dy)

      // if direction is obstructed try left/right
      if (map.tiles(position + direction).isOccupied) {
        direction = direction.leftOf
        if (map.tiles(position + direction).isOccupied) {
          direction = direction.rightOf
          if (map.tiles(position + direction).isOccupied) return None
        }
      }

      Option(direction)
    } else {
      None
    }
  }
}