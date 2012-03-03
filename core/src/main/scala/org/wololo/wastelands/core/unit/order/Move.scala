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
    val direction = calcDirection(unit.game.map, unit.position)

    if (direction.isDefined) {
      if (unit.direction != direction.get)
        return Option(new Turn(unit, direction.get))
      else
        return Option(new org.wololo.wastelands.core.unit.action.Move(unit))
    } else {
      return None
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

      return Option(direction)
    } else {
      return None
    }
  }
}