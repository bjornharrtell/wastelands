package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer.Canvas

/**
 * TODO: probably needs refactoring for more intelligent rendering with other parts
 */
abstract class Unit(map: GameMap, var position: Coordinate) {

  val MoveStatusIdle = 0
  val MoveStatusMoving = 1
  val MoveStatusPausing = 2

  var velocity = 0.04
  var direction = Direction(0, 0)
  var destination = position.clone
  var nextDestination = destination.clone

  var moveDistance = 0.0

  var moveStatus = MoveStatusIdle
  val MovePauseTicks = 15
  var movePauseTicksCounter = MovePauseTicks

  var selected = false
  def select() { selected = true }
  def unselect() { selected = false }

  map.tiles(position).unit = Option(this)
  map.removeShadeAround(position)

  def tick() {
    tickMove
  }

  def startMove() {
    moveStatus = MoveStatusMoving
  }

  def moveTo(coordinate: Coordinate) {
    nextDestination = coordinate
  }

  def calcMove() {
    var delta = destination - position

    // destination reached, bail
    if (delta == (0, 0)) return

    val dx = math.signum(delta.x)
    val dy = math.signum(delta.y)

    direction = Direction(dx, dy)

    if (map.tiles(position + direction).isOccupied) return

    startMove
  }

  def tickMove() {
    if (moveStatus == MoveStatusMoving) {
      if (moveDistance == 0) {
        map.tiles(position).unit = None
        map.tiles(position + direction).unit = Option(this)
      }

      moveDistance += velocity

      if (moveDistance >= 1) {
        moveDistance = 0

        position += direction

        map.removeShadeAround(position)

        moveStatus = MoveStatusPausing
      }
    } else if (moveStatus == MoveStatusPausing) {
      movePauseTicksCounter += -1

      if (movePauseTicksCounter == 0) {
        movePauseTicksCounter = MovePauseTicks
        moveStatus = MoveStatusIdle
        destination.setTo(nextDestination)
        calcMove
      }
    } else if (moveStatus == MoveStatusIdle) {
      if (nextDestination != destination) {
        destination.setTo(nextDestination)
        calcMove
      }
    }
  }

}